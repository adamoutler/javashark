package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskManager;
import com.babysharks.messaging.ErrorType;
import static com.babysharks.messaging.ErrorType.NOSENTRIESACTIVE;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"whoauthorizedthis","data":{"gang":"development","username":"adam_outler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class AuthorizedHandler extends SentryAbstractPapaHandler {

    final static String OUTPUT = "/var/www/baby-sharks/screenshots/";

    public AuthorizedHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        //log the request URI
        System.out.println(t.getRequestURI() + "\n Using machine for screenshot: " + this.username + this.gang);
        //check if the machine is powered on.  if not do the following:
        String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + this.username + this.gang.toLowerCase() + "-") + UUID.randomUUID().toString() + ".png";

        if (vm == null || !vm.isPoweredOn()) {
            //log the machine is off.

            System.out.println(this.gang + " " + this.username + " requested machine is powered off.");
            vm = null;
            // for each machine in the gang
            for (VirtualBoxMachine thisVm : TaskManager.getGangMachines(gang)) {
                //check if the machine is powered on
                if (thisVm.isPoweredOn()) {

                    System.out.println(" selecting " + thisVm.getVMName());
                    vm = thisVm;
                    safeName = getOnlyStrings(System.currentTimeMillis() + "-" + vm.getVMName().toLowerCase() + "-") + UUID.randomUUID().toString() + ".png";

                    break;
                }
            }
        }

        File outputFile = new File(OUTPUT + safeName);
        BufferedImage image;
        if (vm == null) {
            request.getData().setUrl(null);
            request.getData().setErrorType(NOSENTRIESACTIVE);
            this.sendArbitrary(404, t, username);
        } else {
            image = vm.screenshot();
            PicCoords theirScore = new PicCoords(632, 69, 213, 118);
            BufferedImage whoAuthorizedThis = theirScore.getImageAtCoords(image);
            ImageIO.write(whoAuthorizedThis, "PNG", outputFile);
            ScreenData screen = ScreenData.identifyScreen(image);
            request.getData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName).setResultObject(screen).setText(screen.getDescription());
        }

        System.out.println("sending message with URL back: " + request.getData().getUrl());
        request.getData().setErrorType(ErrorType.NOERROR);
        sendSucessMessage(t, request);
    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

}

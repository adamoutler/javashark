package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * curl -X PATCH -d '{"message":{"command":"screenshot","data":{"gang":"development","username":"adam_outler"}}}' https://baby-sharks.adamoutler.com/api/dev/command
 * @author adamo
 */
public class ScreenshotHandler extends SentryAbstractPapaHandler {

    final public static String OUTPUT = "/var/www/baby-sharks/screenshots/";

    public ScreenshotHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        boolean analyze=false;
        
        if (request.getData().getResultObject()!=null&&((String)request.getData().getResultObject()).equals("analyze")){
            analyze=true;
        }
        if (username.contains("analyze")){
                analyze=true;
        }
        if (username.contains(" ")){
            username=username.split(" ")[0];
        }
        //
        System.out.println(t.getRequestURI() + "\n Using machine for screenshot: " + this.username + this.gang);
        //check if the machine is powered on.  if not do the following:
        String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + this.username + this.gang.toLowerCase() + "-") + UUID.randomUUID().toString() + ".png";

        if (vm == null || !vm.isPoweredOn()) {
            //log the machine is off.

            System.out.println(this.gang+" "+this.username+" requested machine is powered off.");
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
        } else {
            image = vm.screenshot();
            ScreenData sd=ScreenData.identifyScreen(image);
            ImageIO.write(image, "PNG", outputFile);
            request.getData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName).setResultObject(sd).setReason(vm.getUsername());
            if (analyze){
                request.getData().setResultObject(new CityKingsBattlefieldAnalysis(image));
            }
        }

        System.out.println("sending message with URL back: " + request.getData().getUrl());
        sendSucessMessage(t, request);
    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

}

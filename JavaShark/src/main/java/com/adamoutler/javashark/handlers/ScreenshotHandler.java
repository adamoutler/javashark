package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.FakeScreenshot;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class ScreenshotHandler extends SentryAbstractPapaHandler {

    final static String OUTPUT = "/var/www/baby-sharks/screenshots/";

    public ScreenshotHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        //log the request URI
        System.out.println(t.getRequestURI() + "\n Using machine for screenshot: " + vm.getVMName());
        //check if the machine is powered on.  if not do the following:
        String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + vm.getVMName().toLowerCase() + "-") + UUID.randomUUID().toString() + ".png";

        if (!vm.isPoweredOn()) {
            //log the machine is off.

            System.out.println(vm.getVMName() + " is powered off.");
            vm = null;
            // for each machine in the gang
            for (VirtualBoxMachine thisVm : new VirtualBox().getGangMachines(gang)) {
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
            image = FakeScreenshot.getScreenshot(gang, username);
        } else {
            image = vm.screenshot();
        }
        ImageIO.write(image, "PNG", outputFile);
        request.getData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName);
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

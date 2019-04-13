package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        ///init(t);
        long time = System.currentTimeMillis();
        System.out.println(t.getRequestURI());
        if (!vm.isPoweredOn()){
            for (VirtualBoxMachine thisVm:new VirtualBox().getGangMachines(gang)){
                if (thisVm.isPoweredOn()){
                    vm=thisVm;
                    break;
                }
            }
        }
        BufferedImage bi=vm.screenshot();
        String safeName = getOnlyStrings(vm.getVMName()) + vm.getVMName().toLowerCase()+time + ".png";
        File outputFile = new File(OUTPUT + safeName);
        ImageIO.write(bi, "PNG", outputFile);
        Message message = new Message().setCommand("screenshot")
                .setData(new MessageData().setUsername(username)
                        .setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName));
        System.out.println("sending message: " + message.getData().getResultObject());
        sendSucessMessage(t, message);
    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

}

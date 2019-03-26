package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.virtualbox.VirtualBoxScreenShot;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;

/**
 *
 * @author adamo
 */
public class ScreenshotHandler extends AbstractPapaHandler {


    final static String OUTPUT = "/var/www/baby-sharks/screenshots/";

    public ScreenshotHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        ///init(t);
        long time = System.currentTimeMillis();
        System.out.println(t.getRequestURI());
        String username = getRequest().getData().getUsername();

        
        VirtualBoxMachine machine=new VirtualBox().getUserMachine(getRequest().getData().getGang(), getRequest().getData().getUsername());
        String vmname = machine.getVMName();
        String name = this.getScreenshot(vmname.toLowerCase(), time);

        Message message = new Message().setCommand("screenshot")
                .setData(new MessageData().setUsername(username)
                        .setUrl((name.isEmpty()) ? "null" : "https://baby-sharks.adamoutler.com/screenshots/" + name));

        System.out.println("sending message: " + message.getData().getResultObject());
        sendSucessMessage(t, message);
    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    private String getScreenshot(String name, long uniquepic) {
        String safeName = getOnlyStrings(name) + uniquepic + ".png";
        File outputFile = new File(OUTPUT + safeName);
        try {
            VirtualBoxScreenShot.getScreenshotFile(name, outputFile);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ScreenshotHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (outputFile.exists()) {
            return safeName;
        }
        return "";

    }

}

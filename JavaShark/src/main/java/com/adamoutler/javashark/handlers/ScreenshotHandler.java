package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.googletools.LocationTime;
import virtualbox.VirtualBoxScreenShot;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author adamo
 */
public class ScreenshotHandler extends AbstractPapaHandler {

    final String key = "REPLACE_ME";

    final static String output = "/var/www/baby-sharks/screenshots/";

    public ScreenshotHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        ///init(t);
        long time = System.currentTimeMillis();
        System.out.println(t.getRequestURI());

        String vmname = getRequest().getData().getUsername();
        String name = this.getScreenshot(vmname, time);

        Message message = new Message().setCommand("screenshot")
                .setData(new MessageData().setUsername(vmname)
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
        File outputFile = new File(output + safeName);
        try {
            VirtualBoxScreenShot.getScreenshot(name, outputFile);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ScreenshotHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (outputFile.exists()) {
            return safeName;
        }
        return "";

    }

}

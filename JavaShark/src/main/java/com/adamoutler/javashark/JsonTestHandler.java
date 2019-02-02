package com.adamoutler.javashark;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author adamo
 */
public class JsonTestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
       
        
        HashedMessage message=new HashedMessage(
                new Message().setCommand("alert")
                        .setBuilding(
                                Integer.toString((int)(Math.random() * 6) + 1))
                        .setMessage("just a test, but more stuff can go here"));
        String response=message.toJson();
        t.sendResponseHeaders(200, response.length());

        try (OutputStream os = t.getResponseBody()) {
            os.write(response.getBytes());
        }
    
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

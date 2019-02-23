/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

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
public abstract class AbstractPapaHandler implements  HttpHandler {

    Message request;
    
    public void setMessage(Message m){
        this.setRequest(m);
    }

    public void sendSucessMessage(HttpExchange t, Message message) throws IOException {
        
        HashedMessage hm=new HashedMessage(message);
        String response = hm.toJson();
        t.sendResponseHeaders(200, response.length());
        writeOutResponse(t, response);
    }
    
    public void sendSucessMessage(HttpExchange t, HashedMessage message) throws IOException {
        String response = message.toJson();
        System.out.println("Sending response  "+ response);
        t.sendResponseHeaders(200, response.length());
        writeOutResponse(t, response);
    }

    public void sendSucessString(HttpExchange t, String response) throws IOException {
        t.sendResponseHeaders(200, response.length());
        writeOutResponse(t, response);
    }

    public void sendBadReq(HttpExchange t, String response) throws IOException {
        t.sendResponseHeaders(400, response.length());
        writeOutResponse(t, response);
    }

    public void sendArbitrary(int code, HttpExchange t, String response) throws IOException {
        t.sendResponseHeaders(code, response.length());
        writeOutResponse(t, response);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void writeOutResponse(HttpExchange t, String response) throws IOException {
        try (OutputStream os = t.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    /**
     * @return the request
     */
    public Message getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(Message request) {
        this.request = request;
    }

    
}

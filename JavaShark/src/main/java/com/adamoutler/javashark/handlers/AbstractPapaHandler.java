/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import static com.babysharks.messaging.ErrorType.NOERROR;
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
public abstract class AbstractPapaHandler implements HttpHandler {

    Message request;

    public void setMessage(Message m) {
        this.setRequest(m);
    }

    public void sendSucessMessage(HttpExchange t, Message message) throws IOException {
        message.getData().setErrorType(NOERROR);
        HashedMessage hm = new HashedMessage(message);
        String response = hm.toJson();
        writeOutResponse(t, response, 200);
    }

    public void sendSucessMessage(HttpExchange t, HashedMessage message) throws IOException {
        message.getMessage().getData().setErrorType(NOERROR);
        String response = message.toJson();
        System.out.println("Sending response  " + response);
        writeOutResponse(t, response, 200);
    }

    public void sendSucessString(HttpExchange t, String response) throws IOException {
        writeOutResponse(t, response, 200);
    }
    public void sendBadReq(HttpExchange t, Message response) throws IOException {
        
        writeOutResponse(t, response.toJson(), 200);
    }
 
    public void sendConflictReq(HttpExchange t, Message response) throws IOException {
        writeOutResponse(t, response.toJson(), 200);
    }

    
    public void sendArbitrary(HttpExchange t, Message response) throws IOException {
        writeOutResponse(t, response.toJson(), 200);
    }
  

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void writeOutResponse(HttpExchange t, String response, int code) throws IOException {
        byte[] responseBytes = response.getBytes("UTF-8");
        String s = new String(responseBytes);
        System.out.println(s);
        t.sendResponseHeaders(code, responseBytes.length);
        try (OutputStream responseStream = t.getResponseBody()) {
            responseStream.write(responseBytes);
        }
        t.close();
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

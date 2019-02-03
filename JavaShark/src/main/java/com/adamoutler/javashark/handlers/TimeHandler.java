package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.adamoutler.googletools.LocationTime;
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
public class TimeHandler extends AbstractPapaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        
        
        String uri=t.getRequestURI().toASCIIString();
        System.out.println(t.getRequestURI());
        String place=uri.substring(uri.lastIndexOf("/")+1,uri.length());
        String s=new LocationTime().getDetails("REPLACE_ME", place);
        HashedMessage message=new HashedMessage(
                new Message().setCommand("speak")
                        .setMessage("It is "+s));
        sendSucessMessage(t, message);    
    }

 


}

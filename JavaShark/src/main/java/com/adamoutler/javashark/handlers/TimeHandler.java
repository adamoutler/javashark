package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.adamoutler.googletools.LocationTime;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.URLDecoder;

/**
 *
 * @author adamo
 */
public class TimeHandler extends AbstractPapaHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        ///init(t);
        
        String uri=t.getRequestURI().toASCIIString();
        System.out.println(t.getRequestURI());
        String place=message.getData().getArgs();
        HashedMessage message=new HashedMessage(
                new Message().setCommand("time")
                        .setData(new MessageData()
                                .setResultObject(LocationTime.getDateTimeObject( place,"REPLACE_ME"))));
        
        System.out.println("sending message: "+message.getMessage().getData().getResultObject());
        sendSucessMessage(t, message);    
    }

 


}

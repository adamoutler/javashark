package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 *
 * @author adamo
 */
public class JsonTestHandler extends AbstractPapaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
       
        
        HashedMessage message=new HashedMessage(
                new Message().setCommand("alert")
                        .setData(new MessageData()
                               
                                .setEmoji("this crashes when I use emoji...")
                                .setNobodyOwns("3,4").setTheyOwn("5,6")
                                .setWeOwn("1,2").setText("help!")
                                .setReason("alert")
                                .setBuildings(new String[]{Integer.toString((int)(Math.random() * 6) + 1)})));
        sendSucessMessage(t, message);    
    }

 


}

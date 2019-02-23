package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import virtualbox.Manager;

/**
 *
 * @author adamo
 */
public class SentryStatus extends AbstractPapaHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        ///init(t);
        MessageData md=request.getData();
        String gang=md.getGang();
        Manager vbox=new Manager();
         Message response = new Message().setCommand("sentrystatus")
                .setData(new MessageData()
                        .setGang(gang)
                        .setActive(vbox.getActiveGangMachines(gang).toString())
                        .setAvailable(vbox.getGangMachines(gang).toString()));
         sendSucessMessage(t, response);
    }
}

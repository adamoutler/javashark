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
import java.io.IOException;

/**
 *
 * @author adamo
 */
public class TimeHandler extends AbstractPapaHandler {

    final String key = "REPLACE_ME";

    public TimeHandler() {
        firstContactGoogle();
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String place = getRequest().getData().getLocation();

        HashedMessage message = new HashedMessage(
                new Message().setCommand("time")
                        .setData(new MessageData()
                                .setResultObject(LocationTime.getDateTimeObject(place, key))));
        sendSucessMessage(t, message);
    }

    private void firstContactGoogle() {
        Thread googleInit=new Thread(() -> {
            LocationTime.getDateTimeObject("fez's Mom's house", key);
        });
        googleInit.setName("Google Security Establishment");
        googleInit.start();
    }
}

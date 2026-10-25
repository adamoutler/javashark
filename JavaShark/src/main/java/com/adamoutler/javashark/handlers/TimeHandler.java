package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.googletools.LocationTime;
import static com.babysharks.messaging.ErrorType.NOPLACEINFO;
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

    final String key = System.getenv().getOrDefault("GOOGLE_API_KEY", System.getProperty("google.api.key", ""));

    public TimeHandler() {
        firstContactGoogle();
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            String place = getRequest().getData().getLocation();
            HashedMessage message = new HashedMessage(
                    new Message().setCommand("time")
                            .setData(new MessageData()
                                    .setResultObject(LocationTime.getDateTimeObject(place, key))));
            sendSucessMessage(t, message);
        } catch (Exception ex) {
            request.getData().setErrorType(NOPLACEINFO);

            this.sendBadReq(t, request);
        }
    }

    private void firstContactGoogle() {
        Thread googleInit = new Thread(() -> {
            LocationTime.getDateTimeObject("fez's Mom's house", key);
        });
        googleInit.setName("Google Security Establishment");
        googleInit.start();
    }
}

package com.adamoutler.javashark.handlers;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * curl -X PATCH -d '{"message":{"command":"sentrystatus","data":{"gang":"development","username":"adamoutler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class SentryStatus extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        VirtualBox vbox = new VirtualBox();

        ArrayList<VirtualBoxMachine> active = vbox.getActiveGangMachines(gang);
        Message response = new Message().setCommand("sentrystatus")
                .setData(new MessageData().setGang(gang)
                        .setActive(active)
                        .setAvailable(vbox.getGangMachines(gang)));
        for (VirtualBoxMachine v : active) {
            ScreenData sd = ScreenData.identifyScreen(v.screenshot());
            v.setScreenID(sd);
            v.setScreenDescription(sd.getDescription());
        }
        sendSucessMessage(t, response);
    }
}

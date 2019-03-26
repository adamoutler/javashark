/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.cats.screen_id.ScreenData;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.Message;
import java.util.ArrayList;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"sentrystart","data":{"gang":"development","username":"adamoutler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class sentrydetail extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        ArrayList<VirtualBoxMachine> al = new ArrayList<>();
        al.add(vm);
        Message m = getRequest().setCommand("stopsentry").setData(this.getRequest().getData().setActive(al).setReason("Stopping machine"));
        this.sendSucessMessage(he, m);
        VirtualBoxControl vmControl = new VirtualBoxControl(vm);
        vmControl.stopVM();

    }

}

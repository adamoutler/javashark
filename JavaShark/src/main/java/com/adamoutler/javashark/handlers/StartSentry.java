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
public class StartSentry extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        long time = System.currentTimeMillis();
        ArrayList<VirtualBoxMachine> al = new ArrayList<>();
        al.add(vm);
        Message m = new Message().setCommand("sentrystart").setData(this.getRequest().getData().setActive(al).setReason("Starting machine"));
        new Thread(() -> {
            try {
                this.setMessage(m);
                this.sendSucessMessage(he, m);
                VirtualBoxControl vmControl = new VirtualBoxControl(vm);
                vmControl.startVirtualMachine();
                while (time < System.currentTimeMillis() + 120000) {
                    if (isBooted(vm)) {
                        break;
                    }
                }
                
                vmControl.startCats();
            } catch (IOException ex) {
                Logger.getLogger(StartSentry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();

    }

    private boolean isBooted(VirtualBoxMachine vm) throws IOException {
        ScreenData sd = ScreenData.identifyScreen(vm.screenshot());
        if (sd.equals(sd.BOOTED)) {
            return true;
        }
        return false;
    }

   
}

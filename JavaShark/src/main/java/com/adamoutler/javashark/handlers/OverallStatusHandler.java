/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.javashark.StatusDisplay;
import static com.adamoutler.javashark.handlers.AbstractPapaHandler.convertStreamToString;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.HashedMessage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author adamo
 */
public class OverallStatusHandler extends AbstractPapaHandler {
    
    public OverallStatusHandler() {
    }
    
    @Override
    public void handle(HttpExchange t) throws IOException {
        ArrayList<VirtualBoxMachine> active = new VirtualBox().getMachinesList();
        StatusDisplay sd=new StatusDisplay(this);
        String value=sd.getStatus();
        sendSucessString(t, value);
    }
    
}

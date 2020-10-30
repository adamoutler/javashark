/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.javashark.StatusDisplay;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
        try {
            ArrayList<VirtualBoxMachine> active = new VirtualBox().getMachinesList();
            StatusDisplay sd = new StatusDisplay(this);
            String value = sd.getStatus();
            sendSucessString(t, value);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            ex.printStackTrace(pw);
            sendSucessString(t, sw.toString());
        }
    }

}

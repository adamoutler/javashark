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
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\"><head></head><body>");
//        active.stream().sorted((VirtualBoxMachine a, VirtualBoxMachine b) -> {
//            return a.getGang().compareTo(b.getGang());
//        }).forEach(vm -> {
//            sb.append("<h4>").append(vm.getUsername()).append(" ")
//                    .append(vm.getGang()).append("</h4>");
//            if (vm.isPoweredOn()) {
//                TaskType task = vm.getCurrentTask();
//                ScreenData screen = vm.getScreen();
//                sb.append("<B>ONLINE</B><br>Task:").append(task.name())
//                        .append(" ").append(task.getTaskDescription())
//                        .append("<br>Screen: ").append(screen.name())
//                        .append(" ").append(screen.getDescription())
//                        .append("<br>");
//                
//            }
//            sb.append("- ADB:").append(vm.getAdbName()).append(" RDP:")
//                    .append(vm.getRdpPort()).append("<br>");
//        });
//      
        
        sendSucessString(t, value);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.javashark.handlers.AbstractPapaHandler;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.HashedMessage;
import java.util.ArrayList;

/**
 *
 * @author adamo
 */
public class StatusDisplay {

    final AbstractPapaHandler handler;

    public StatusDisplay(AbstractPapaHandler handler) {
        this.handler = handler;
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        ArrayList<VirtualBoxMachine> active = new VirtualBox().getMachinesList();
        sb.append("<head><title>").append(HashedMessage.instance).append(" status</title><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\"/>"
                + "<style> .themed-container {"
                + "    padding: 15px;"
                + "    margin-bottom: 30px;"
                + "    background-color: rgba(0, 123, 255, .15);"
                + "    border: 1px solid rgba(0, 123, 255, .2);"
                + "}"
                + ".img {"
                + "    max-width: auto;"
                + "    max-height: 100%;"
                + "}"
                + ".smcontainer {"
                + "    max-width: 300px;"
                + "    max-height: 200px;"
                + "}"
                + "</style></head><body>");
        sb.append("<H3> JavaShark ").append(HashedMessage.instance).append(" Status</H1>");
        sb.append("<div class=\"w3-white w3-padding \">");
        active.stream().sorted((VirtualBoxMachine a, VirtualBoxMachine b) -> {
            return a.getGang().compareTo(b.getGang());
        }).forEach(vm -> {
            String screenshot = "";
            boolean online = vm.isPoweredOn();
            sb.append("<tr><tc><div class=\"nav section w3-white w3-padding themed-container\"><div class=\"item\">")
                    .append("<h3>")
                    .append(online ? "<span class=\"text-success\">&#x1F4F1 " : "<span class=\"text-dark\">&#x1F4F4 ")
                    .append(vm.getUsername()).append(" ")
                    .append(vm.getGang()).append("</span></h4>");
            if (online) {
                TaskType task = vm.getCurrentTask();
                ScreenData screen = vm.getScreen();
                screenshot = vm.writeScreenToWebServer(vm.getUsername() + "-" + System.currentTimeMillis() + "-status");

                sb.append("<div><div><b>Task:</b> ").append(task.name())
                        .append(" - ").append(task.getTaskDescription())
                        .append("</div><div><b>Screen:</b> ").append(screen.name())
                        .append(" - ").append(screen.getDescription())
                        .append("</div></div>");
            }
            sb.append("<div><div>ADB:").append(vm.getAdbName()).append("</div><div>RDP:")
                    .append(vm.getRdpPort()).append("</div></div>");
            sb.append("<div><small>").append(vm.getVMName()).append("</small></div></div></tc><tc><div >");
            if (!screenshot.isEmpty()) {
                sb.append("<img  style='margin-left: 1em; height: 100%; object-fit: contain' src=\"").append(screenshot).append("\">");
            }

            sb.append("</div></tc></tr></div></div>");
        });
        sb.append("</div>");
        return sb.toString();
    }
;
}

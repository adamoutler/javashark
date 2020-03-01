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
        sb.append("<html>"
                + "<head><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
                + "<style> .themed-container {\n"
                + "    padding: 15px;\n"
                + "    margin-bottom: 30px;\n"
                + "    background-color: rgba(0, 123, 255, .15);\n"
                + "    border: 1px solid rgba(0, 123, 255, .2);\n"
                + "}"
                + ".sizedimg {\n"
                + "  max-width: 100%;\n"
                + "  position: relative;\n"
                + "  margin-top: -70px;\n"
                + "}"
                + ".imagecontainer {\n"
                + "  width: 100%;\n"
                + "  height: 100%;\n"
                + "  border: 1px solid #D9D9D9;\n"
                + "  min-height: 350px;\n"
                + "  max-height: 350px;\n"
                + "  overflow: hidden;\n"
                + "}"
                + "</style></head><body>");
        sb.append("<H1> JavaShark ").append(HashedMessage.instance).append(" Status</H1>");
        sb.append("<div class=\"w3-white w3-padding \">");
        active.stream().sorted((VirtualBoxMachine a, VirtualBoxMachine b) -> {
            return a.getGang().compareTo(b.getGang());
        }).forEach(vm -> {
            String screenshot = "";
            boolean online = vm.isPoweredOn();
            sb.append("<tr><tc><div class=\"nav section w3-white w3-padding themed-container\"><div class=\"item\">")
                    .append("<h4>")
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
            sb.append("<div><small>").append(vm.getVMName())
                    .append("</small></div></div></tc><tc><div class=\"aside imagecontainer\">")
                    .append("<img style=\"sizedimg\" src=\"").append(screenshot).append("\"></tc></tr></div>");

            sb.append("</div></div>");
        });
        sb.append("</div>");
        return sb.toString();
    }
;
}

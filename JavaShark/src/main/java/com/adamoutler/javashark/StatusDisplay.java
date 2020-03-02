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
        sb.append("<html><head><title>").append(HashedMessage.instance).append(" status</title><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\"/>"
                + "<style>"
                + ".body {"
                + "word-wrap: break-word;"
                + "}"
                + " .themed-container {"
                + "    padding: 15px;"
                + "    margin-bottom: 30px;"
                + "    background-color: rgba(0, 123, 255, .15);"
                + "    border: 1px solid rgba(0, 123, 255, .2);"
                + "}"
                + ".fiftyPercent{"
                + "    max-width:47%;"
                + ".img {"
                + "    max-width: 47%;"
                + "    max-height: 50%;"
                + "    display: flex; "
                + "    object-fit: scale-down;"
                + "}"
                + ".maxwidth{"
                + "     max-width: 99%;"
                + "}"
                + ".smcontainer {"
                + "    max-width: 300px;"
                + "    max-height: 200px;"
                + "}"
                + "</style></head><body>");
        sb.append("<H2 class=\"javasharkheading\"> JavaShark ").append(HashedMessage.instance).append(" Status</H2>");
        sb.append("<div class=\"w3-white w3-padding \">");
        active.stream().sorted((VirtualBoxMachine a, VirtualBoxMachine b) -> {
            return a.getGang().compareTo(b.getGang());
        }).forEach(vm -> {
            String screenshot = "";
            boolean online = vm.isPoweredOn();
            sb.append("<div class=\"maxwidth useritem\"><div class=\"nav section w3-white w3-padding themed-container\"><tr class=\"maxwidth tablerow\"><tc class=\"fiftyPercent\" >")
                    .append("<h3  ")
                    .append(online ? "class=\"userheading text-success\">&#x1F4F1 " : "class=\"userheading text-dark\">&#x1F4F4 ")
                    .append(vm.getUsername()).append(" ")
                    .append(vm.getGang()).append("</h3>");
            if (online) {
                TaskType task = vm.getCurrentTask();
                ScreenData screen = vm.getScreen();
                screenshot = vm.writeScreenToWebServer(vm.getUsername() + "-" + System.currentTimeMillis() + "-status");
                if (HashedMessage.instance.equals("developmenttest")) {
                    screenshot="https://javashark.baby-sharks.com/screenshots/Baluhubsa0c6f5c1-9d86-4f0e-9bef-a41231111880.jpg";
                }
                sb.append("<div class=\"machineinfo\"><div class=\"taskinfo\"><b>Task:</b> ").append(task.name())
                        .append(" - ").append(task.getTaskDescription())
                        .append("</div><div class=\"screeninfo\"><b>Screen:</b> ").append(screen.name())
                        .append(" - ").append(screen.getDescription())
                        .append("</div></div>");
            }
            sb.append("<div class=\"portinfo\"><div class=\"adbinfo\">ADB:").append(vm.getAdbName()).append("</div><div>RDP:")
                    .append(vm.getRdpPort()).append("</div>");
            sb.append("<divclass=\"vmname\"><small>").append(vm.getVMName()).append("</small></div></tc><tc class=\"fiftyPercent\">");
            if (!screenshot.isEmpty()) {
                sb.append("<a href=\"").append(screenshot).append("\">").append("<img class=\"screenshot fiftyPercent\"  src=\"").append(screenshot).append("\"></a>");
            }

            sb.append("</tc></tr></div></div>");
        });
        sb.append("</div>");
        return sb.toString();
    }
;
}

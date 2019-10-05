/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import static com.babysharks.messaging.ErrorType.OTHERSENTRYACTIVE;
import static com.babysharks.messaging.ErrorType.VMERROR;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            vm.clearTasks();
            ArrayList<VirtualBoxMachine> gangMachines = new VirtualBox().getGangMachines(gang);
            for (VirtualBoxMachine machine : gangMachines) {
                if (machine.getCurrentTask().equals(TaskType.WATCH)) {
                    request.getData().setReason("Other Sentry Active");
                    this.sendConflictReq(he, username, OTHERSENTRYACTIVE);
                    return;
                }

            }
        } catch (Exception ex) {
            this.sendBadReq(he, username, VMERROR);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);

            return;
        }

        addTasks(TaskType.POWERON, TaskType.STARTCATS, TaskType.CORRECTIVEACTIONCK, TaskType.WATCH);
        request.getData().addAvailable(vm).setReason("starting");
        this.sendSucessMessage(he, request);

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import static com.babysharks.messaging.ErrorType.USERNOTONWATCH;
import static com.babysharks.messaging.ErrorType.VMERROR;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 *
 * curl -X PATCH -d
 * '{"message":{"command":"mycars","data":{"gang":"bs2","username":"Baluhu"}}}'
 * https://"+MainApp.serverUrl+"/api/dev/command
 *
 */
public class MyCarsHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        try {
            if (vm.getCurrentTask() != TaskType.WATCH) {
                request.getData().setErrorType(USERNOTONWATCH);

                this.sendConflictReq(he, request);
                return;
            }
            vm.getTaskData().setTelegramid(request.getData().getTelegramid());
            vm.setTemporaryTask(TaskType.MYCARS);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            request.getData().setErrorType(VMERROR);

            this.sendConflictReq(he, request);
            return;

        }
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        request.getData().setAvailable(vms);
        this.sendSucessMessage(he, request);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.task.TaskType;
import static com.babysharks.messaging.ErrorType.VMERROR;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * curl -X PATCH -d *
 * '{"message":{"command":"deepbuildingscan","data":{"gang":"development","username":"adamoutler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class DeepBuildingScanHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            vm.clearTasks();
            vm.setHashes(request.getData().getHashes());
        } catch (Exception ex) {
            request.getData().setErrorType(VMERROR);

            this.sendBadReq(he, request);
            return;
        }
        addTasks(TaskType.POWERON, TaskType.STARTCATS, TaskType.CORRECTIVEACTIONCK, TaskType.DEEPBUILDINGSCAN);
        request.getData().addAvailable(vm).setReason("scanning");
        this.sendSucessMessage(he, request);
    }
}

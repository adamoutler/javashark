/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.ErrorType;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * curl -X PATCH -d * '{"message":{"command":"deepbuildingrecon","data":{"gang":"development","username":"adamoutler"}}}' https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class DeepBuildingReconHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
        vm.clearTasks();
        vm.setHashes(request.getData().getHashes());
        } catch (Exception ex){
          this.sendBadReq(he, username, ErrorType.VMERROR);
          return;
        }
        addTasks(TaskType.POWERON, TaskType.STARTCATS, TaskType.CORRECTIVEACTIONCK, TaskType.DEEPBUILDINGRECON);
        request.getData().addAvailable(vm).setReason("reconoitering");
        this.sendSucessMessage(he, request);
    }
}

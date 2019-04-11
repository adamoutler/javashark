/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.Message;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"deepbuildingscan","data":{"gang":"development","username":"adamoutler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class UpdateHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        vm.clearTasks();
        addTasks(TaskType.POWERON, TaskType.IDLE1MINUTE, TaskType.UPDATECATS);
        request.getData().addAvailable(vm).setReason("Starting machine");
        this.sendSucessMessage(he, request);
    }
}

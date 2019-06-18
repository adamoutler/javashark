/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author adamo
 *
 * curl -X PATCH -d
 * '{"message":{"command":mycars","data":{"gang":"bs2","username":"Baluhu""}}}'
 * https://javashark.baby-sharks.com/api/dev/command
 *
 */
public class MyCarsHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        if (vm.getCurrentTask()!=TaskType.WATCH){
            this.sendConflictReq(he, "User not on watch");
            return;
        }
        vm.getTaskData().setTelegramid(request.getData().getTelegramid());
        vm.setTemporaryTask(TaskType.MYCARS);
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        request.getData().setAvailable(vms);
        this.sendSucessMessage(he, request);
    }
}

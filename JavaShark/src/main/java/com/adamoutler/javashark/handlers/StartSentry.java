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
 * curl -X PATCH -d '{"message":{"command":"sentrystart","data":{"gang":"development","username":"adamoutler"}}}' https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class StartSentry extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        long time = System.currentTimeMillis();
        ArrayList<VirtualBoxMachine> al = new ArrayList<>();
        al.add(vm);
        vm.clearTasks();
        TaskType[] tasks=new TaskType[]{TaskType.POWERON,TaskType.STARTCATS,TaskType.CORRECTIVEACTIONCK,TaskType.SENTRY};
        for (TaskType task:tasks){
            vm.addTask(task);
        }
        Message m = new Message().setCommand("sentrystart").setData(this.getRequest().getData().setAvailable(al).setReason("Starting machine"));
        this.sendSucessMessage(he, m);

    }

    private boolean isBooted(VirtualBoxMachine vm) throws IOException {
        ScreenData sd = vm.getScreen() ;
        if (sd.equals(sd.BOOTED)) {
            return true;
        }
        return false;
    }

   
}

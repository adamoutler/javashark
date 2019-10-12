/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskManager;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.Message;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 *
 * @author adamo
 */
abstract class SentryAbstractPapaHandler extends AbstractPapaHandler {

    String gang;
    String username;
    VirtualBoxMachine vm;

    @Override
    public abstract void handle(HttpExchange he) throws IOException;

    @Override
    public void setMessage(Message m) {
        this.setRequest(m);
        gang = m.getData().getGang();
        username = m.getData().getUsername().replace("@", "");
        VirtualBoxMachine myVm = new VirtualBox().getUserMachine(gang, username);
        vm = TaskManager.getTaskMachine(myVm);

    }

    void addTasks(TaskType... tasks) {
        if (tasks == null) {
            System.out.println("added null task to" +vm.getUsername());
            return;
        }
        for (TaskType task : tasks) {
            if (task != null) {
            System.out.println("added "+task+" task to" +vm.getUsername());

                vm.addTask(task);
            } else {
                System.out.println ("null task");
            }
            
        }
    }
}

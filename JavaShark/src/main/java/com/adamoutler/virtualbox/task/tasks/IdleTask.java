/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;

/**
 *
 * @author adamo
 */
public class IdleTask extends Task {

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        vm.setKillThread(false);
        vm.getTaskData().setIdleNow();
        ready(vm);
        complete(vm);
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {
        if (!vm.getGang().equals("development") && !vm.getGang().equals("staging") && vm.getTaskData().getIdleTime() + 300000 < System.currentTimeMillis()) {
            if (vm.isPoweredOn()) {
                vm.addTask(TaskType.POWEROFF);
            }
        }
    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }
}

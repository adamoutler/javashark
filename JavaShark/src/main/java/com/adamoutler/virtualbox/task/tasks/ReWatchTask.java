/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.IfaceTask;
import com.adamoutler.virtualbox.task.TaskType;

/**
 *
 * @author adamo
 */
public class ReWatchTask implements IfaceTask {
    public ReWatchTask() {
    }

    @Override
    public void init(VirtualBoxMachine vm) {
        vm.setTaskReady(true);
        vm.setTaskComplete(true);
        vm.addTask(TaskType.CLEAR);
        vm.addTask(TaskType.CORRECTIVEACTIONCK);
        vm.addTask(TaskType.WATCH);
    }

    @Override
    public void exec(VirtualBoxMachine vm) {
        vm.setTaskComplete(true);
    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }

}

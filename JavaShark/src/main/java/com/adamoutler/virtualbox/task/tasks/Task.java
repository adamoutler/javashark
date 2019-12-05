/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.Prop;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.IfaceTask;
import static com.adamoutler.virtualbox.task.TaskManager.getTaskMachine;

/**
 *
 * @author adamo
 */
public abstract class Task implements IfaceTask {

    final static public String WEBSERVEREXCELLOCATION = Prop.getProp("reports");
    final static public String WEBSERVERSCREENSHOTLOCATION = Prop.getProp("screenshots");

    abstract void initializeTask(VirtualBoxMachine vm);

    abstract void runTask(VirtualBoxMachine vm);

    @Override
    public void init(VirtualBoxMachine vm) {
        vm = getTaskMachine(vm);
        vm.setTaskInitialized(true);
        initializeTask(vm);
    }

    @Override
    public void exec(VirtualBoxMachine vm) {
        vm = getTaskMachine(vm);
        runTask(vm);
    }

    void ready(VirtualBoxMachine vm) {
        vm.setTaskReady(true);
    }

    void complete(VirtualBoxMachine vm) {
        vm.setTaskComplete(true);
    }

    void initialized(VirtualBoxMachine vm) {
        vm.setTaskInitialized(true);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.sentry_tools;

import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import java.util.ArrayList;

/**
 *
 * @author adamo
 */
public class SentryTools {

    final VirtualBoxMachine vm;

    public SentryTools(VirtualBoxMachine vm) {
        this.vm = vm;

    }

    public boolean isOnWatch() {
        return vm.getCurrentTask().equals(TaskType.WATCH);
    }

    public boolean isBusy() {
        try {
            TaskType current = vm.getCurrentTask();
            return !(current == TaskType.IDLE || current == TaskType.POWEROFF);
        } catch (Exception ex) {
            System.out.println("attempted SentryTools.isBusy but vm was not powered on.");
            return false;
        }
    }

    public VirtualBoxMachine getCurrent(TaskType task) {
        try {
            ArrayList<VirtualBoxMachine> vms = new VirtualBox().getGangMachines(vm.getGang());
            for (VirtualBoxMachine virtualMachine : vms) {
                if (virtualMachine.getCurrentTask() == task) {
                    return virtualMachine;
                }
            }
        } catch (Exception ex) {
            System.out.println("attempted SentryTools.getCurrent but an error occurred.");
        }
        return null;
    }

    public TaskType[] getProductiveTasks() {
        if (getCurrent(TaskType.WATCH) == null) {
            return new TaskType[]{TaskType.POWERON, TaskType.STARTCATS, TaskType.CORRECTIVEACTIONCK, TaskType.WATCH};
        } else if (getCurrent(TaskType.BATTLEFIELDSCAN) == null) {
            return new TaskType[]{TaskType.POWERON, TaskType.STARTCATS, TaskType.CORRECTIVEACTIONCK, TaskType.BATTLEFIELDSCAN};
        }
        return new TaskType[]{TaskType.POWEROFF};

    }

}

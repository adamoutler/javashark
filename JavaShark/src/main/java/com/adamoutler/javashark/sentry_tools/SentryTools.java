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
    final String gang;
    final String username;

    public SentryTools(VirtualBoxMachine vm) {
        this.vm = vm;
        this.gang = vm.getGang();
        this.username = vm.getUsername();
    }

    public boolean isOnWatch() {
        return vm.getCurrentTask().equals(TaskType.WATCH);
    }

    public boolean isBusy() {
        TaskType current = vm.getCurrentTask();
        return !(current == TaskType.IDLE || current == TaskType.POWEROFF);
    }

    public VirtualBoxMachine getCurrent(TaskType task) {
        ArrayList<VirtualBoxMachine> vms = new VirtualBox().getGangMachines(gang);
        for (VirtualBoxMachine virtualMachine : vms) {
            if (virtualMachine.getCurrentTask() == task) {
                return virtualMachine;
            }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskManager;
import com.adamoutler.virtualbox.task.TaskType;
import com.adamoutler.virtualbox.task.tasks.sentry.FastBuildingOwnershipDetector;
import com.babysharks.messaging.SendPapaAsynch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class WatchTask extends Task {

    public WatchTask() {
        System.out.println("Sentry Watch Task initialized!");
    }

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        try {
            vm.getTaskData().setNullCount(0);
            FastBuildingOwnershipDetector fbod = new FastBuildingOwnershipDetector(vm);
            vm.getTaskData().previous = fbod.scan(vm.screenshot());
            ready(vm);
            turnOffOtherSentries(vm);
            SendPapaAsynch.sentryReasonWarning(vm, "monitoring");
        } catch (Exception ex) {
            Logger.getLogger(WatchTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void turnOffOtherSentries(VirtualBoxMachine vm) {
        // scan and turn off all sentries in the gang except ours
        for (VirtualBoxMachine testVm : TaskManager.getMachines()) {
            if (testVm.equals(vm)) {
                continue;
            }
            //if the machine is in the same gang turn it off
            if (testVm.getGang().equals(vm.getGang())
                    && testVm.getCurrentTask().equals(TaskType.WATCH)) {
                testVm.clearTasks();
                testVm.addTask(TaskType.POWEROFF);
            }
        }
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {
        new FastBuildingOwnershipDetector(vm).run();
    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        if (vm.isPoweredOn() && vm.getScreen().equals(ScreenData.CITYKINGS)) {
            System.out.println("putting " + vm.getUsername() + " back on sentry duty");
            vm.resumeTask(TaskType.WATCH);            
            return true;
        } else {
            return false;
        }
    }
}

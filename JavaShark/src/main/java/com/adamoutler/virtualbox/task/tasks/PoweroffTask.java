/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.PapaAsync;

/**
 *
 * @author adamo
 */
public class PoweroffTask extends Task {

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        vm.updateStatuses();
        if (vm.isPoweredOn()) {
            PapaAsync.sentryReasonWarning(vm, "offline");
        }
        new VirtualBoxControl(vm).stopVM();
        vm.setScreenID(ScreenData.OFF);
        ready(vm);
    }

    @Override
    public void runTask(VirtualBoxMachine vm) {
        complete(vm);
        vm.clearTasks();
    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }
}

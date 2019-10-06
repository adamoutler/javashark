/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.battlefield.BattlefieldNavigator;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.SendPapaAsynch;


/**
 * watches for enlist and reports to papa when done. if CK is in progress, it
 * will not report.
 *
 * @author adamo
 */
public class BeginTheAttackWatch extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        SendPapaAsynch.sentryReasonWarning(vm, "begin the attack monitor");
        ready(vm);
    }

    @Override
    void runTask(VirtualBoxMachine vm) {

        new BattlefieldNavigator().navigateToCK(vm);
        ScreenData sd = vm.getScreen();
        switch (sd) {
            case BEGINTHEATTACK:
                break;
            case CITYKINGSENLIST:
                SendPapaAsynch.sentryReasonWarning(vm, "enlist");
                complete(vm);
                return;
            case CITYKINGS:
                complete(vm);
                return;
            default:
                SendPapaAsynch.sentryReasonWarning(vm, "done watching");
                complete(vm);
        }

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        if (vm.isPoweredOn() && vm.getScreen().equals(ScreenData.BEGINTHEATTACK)) {
            System.out.println("putting " + vm.getUsername() + " back on sentry duty");
            vm.resumeTask(TaskType.BEGINTHEATTACK);
            return true;
        } else {
            return false;
        }
    }

}

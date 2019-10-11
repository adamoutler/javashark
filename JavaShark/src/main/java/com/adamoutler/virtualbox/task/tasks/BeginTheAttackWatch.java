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
import com.babysharks.messaging.Message;
import com.babysharks.messaging.PapaAsync;

/**
 * watches for enlist and reports to papa when done. if CK is in progress, it
 * will not report.
 *
 * @author adamo
 */
public class BeginTheAttackWatch extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        PapaAsync.sentryReasonWarning(vm, "begin the attack monitor");
        ready(vm);
    }

    @Override
    void runTask(VirtualBoxMachine vm) {

        new BattlefieldNavigator().navigateToCK(vm);
        ScreenData sd = vm.getScreen();
        switch (sd) {
            case CITYKINGSENLIST:
                Message m = new Message().setCommand("newround");
                m.getData().setGang(vm.getGang()).setUsername(vm.getUsername())
                        .setReason("City Kings enlist is showing!");
                PapaAsync.send(m);
                vm.addTask(TaskType.CLEAR);
                return;
            case CITYKINGSBEGINTHEATTACK:
                break;
            case CITYKINGS:
                complete(vm);
                return;
            case RECONNECT:
                vm.addTask(TaskType.CLEAR);
            default:
                Message n = new Message().setCommand("BeginTheAttackFailure");
                n.getData().setGang(vm.getGang()).setUsername(vm.getUsername())
                        .setReason("I couldn't recognize the screen while watching for Enlist Button.");
                PapaAsync.send(n);
                complete(vm);
        }

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        if (vm.isPoweredOn() && vm.getScreen().equals(ScreenData.CITYKINGSBEGINTHEATTACK)) {
            System.out.println("putting " + vm.getUsername() + " back on sentry duty");
            vm.resumeTask(TaskType.BEGINTHEATTACK);
            return true;
        } else {
            return false;
        }
    }

}

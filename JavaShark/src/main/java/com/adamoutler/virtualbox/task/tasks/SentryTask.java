/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.tasks.sentry.FastBuildingOwnershipDetector;
import com.babysharks.messaging.SendPapaAsynch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class SentryTask extends Task {

    public SentryTask() {
        System.out.println();
    }

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        try {
            FastBuildingOwnershipDetector fbod=new FastBuildingOwnershipDetector(vm);
            vm.getTaskData().previous = fbod.scan(vm.screenshot());
            ready(vm);
        } catch (Exception ex) {
            Logger.getLogger(SentryTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {
        new FastBuildingOwnershipDetector(vm).run();
    }

}

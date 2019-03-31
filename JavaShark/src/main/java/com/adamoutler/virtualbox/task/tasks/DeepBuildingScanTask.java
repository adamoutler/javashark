/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.citykings.hash_gathering.CityKingsMatchRaw;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.tasks.sentry.FastBuildingOwnershipDetector;
import com.babysharks.messaging.SendPapaAsynch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class DeepBuildingScanTask extends Task {

    public DeepBuildingScanTask() {
        System.out.println();
    }

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        try {
            Thread t=new Thread(()->{
            new CityKingsMatchRaw().scanBuildings(vm);
            });
            t.start();
            ready(vm);
        } catch (Exception ex) {
            Logger.getLogger(DeepBuildingScanTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {
        
    }

}

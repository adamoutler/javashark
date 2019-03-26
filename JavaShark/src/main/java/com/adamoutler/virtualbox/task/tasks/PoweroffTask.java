/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;

/**
 *
 * @author adamo
 */
public class PoweroffTask extends Task {

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        new VirtualBoxControl(vm).stopVM();
        ready(vm);
    }

    @Override
    public void runTask(VirtualBoxMachine vm) {
        complete(vm);
    }
    
}

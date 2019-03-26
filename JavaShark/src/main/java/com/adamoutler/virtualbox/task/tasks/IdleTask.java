/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.virtualbox.VirtualBoxMachine;

/**
 *
 * @author adamo
 */
public class IdleTask  extends Task{

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        ready(vm);
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {
        complete(vm);
    }
}

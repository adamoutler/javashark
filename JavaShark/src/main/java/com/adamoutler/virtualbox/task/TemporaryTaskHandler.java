/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.virtualbox.VirtualBoxMachine;

/**
 *
 * @author adamo
 */
class TemporaryTaskHandler {
    public static void doTemporaryTask(VirtualBoxMachine vm){
        vm.getTemporaryTask().getTask().init(vm);
        vm.getTemporaryTask().getTask().exec(vm);
        vm.setTemporaryTask(null);
        
    }
}

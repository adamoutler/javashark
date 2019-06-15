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
public class UpdateCatsTask extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        ready(vm);

            vm.adbShell(new String[]{"input keyevent KEYCODE_HOME; am start -a android.intent.action.VIEW -d 'market://details?id=com.zeptolab.cats.google'"});
            vm.getTaskData().setIdleNow();
    }

    @Override
    void runTask(VirtualBoxMachine vm) {
        if (System.currentTimeMillis()>vm.getTaskData().getIdleTime()+60000){
            vm.adbShell(new String[]{"input tap 730 262"});
            complete(vm);
            
        }
    }
    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }
}

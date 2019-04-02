/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.SendPapaAsynch;

/**
 *
 * @author adamo
 */
public class PowerOnTask extends Task {
    
    @Override
    void initializeTask(final VirtualBoxMachine vm) {
        final boolean power=vm.isPoweredOn();
        VirtualBoxControl vc = new VirtualBoxControl(vm);
        Thread startMachine = new Thread(() -> {
            String value = vm.adbShell(new String[]{"ls", "/"});
            if (value.equals("")) {
                vc.startVirtualMachine();
            }
            vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_HOME"});
            ready(vm);
            if (! power){
                SendPapaAsynch.onlineWarning(vm, false);
            }
        });
        startMachine.setName("Start VM " + vm.getGang() + " " + vm.getUsername());
        startMachine.start();
        
    }
    
    @Override
    void runTask(VirtualBoxMachine vm) {
        ScreenData sd = ScreenData.identifyScreen(vm);
        if (sd.equals(ScreenData.BOOTED)) {
            complete(vm);
        } else {
            vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_HOME"});
            vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_DPAD_RIGHT"});
        }
        
    }
}

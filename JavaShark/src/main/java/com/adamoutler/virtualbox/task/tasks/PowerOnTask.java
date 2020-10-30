/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.PapaAsync;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class PowerOnTask extends Task {

    @Override
    void initializeTask(final VirtualBoxMachine vm) {
        VirtualBoxControl vc = new VirtualBoxControl(vm);
        Thread startMachine = new Thread(() -> {
            String value = vm.adbShell(new String[]{"ls", "/"});
            if (value.equals("")) {
                vc.startVirtualMachine();
            }
            if (!vm.isPoweredOn()) {
                vc.stopVM();
                vc.StopVMSoft();
                vc.startVirtualMachine();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PowerOnTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!vm.isPoweredOn()){
                    PapaAsync.onlineWarning(vm, false);
                }
            }
            ready(vm);
            vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_HOME"});
        });
        startMachine.setName("Start VM " + vm.getGang() + " " + vm.getUsername());
        startMachine.start();

    }

    @Override
    void runTask(VirtualBoxMachine vm) {
        vm.getTaskData().poweroncounts++;
        if (vm.getTaskData().poweroncounts % 30 == 0 && !vm.isPoweredOn()) {
            VirtualBoxControl vc = new VirtualBoxControl(vm);
            vc.stopVM();
            vc.StopVMSoft();
            vc.startVirtualMachine();
            System.err.print("VM didn't start after 30 attempts. Retrying");
        }
        try {
            ScreenData sd = ScreenData.identifyScreen(vm);
            switch (sd) {
                case BOOTED:
                    complete(vm);
                    vm.getTaskData().poweroncounts = 0;
                    break;
                case NOTRESPONDING:
                    vm.longTap(ScreenData.NOTRESPONDING.getPicCoords()); //tap on the Wait option
                    break;
                case WAITUNRESPONSIVE:
                    vm.longTap(ScreenData.WAITUNRESPONSIVE.getPicCoords());
                    break;
                case UNRESPONSIVEBACKTODISMISS:
                    vm.adbShell("input", "keyevent", "KEYCODE_BACK");
                    break;
                default:
                    vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_HOME"});
                    vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_DPAD_RIGHT"});
                    break;
            }
        } catch (java.awt.image.RasterFormatException ex) {
            System.out.println("could not get image from " + vm.getUsername() + " likely still booting");
        }

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }
}

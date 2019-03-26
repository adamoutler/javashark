/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class PowerOnTask extends Task {

    @Override
    void initializeTask(final VirtualBoxMachine vm) {
        VirtualBoxControl vc = new VirtualBoxControl(vm);
        new Thread(() -> {
            String value = vm.adbShell(new String[]{"ls", "/"});
            if (value.equals("")) {
                vc.startVirtualMachine();
            }
            vm.adbShell(new String[]{"input","keyevent","KEYCODE_HOME"});
            ready(vm);
        }).start();


    }

    @Override
    void runTask(VirtualBoxMachine vm) {
        BufferedImage screenshot = vm.screenshot();
        ScreenData sd=ScreenData.identifyScreen(screenshot);
        if (sd.equals(ScreenData.BOOTED)){
            complete(vm);
        } else {
            vm.adbShell(new String[]{"input","keyevent","KEYCODE_HOME"});
        }

    }
}

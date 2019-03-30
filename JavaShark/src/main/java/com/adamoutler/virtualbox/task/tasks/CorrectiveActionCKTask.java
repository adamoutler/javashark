/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CorrectiveActionCKTask extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        ready(vm);
        vm.adbShell(new String[]{"input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; sleep 1; input tap 535 651; sleep 1; input tap 899 87; sleep 4; input tap 526 480; input tap 526 480; input tap 526 480; "});
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(CorrectiveActionCKTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    void runTask(VirtualBoxMachine vm) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CorrectiveActionCKTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!vm.getScreen().equals(ScreenData.CITYKINGS)) {
            vm.adbShell(new String[]{"input keyevent KEYCODE_BACK; sleep 1; input tap 899 87;input tap 899 87;"});
        } else {
            complete(vm);
        }

    }

}

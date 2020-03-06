/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenData;
import static com.adamoutler.cats.screen_id.ScreenData.UNRESPONSIVEBACKTODISMISS;
import static com.adamoutler.cats.screen_id.ScreenData.WAITUNRESPONSIVE;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
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
        vm.setKillThread(false);
        vm.adbShell(new String[]{"input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; sleep 1; input tap 535 651; sleep 1; input tap 899 87; sleep 4; input tap 526 480; input tap 526 480; input tap 526 480; "});
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(CorrectiveActionCKTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    void runTask(VirtualBoxMachine vm) {

        if (vm.getTaskData().lastCorrectiveAction>System.currentTimeMillis()-2000){
            return;
        }
        vm.getTaskData().lastCorrectiveAction=System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CorrectiveActionCKTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        ScreenData sd = vm.getScreen();
        
        if (!sd.equals(ScreenData.RECONNECT) && !sd.equals(ScreenData.CITYKINGS) && !sd.equals(ScreenData.CITYKINGSBEGINTHEATTACK) && !sd.equals(ScreenData.CITYKINGSENLIST)) {
            if (sd.equals(ScreenData.BOOTED)) {
                vm.setTemporaryTask(TaskType.STARTCATS);
            } else  if (sd.equals(WAITUNRESPONSIVE)) {
                vm.tap(WAITUNRESPONSIVE.getPicCoords());
            } 
            if (sd.equals(ScreenData.UNRESPONSIVEBACKTODISMISS)) {
                vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_BACK"});
            }
            if (sd.equals(ScreenData.RANDOMERROR)) {
                vm.tap(ScreenData.RANDOMERROR.getPicCoords());
            }

            vm.adbShell(new String[]{"input keyevent KEYCODE_BACK; sleep 1; input tap 899 87;input tap 899 87;"});
        } else {
            complete(vm);
        }

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.battlefield.BattlefieldNavigator;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class StartCatsTask extends Task {

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        vm.getTaskData().startCatsRetries = 0;
        ScreenData sd = vm.getScreen();
        if (!sd.equals(ScreenData.BOOTED) && !sd.equals(ScreenData.CATSLOADEDCHAMPIONSHIPSCREEN) && !sd.equals(ScreenData.CATSLOADEDULTIMATESCREEN) && !sd.equals(ScreenData.CITYKINGS)) {
            vm.setTaskInitialized(false);
            vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_HOME"});
            vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_DPAD_RIGHT"});
            return;
        }
        vm.adbShell(new String[]{"am", "start", "com.zeptolab.cats.google/com.zeptolab.cats.CATSActivity"});
        vm.getTaskData().setLastStartCats(System.currentTimeMillis());
        ready(vm);
    }

    @Override
    public void runTask(VirtualBoxMachine vm) {
        if (vm.getTaskData().getLastStartCats() < System.currentTimeMillis() + 100000) {
            vm.getTaskData().setLastStartCats(System.currentTimeMillis());
            ScreenData sd = vm.getScreen();
            switch (sd) {
                case CATSLOADEDCHAMPIONSHIPSCREEN:
                case CATSLOADEDULTIMATESCREEN:
                case CITYKINGS:
                case RECONNECT:
                    complete(vm);
                    break;
                case WHATCANYOUDOTODAY:
                    vm.tap(BattlefieldNavigator.BACKBUTTON);
                    vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_BACK"});
                    vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_BACK"});
                    break;
                case NOTRESPONDING:
                    vm.longTap(ScreenData.NOTRESPONDING.getPicCoords()); //tap on the Wait option
                    break;
                case WAITUNRESPONSIVE:
                    vm.longTap(ScreenData.WAITUNRESPONSIVE.getPicCoords());
                    break;

                case RANDOMERROR:
                    vm.longTap(ScreenData.RANDOMERROR.getPicCoords());
                    break;
                case UNRESPONSIVEBACKTODISMISS:
                    vm.adbShell("input", "keyevent", "KEYCODE_BACK");
                    break;
                default:
                    vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_BACK"});
                    vm.adbShell(new String[]{"input", "keyevent", "KEYCODE_ENTER"});
                    vm.adbShell(new String[]{"am", "start", "com.zeptolab.cats.google/com.zeptolab.cats.CATSActivity"});
                    if (++vm.getTaskData().startCatsRetries % 100 == 0) {
                        vm.writeBabySharksDebug(vm, "Tried starting CATS " + vm.getTaskData().startCatsRetries + " times. Retrying CATS restart");
                        vm.adbShell(new String[]{"am", "force-stop", "com.zeptolab.cats.google/com.zeptolab.cats.CATSActivity"});
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(StartCatsTask.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        initializeTask(vm);
                    }
                    if (vm.getTaskData().startCatsRetries == 201) {
                        vm.clearTasks();
                        vm.addTask(TaskType.POWEROFF);
                        vm.writeBabySharksDebug(vm, "Tried starting CATS " + vm.getTaskData().startCatsRetries + " times. Giving up.  Could not start!");

                    }
                    break;
            }
        }
    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }
}

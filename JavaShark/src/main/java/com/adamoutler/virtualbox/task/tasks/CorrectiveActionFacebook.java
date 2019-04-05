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
public class CorrectiveActionFacebook extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        ready(vm);
        vm.adbShell(new String[]{"input keyevent KEYCODE_BACK; sleep 5; input keyevent KEYCODE_BACK; sleep 5; input tap 535 651; sleep 5;input tap 967 42; sleep 10; input tap 344 394;sleep 10; input tap 332 286 "});
      
    }

    @Override
    void runTask(VirtualBoxMachine vm) {


            complete(vm);

    }

}

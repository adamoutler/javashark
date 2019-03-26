/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class StartCatsTask extends Task{

    @Override
    public void initializeTask(VirtualBoxMachine vm) {
        if (!vm.getScreen().equals(ScreenData.BOOTED)){
            vm.setTaskInitialized(false);
            vm.adbShell(new String[]{"input","keyevent","KEYCODE_HOME"});
            return;
        }
        vm.adbShell(new String[]{"am","start","com.zeptolab.cats.google/com.zeptolab.cats.CATSActivity"});
        ready(vm);
    }

    @Override
    public void runTask(VirtualBoxMachine vm) {

        ScreenData sd=ScreenData.identifyScreen(vm.screenshot());
        if (sd.equals(ScreenData.CATSLOADEDCHAMPIONSHIPSCREEN)||
                sd.equals(ScreenData.CATSLOADEDULTIMATESCREEN)||
                sd.equals(ScreenData.CITYKINGS)||
                sd.equals(ScreenData.RECONNECT)){
            complete(vm);
        } else {
            vm.adbShell(new String[]{"input","keyevent","KEYCODE_BACK"});
        }
    }
    
}

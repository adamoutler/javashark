/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.ErrorType;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.PapaAsync;
import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class RecordTrophiesTask  extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
       BufferedImage screenshot=vm.screenshot();
       if (!ScreenData.CITYKINGS.matchesHashes(screenshot)){
           this.initialized(vm);
           this.complete(vm);
           Message message=new Message();
           message.getData().setErrorType(ErrorType.WRONGSCREEN);
           message.setCommand("trophyrecord");
       }
    }

    @Override
    void runTask(VirtualBoxMachine vm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

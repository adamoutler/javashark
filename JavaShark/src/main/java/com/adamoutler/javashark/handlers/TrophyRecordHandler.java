/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.ErrorType;
import com.babysharks.messaging.PapaAsync;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class TrophyRecordHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        VirtualBoxMachine vm = getActiveSentry();
        if (checkActiveSentry(vm, he)) {
            return;
        }
        this.sendSucessMessage(he, request);
        vm.longTap(new XYCoords(500, 50));
        vm.longTap(new XYCoords(500, 50));
        vm.longTap(new XYCoords(500, 50));
        File f = getFileToWrite();
        vm.startRecording(f);
        doSwipes(vm);
        vm.stopRecording();
        vm.longTap(new XYCoords(50, 50));
        vm.longTap(new XYCoords(50, 50));
        vm.longTap(new XYCoords(50, 50));
        request.getData().setResultObject("https://javashark.baby-sharks.com/screenshots/" + f.getName());
        PapaAsync.send(request);

    }

    private void doSwipes(VirtualBoxMachine vm1) {
        try {
            XYCoords start = new XYCoords(600, 700);
            XYCoords end = new XYCoords(600, 200);
            int timeInMs = 4000;
            vm1.swipe(start, end, timeInMs);
            Thread.sleep(6000);
            vm1.swipe(start, end, timeInMs);
            Thread.sleep(6000);
            vm1.swipe(start, end, timeInMs);
            Thread.sleep(6000);

        } catch (InterruptedException ex) {
            Logger.getLogger(TrophyRecordHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File getFileToWrite() throws IOException {
        File f = File.createTempFile(username + "-" + gang + "-", ".webm", new File("/var/www/baby-sharks/screenshots/"));
        f.delete();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TrophyRecordHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f;
    }

    private VirtualBoxMachine getActiveSentry() {
        ArrayList<VirtualBoxMachine> gangVms = new VirtualBox().getGangMachines(this.gang);
        VirtualBoxMachine vm = null;
        for (VirtualBoxMachine thisvm : gangVms) {
            vm = thisvm;
            if (thisvm.isPoweredOn()
                    && ScreenData.CITYKINGS.matchesHashes(thisvm.screenshot())
                    && vm.getCurrentTask().equals(TaskType.WATCH)) {
                vm = thisvm;
            }
        }
        return vm;
    }

    private boolean checkActiveSentry(VirtualBoxMachine vm1, HttpExchange he) throws IOException {
        if (vm1 == null) {
            this.request.getData().setErrorType(ErrorType.NOSENTRIESACTIVE);
            this.sendSucessMessage(he, request);
            return true;
        }
        return false;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.battlefield.BattlefieldNavigator;
import com.adamoutler.cats.screen_id.ScreenData;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.OUTPUT;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.getOnlyStrings;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.PapaAsync;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * watches for enlist and reports to papa when done. if CK is in progress, it
 * will not report.
 *
 * @author adamo
 */
public class BeginTheAttackWatch extends Task {

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        PapaAsync.sentryReasonWarning(vm, "begin the attack monitor");
        ready(vm);
    }

    @Override
    void runTask(VirtualBoxMachine vm) {

        new BattlefieldNavigator().navigateToCK(vm);
        ScreenData sd = vm.getScreen();
        switch (sd) {
            case CITYKINGSENLIST:
                Message m = new Message().setCommand("newround");
                m.getData().setGang(vm.getGang()).setUsername(vm.getUsername())
                        .setReason("City Kings enlist is showing!");
                getScreenshot(m,vm);
                PapaAsync.send(m);
                vm.addTask(TaskType.CLEAR);
                break;
            case CITYKINGSBEGINTHEATTACK:
                break;
            case CITYKINGS:
                complete(vm);
                return;
            case RECONNECT:
                vm.addTask(TaskType.CLEAR);
            default:

                vm.writeBabySharksDebug(vm, "Attempting perform BEGINTHEATTACKWATCH but I couldn't recognize screen.");
                Message n = new Message().setCommand("BeginTheAttackFailure");
                n.getData().setGang(vm.getGang()).setUsername(vm.getUsername())
                        .setReason("I couldn't recognize the screen while watching for Enlist Button.");
                getScreenshot(n,vm);
                PapaAsync.send(n);
                complete(vm);
        }

    }

    public Message getScreenshot(Message m, VirtualBoxMachine vm) {
        try {
            String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + vm.getUsername() + vm.getGang().toLowerCase() + "-") + UUID.randomUUID().toString() + ".png";
            File outputFile = new File(OUTPUT + safeName);
            BufferedImage image = vm.screenshot();
            ScreenData sd = ScreenData.identifyScreen(image);
            ImageIO.write(image, "PNG", outputFile);
            m.getData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName).setResultObject(sd).setReason(vm.getUsername());
        } catch (IOException ex) {
            Logger.getLogger(BeginTheAttackWatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        if (vm.isPoweredOn() && vm.getScreen().equals(ScreenData.CITYKINGSBEGINTHEATTACK)) {
            System.out.println("putting " + vm.getUsername() + " back on sentry duty");
            vm.resumeTask(TaskType.BEGINTHEATTACK);
            return true;
        } else {
            return false;
        }
    }

}

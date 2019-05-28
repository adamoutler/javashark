/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.cats.screen_id.ScreenData;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.OUTPUT;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.getOnlyStrings;
import com.adamoutler.virtualbox.task.MyCars;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.SendPapaAsynch;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 * 
 * curl -X PATCH -d '{"message":{"command":mycars","data":{"gang":"bs2","username":"Baluhu""}}}' https://javashark.baby-sharks.com/api/dev/command
 * 
 */
public class MyCarsHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
           if (vm == null) {
            this.sendBadReq(he, "No sentry for user");
            return;
        }
        vm.setKillThread(true);
        boolean resumeWatch = vm.getCurrentTask() == TaskType.WATCH;
        if (!resumeWatch){
           this.sendBadReq(he, "Sentry is not on WATCH!");
            return; 
        }
        vm.clearTasks();
     
        

        String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + this.username + this.gang.toLowerCase() + "-") + UUID.randomUUID().toString() + ".png";
        if (vm.isPoweredOn()) {
            ScreenData currentScreen=ScreenData.identifyScreen(vm.screenshot());
            
            
            BufferedImage image = MyCars.getMyCars(vm);
            File outputFile = new File(OUTPUT + safeName);
            ScreenData sd = ScreenData.identifyScreen(image);
            ImageIO.write(image, "PNG", outputFile);
            request.getData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName).setResultObject(sd);
            sendSucessMessage(he, request);
            if (resumeWatch) {
                vm.addTask(TaskType.CORRECTIVEACTIONCK);
                vm.addTask(TaskType.WATCH);
            }
            return;

        }
        this.sendBadReq(he, "VM Off");
    }

}

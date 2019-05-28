/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.cats.battlefield.BattlefieldNavigator;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.ImageJoiner;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class MyCars {

    static XYCoords button1 = new XYCoords(65, 525);
    static XYCoords button2 = new XYCoords(135, 525);
    static XYCoords button3 = new XYCoords(215, 525);
    static XYCoords neutral = new XYCoords(150, 600);
    static PicCoords badgeLocation = new PicCoords(0, 192, 305, 384);

    public static BufferedImage getMyCars(VirtualBoxMachine vm) {
        new BattlefieldNavigator().navigateToCK(vm);
        BufferedImage car1 = tapGetPicture(button1, ScreenData.CITYKINGCAR1, vm);
        new BattlefieldNavigator().navigateToCK(vm);
        BufferedImage car2 = tapGetPicture(button2, ScreenData.CITYKINGCAR2, vm);
        new BattlefieldNavigator().navigateToCK(vm);
        BufferedImage car3 = tapGetPicture(button3, ScreenData.CITYKINGCAR3, vm);
        LinkedList<BufferedImage> images = new LinkedList();
        images.add(car1);
        images.add(car2);
        images.add(car3);
        return ImageJoiner.joinImagesSide(images);

    }

    private static BufferedImage tapGetPicture(XYCoords button, ScreenData screenData, VirtualBoxMachine vm) {
        vm.longTap(button);
        long endTime = System.currentTimeMillis() + 10000;
        BufferedImage screenshot = null;
        while (screenData.matchesHashes(screenshot) && System.currentTimeMillis() > endTime) {
            vm.tap(button);
            sleep(2000);
            screenshot = vm.screenshot();
        }
        if (System.currentTimeMillis()>endTime){
            return null;
        }
        return screenshot;
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyCars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

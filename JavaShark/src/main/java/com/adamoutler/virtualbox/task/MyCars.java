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
    static XYCoords expand = new XYCoords(24, 386);
    static XYCoords contract = new XYCoords(289, 402);

    static PicCoords badgeLocation = new PicCoords(0, 192, 305, 384);

    public static BufferedImage getMyCars(VirtualBoxMachine vm) {

        LinkedList<BufferedImage> images = new LinkedList();
        for (int i = 1; i < 4; i++) {
            getToScreen(vm);
            images.add(tapGetPicture(button1, ScreenData.valueOf("CITYKINGCAR" + i), vm));
        }
        contractScreen(vm);

        return ImageJoiner.joinImagesSide(images);

    }

    private static void contractScreen(VirtualBoxMachine vm) {
        ScreenData sd = vm.getScreen();

        while (sd == ScreenData.CITYKINGCAR1 || sd == ScreenData.CITYKINGCAR2 || sd == ScreenData.CITYKINGCAR3) {
            vm.tap(contract);
            sleep(2000);
            sd = vm.getScreen();
        }
    }

    static void getToScreen(VirtualBoxMachine vm) {

        ScreenData sd = vm.getScreen();
        while (sd != ScreenData.CITYKINGCAR1 && sd != ScreenData.CITYKINGCAR2 && sd != ScreenData.CITYKINGCAR3) {
            new BattlefieldNavigator().navigateToCK(vm);
            vm.tap(expand);
            sleep(2000);
            sd = vm.getScreen();
        }
    }

    private static BufferedImage tapGetPicture(XYCoords button, ScreenData screenData, VirtualBoxMachine vm) {
        vm.longTap(button);
        long endTime = System.currentTimeMillis() + 10000;
        BufferedImage screenshot = vm.screenshot();
        while (!screenData.matchesHashes(screenshot) && System.currentTimeMillis() < endTime) {
            vm.tap(button);
            sleep(2000);
            screenshot = vm.screenshot();
        }
        if (System.currentTimeMillis() > endTime) {
            return null;
        }
        return badgeLocation.getImageAtCoords(screenshot);
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyCars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

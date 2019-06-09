/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.battlefield.BattlefieldNavigator;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.ImageJoiner;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.OUTPUT;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.getOnlyStrings;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.MyCars;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.SendPapaAsynch;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class MyCarsTask extends Task {



    enum TapLocations {
        BUTTON1(new XYCoords(65, 525)),
        BUTTON2(new XYCoords(135, 525)),
        BUTTON3(new XYCoords(215, 525));
        XYCoords coords;

        TapLocations(XYCoords coords) {
            this.coords = coords;
        }

        XYCoords getCoords() {
            return coords;
        }
    }
    final static XYCoords NEUTRALTAPLOCATION = new XYCoords(150, 600);
    final static XYCoords EXPANDSIDEBARLOCATION = new XYCoords(24, 386);
    final static XYCoords CONTRACTSIDEBARLOCATION = new XYCoords(289, 402);
    final static PicCoords CARBADGELOCATION = new PicCoords(0, 192, 305, 384);

    @Override
    void initializeTask(VirtualBoxMachine vm) {
        vm.getTaskData().myCarsStage = 0;
        vm.getTaskData().retries = 0;
        vm.getTaskData().carsScreenshots = new LinkedList<>();
        vm.setTemporaryTaskInitialized(true);
    }

    @Override
    void runTask(VirtualBoxMachine vm) {
        BufferedImage screenshot = vm.screenshot();
        ScreenData sd = ScreenData.identifyScreen(screenshot);
        switch (vm.getTaskData().myCarsStage) {
            case 0: //navigate to city kings
                if (!sd.equals(ScreenData.CITYKINGS)) {
                    new BattlefieldNavigator().navigateToCK(vm);
                    screenshot = vm.screenshot();
                    sd = ScreenData.identifyScreen(screenshot);
                }
                if (sd == ScreenData.CITYKINGS) {
                    nextStage(vm);
                    vm.tap(EXPANDSIDEBARLOCATION);
                }
                break;
            case 1:
                vm.tap(EXPANDSIDEBARLOCATION);
            case 2: //verify car 1 and repeat this stage up to 3 times
                giveUpIfRetriesTooHigh(vm);
                if (vm.getTaskData().carsScreenshots.size()==1) {
                    vm.getTaskData().myCarsStage=3;
                }
                navigateToProperScreenOrIncreaseStage(sd, ScreenData.CITYKINGCAR1, vm, TapLocations.BUTTON1.getCoords());
                break;
            case 3: // verify car1 again. if not car 1, die.  else screenshot car 1, add to list. navigate to car 2. reset retry counter
                
                if (sd != ScreenData.CITYKINGCAR1) {
                    giveUp(vm);
                    break;
                }
                addVerfiedScreenshot(vm, screenshot,0);
                vm.tap(TapLocations.BUTTON2.getCoords());
                break;
            case 4: // verify car 2. repeat this staget up to 3 times.
                giveUpIfRetriesTooHigh(vm);
                navigateToProperScreenOrIncreaseStage(sd, ScreenData.CITYKINGCAR2, vm, TapLocations.BUTTON2.getCoords());
                break;
            case 5: // screenshot car 2. add to list. navigate to car 3. reset counter.
                if (sd != ScreenData.CITYKINGCAR2) {
                    giveUp(vm);
                    break;
                }
                addVerfiedScreenshot(vm, screenshot,1);
                vm.tap(TapLocations.BUTTON3.getCoords());
                break;
            case 6: // verify car 3. repeat this staget up to 3 times.
                giveUpIfRetriesTooHigh(vm);
                navigateToProperScreenOrIncreaseStage(sd, ScreenData.CITYKINGCAR3, vm, TapLocations.BUTTON3.getCoords());
                break;
            case 7: // screenshot car 3. add to list. navigate to car 3. reset counter.
                if (sd != ScreenData.CITYKINGCAR3) {
                    giveUp(vm);
                    break;
                }
                addVerfiedScreenshot(vm, screenshot,2);
                vm.tap(TapLocations.BUTTON3.getCoords());
                break;
            case 8: // build picture and send to papa on a new thread.
                writePic(vm);
                contractScreen(vm);
                break;
            case 9:
                if (!sd.equals(ScreenData.CITYKINGS)){
                    contractScreen(vm);
                } else {
                    nextStage(vm);
                }
                break;
            default: //complete the task and resume normal operations.
                vm.completeCurrentTask();

        }
    }

    private void navigateToProperScreenOrIncreaseStage(ScreenData currentScreen, ScreenData expectedScreen, VirtualBoxMachine vm, XYCoords buttonToPressIfNotOnExpectedScreen) {
        if (currentScreen != expectedScreen) {
            vm.tap(buttonToPressIfNotOnExpectedScreen);
            increaseRetries(vm);
        } else {
            nextStage(vm);
        }
    }

    private void nextStage(VirtualBoxMachine vm) {
        vm.getTaskData().myCarsStage++;
    }

    private void giveUp(VirtualBoxMachine vm) {
        vm.getTaskData().myCarsStage = 99;
    }

    private void increaseRetries(VirtualBoxMachine vm) {
        vm.getTaskData().retries++;
    }

    private void addVerfiedScreenshot(VirtualBoxMachine vm, BufferedImage screenshot,int expectedNumber) {
        if (vm.getTaskData().carsScreenshots.size() == expectedNumber) {
            vm.getTaskData().carsScreenshots.add(CARBADGELOCATION.getImageAtCoords(screenshot));
            vm.getTaskData().retries = 0;
            nextStage(vm);

        }
    }

    private void handleStage1(ScreenData sd, VirtualBoxMachine vm) {
        vm.tap(EXPANDSIDEBARLOCATION);
        if (sd == ScreenData.CITYKINGCAR1) {
            vm.getTaskData().myCarsStage = 2;
        } else {
            vm.tap(TapLocations.BUTTON1.getCoords());
            increaseRetries(vm);
        }
    }

    private void giveUpIfRetriesTooHigh(VirtualBoxMachine vm) {
        //open the panel
        if (vm.getTaskData().retries == 5) {
            giveUp(vm);
        }
    }

    private boolean handleFirstPicture(BufferedImage bi, VirtualBoxMachine vm) {

        ScreenData sd = ScreenData.identifyScreen(bi);
        if (sd != ScreenData.CITYKINGCAR1) {
            new BattlefieldNavigator().navigateToCK(vm);
            return false;
        }

        if (vm.getTaskData().myCarsStage == 3) {
            giveUp(vm);
            return true;
        }
        return false;
    }

    BufferedImage getFirstPicture(VirtualBoxMachine vm) {
        ScreenData sd = vm.getScreen();
        BufferedImage bi = null;
        int i = 0;
        new BattlefieldNavigator().navigateToCK(vm);
        vm.tap(EXPANDSIDEBARLOCATION);
        sleep(2000);
        bi = vm.screenshot();
        sd = ScreenData.identifyScreen(bi);
        return bi;
    }

    private void contractScreen(VirtualBoxMachine vm) {
        ScreenData sd = vm.getScreen();
        while (sd == ScreenData.CITYKINGCAR1 || sd == ScreenData.CITYKINGCAR2 || sd == ScreenData.CITYKINGCAR3) {
            vm.tap(CONTRACTSIDEBARLOCATION);
            sleep(2000);
            sd = vm.getScreen();
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyCars.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void writePic(VirtualBoxMachine vm) {
       BufferedImage output=ImageJoiner.joinImagesSide(vm.getTaskData().carsScreenshots);
       String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + vm.getUsername()+ vm.getGang().toLowerCase() + "-") + UUID.randomUUID().toString();
        try {
            ImageIO.write(output, "PNG", new File(OUTPUT + safeName + "-MYCARS.png"));
        } catch (IOException ex) {
            Logger.getLogger(MyCarsTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        MessageData md = new MessageData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName + "-battle.png").setComment("pics of my cars").setTelegramid(vm.getTaskData().getTelegramid()).setUsername(vm.getUsername()).setGang(vm.getGang());
        Message message = new Message().setCommand("mycars").setData(md);
        this.nextStage(vm);
        SendPapaAsynch.sendToPapa(message);
    }
    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }

}

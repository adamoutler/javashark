/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.NOBODY;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.THEM;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.US;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.WHITEBORDER;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.XYCoords;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.OUTPUT;
import static com.adamoutler.javashark.handlers.ScreenshotHandler.getOnlyStrings;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.PapaAsync;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author adamo
 */
public class FastBuildingOwnershipDetector {

    final static XYCoords BUILDING1 = new XYCoords(156, 380);
    final static XYCoords BUILDING2 = new XYCoords(90, 505);
    final static XYCoords BUILDING3 = new XYCoords(345, 595);
    final static XYCoords BUILDING4 = new XYCoords(620, 607);
    final static XYCoords BUILDING5 = new XYCoords(827, 512);
    final static XYCoords BUILDING6 = new XYCoords(798, 335);

    public enum BuildingBar {
        BAR1(new XYCoords(177, 380)),
        BAR2(new XYCoords(124, 530)),
        BAR3(new XYCoords(369, 620)),
        BAR4(new XYCoords(644, 630)),
        BAR5(new XYCoords(849, 536)),
        BAR6(new XYCoords(833, 360));
        XYCoords xy;

        BuildingBar(XYCoords xy) {
            this.xy = xy;
        }

        public XYCoords getXY() {
            return xy;
        }
    }

    String gang;
    String username;
    VirtualBoxMachine vm;

    public FastBuildingOwnershipDetector(VirtualBoxMachine vm) {
        this.gang = vm.getGang();
        this.username = vm.getUsername();
        this.vm = vm;
    }

    /**
     * takes a screenshot of city kings and scans buildings for changes
     *
     * @param screenshot ck screenshot
     * @return an OwnedBy array
     * @throws Exception
     */
    public OwnedBy[] scan(BufferedImage screenshot) throws Exception {
        PicColors pc = new PicColors(screenshot);
        OwnedBy[] buildings = new OwnedBy[6];
        buildings[0] = (this.checkBuilding(BUILDING1, pc));
        buildings[1] = (this.checkBuilding(BUILDING2, pc));
        buildings[2] = (this.checkBuilding(BUILDING3, pc));
        buildings[3] = (this.checkBuilding(BUILDING4, pc));
        buildings[4] = (this.checkBuilding(BUILDING5, pc));
        buildings[5] = (this.checkBuilding(BUILDING6, pc));
        return buildings;
    }

    /**
     * runs the FastBuildingOwnershipDetector. Sends notifications to Papa.
     *
     */
    public void run() {
        try {
            BufferedImage screenshot = vm.screenshot();
            vm.getTaskData().current = scan(screenshot);
            if (vm.getTaskData().previous == null) {
                vm.getTaskData().previous = new OwnedBy[]{THEM, THEM, THEM, THEM, THEM, THEM};
            }
            if (anyAreNull(vm.getTaskData().current, vm.getTaskData().previous)) {
                System.out.println(vm.getUsername() + ": Null detected!!!!!!");
                for (int i = 0; i < 6; i++) {
                    System.out.println("C: " + vm.getTaskData().current[i] + " P: " + vm.getTaskData().previous[i]);
                }
                if (vm.getTaskData().nullCount++ > 9) {
                    vm.setTaskComplete(true);
                    vm.updateStatuses();
                    BufferedImage lastScreenshot = vm.screenshot();
                    ScreenData sd = ScreenData.identifyScreen(lastScreenshot);
                    if (sd.equals(ScreenData.CITYKINGSVICTORY) || sd.equals(ScreenData.CITYKINGSDEFEAT)) {
                        sendFinalResults(vm, lastScreenshot);
                    }
                    PapaAsync.sentryReasonWarning(vm, "blind");

                }
                return;
            } else {
                vm.getTaskData().lastValidScreenshot = screenshot;
            }
            try {
                lostBuffDetection(vm, new PicColors(vm.getTaskData().lastValidScreenshot));
            } catch (NullPointerException ex) {

            }
            vm.getTaskData().setNullCount(0);

            reactToChangesInBuildingOwnership();

            vm.getTaskData().previous = vm.getTaskData().current;

        } catch (Exception ex) {
            Logger.getLogger(FastBuildingOwnershipDetector.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void sendFinalResults(VirtualBoxMachine vm, BufferedImage screenshot) throws IOException {

        //send final battlefield screenshot
        String safeName = getOnlyStrings(System.currentTimeMillis() + "-" + this.username + this.gang.toLowerCase() + "-") + UUID.randomUUID().toString();
        ImageIO.write(vm.getTaskData().lastValidScreenshot, "PNG", new File(OUTPUT + safeName + "-battle.png"));
        MessageData md = new MessageData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName + "-battle.png").setComment("screenshot of final battlefield").setUsername(vm.getUsername()).setGang(vm.getGang());
        Message message = new Message().setCommand("finalbattlefield").setData(md);
        PapaAsync.sendToPapa(message);

        //send final score screenshot;
        ImageIO.write(screenshot, "PNG", new File(OUTPUT + safeName + "-score.png"));
        message.setCommand("finalscore").getData().setUrl("https://javashark.baby-sharks.com/screenshots/" + safeName + "-score.png").setResultObject(ScreenData.identifyScreen(screenshot)).setComment("screenshot of final score");
        PapaAsync.sendToPapa(message);
    }

    private void reactToChangesInBuildingOwnership() throws RestClientException {
        System.out.println("Ownership/" + vm.getGang() + "/" + vm.getUsername() + ": " + new Date() + " " + Arrays.asList(vm.getTaskData().current));
        ArrayList<Integer> alertBuildings = getAlertBuildings(vm.getTaskData().current, vm.getTaskData().previous);
        maybeFreakOut(alertBuildings);
    }

    private void maybeFreakOut(ArrayList<Integer> alertBuildings) throws RestClientException {
        if (alertBuildings.size() > 0) {
            int[] alertBuildingsArray = alertBuildings.stream().mapToInt(i -> i).toArray();
            freakOut(alertBuildingsArray);
        }
    }

    ArrayList<Integer> getAlertBuildings(OwnedBy[] current, OwnedBy[] previous) {
        ArrayList<Integer> alertBuildings = new ArrayList<>();
        for (int i = 0; i < current.length; i++) {
            if (current[i] != previous[i] && previous[i].equals(OwnedBy.US)) {
                alertBuildings.add(i);

            }
        }
        return alertBuildings;
    }

    private static boolean anyAreNull(OwnedBy[] current, OwnedBy[] previous) {

        return current[0] == null || current[1] == null
                || current[2] == null || current[3] == null
                || current[4] == null || current[5] == null
                || previous[0] == null || previous[1] == null
                || previous[2] == null || previous[3] == null
                || previous[4] == null || previous[5] == null;
    }

    void freakOut(int[] buildings) throws RestClientException {
        MessageData data = configureData(buildings, vm);
        System.out.println("TIME TO FREAK OUT!!!!! OMFG");
        Message message = new Message();
        HashedMessage hm = new HashedMessage(message.setCommand("alert").setData(data));
        String s = PapaAsync.sendToPapa(hm);
        System.out.println(s);

    }

    MessageData configureData(int[] alertBuildings, VirtualBoxMachine vm) {
        MessageData data = new MessageData().setAlertBuilding(alertBuildings)
                .setText("Help").setReason("alert").setBuildings(new String[]{
            vm.getTaskData().current[0].getFriendlyname(),
            vm.getTaskData().current[1].getFriendlyname(),
            vm.getTaskData().current[2].getFriendlyname(),
            vm.getTaskData().current[3].getFriendlyname(),
            vm.getTaskData().current[4].getFriendlyname(),
            vm.getTaskData().current[5].getFriendlyname()}
        ).setGang(gang).setAvailable(new ArrayList<>(Arrays.asList(vm)))
                .setUsername(username);

        ArrayList<Integer> weOwn = new ArrayList<>();
        ArrayList<Integer> theyOwn = new ArrayList<>();
        ArrayList<Integer> nobodyOwns = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            switch (vm.getTaskData().current[i]) {
                case THEM:
                    theyOwn.add(i);
                    break;
                case US:
                    weOwn.add(i);
                    break;
                case NOBODY:
                    nobodyOwns.add(i);
                    break;
            }
        }
        data.setWeOwn(weOwn.stream().mapToInt(i -> i).toArray());
        data.setTheyOwn(theyOwn.stream().mapToInt(i -> i).toArray());
        data.setNobodyOwns(nobodyOwns.stream().mapToInt(i -> i).toArray());
        return data;
    }

    private OwnedBy checkBuilding(XYCoords coords, PicColors screenshot) throws Exception {
        return getOwnership(screenshot, coords);
    }

    private OwnedBy getOwnership(PicColors screenshot1, XYCoords coords) throws Exception {
        try {
            if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.US.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
                return OwnedBy.US;
            } else if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.THEM.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
                return OwnedBy.THEM;
            } else if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.NOBODY.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
                return OwnedBy.NOBODY;
            }
        } catch (NullPointerException ex) {
            System.out.println("Could not analyze screenshot");
        }

        return null;
    }

    public static float getPercentOurCars(BuildingBar bar, PicColors pc) throws NullPointerException {
        int initialXCoord = bar.getXY().getX();
        int yCoord = bar.getXY().getY();
        int startOfOurBar = pc.scanRightXCoordinateForColorAtYCoordinate(new int[]{OwnedBy.US.getColor()}, initialXCoord, yCoord, initialXCoord + 100);
        if (startOfOurBar == -1) {
            return 0;
        }
        int endOfOurBar = pc.scanRightXCoordinateForLackOfColorAtYCoordinate(new int[]{OwnedBy.US.getColor()}, startOfOurBar, yCoord, startOfOurBar + 200);
        int pixelsOurBar = endOfOurBar - startOfOurBar;
        int endOfBar = pc.scanRightXCoordinateForColorAtYCoordinate(new int[]{WHITEBORDER.getColor()}, endOfOurBar, yCoord, initialXCoord + 200);
        int sizeOfBar = endOfBar - startOfOurBar;
        if (sizeOfBar < 155) {
            throw new NullPointerException("Could not read bar size");
        }
        float percentOurs = ((float) pixelsOurBar / (float) sizeOfBar) * 100;

        return percentOurs;
    }

    public static void lostBuffDetection(VirtualBoxMachine vm, PicColors pc) {
        try {
            float bar1 = getPercentOurCars(BuildingBar.BAR1, pc);
            performBuffDetection(bar1, vm.getTaskData().building1PercentOurCars, 1);
            vm.getTaskData().building1PercentOurCars = bar1;
        } catch (Exception ex) {
        }
        try {
            float bar2 = getPercentOurCars(BuildingBar.BAR2, pc);
            performBuffDetection(bar2, vm.getTaskData().building2PercentOurCars, 2);
            vm.getTaskData().building2PercentOurCars = bar2;
        } catch (Exception ex) {
        }
        try {
            float bar3 = getPercentOurCars(BuildingBar.BAR3, pc);
            performBuffDetection(bar3, vm.getTaskData().building3PercentOurCars, 3);
            vm.getTaskData().building3PercentOurCars = bar3;
        } catch (Exception ex) {
        }
        try {
            float bar4 = getPercentOurCars(BuildingBar.BAR4, pc);
            performBuffDetection(bar4, vm.getTaskData().building4PercentOurCars, 4);
            vm.getTaskData().building4PercentOurCars = bar4;
        } catch (Exception ex) {
        }
        try {
            float bar5 = getPercentOurCars(BuildingBar.BAR5, pc);
            performBuffDetection(bar5, vm.getTaskData().building5PercentOurCars, 5);
            vm.getTaskData().building5PercentOurCars = bar5;
        } catch (Exception ex) {
        }
        try {
            float bar6 = getPercentOurCars(BuildingBar.BAR6, pc);
            performBuffDetection(bar6, vm.getTaskData().building6PercentOurCars, 6);
            vm.getTaskData().building6PercentOurCars = bar6;
        } catch (Exception ex) {
        }

    }

    public static void wipeBuildingData(VirtualBoxMachine vm) {
        vm.getTaskData().building1PercentOurCars = 0f;
        vm.getTaskData().building2PercentOurCars = 0f;
        vm.getTaskData().building3PercentOurCars = 0f;
        vm.getTaskData().building4PercentOurCars = 0f;
        vm.getTaskData().building5PercentOurCars = 0f;
        vm.getTaskData().building6PercentOurCars = 0f;

    }

    private static void performBuffDetection(float newValue, Float oldValue, int building) {
        if (oldValue == null || oldValue == 0 || oldValue == newValue) {
            //we cant work with this or nothing happened.
            //nothing to do just return
        } else if (newValue < oldValue && newValue > 50) { // we lost a buff
            Message m = new Message().setCommand("lostbuff");
            m.getData().setBuildings(new String[]{"" + building}).setReason(
                    "Building " + building + " lost a buff from " + oldValue
                    + "% to " + newValue + "%");
            String s = PapaAsync.sendToPapa(m);
        }
        /*
        else if (oldValue > 50 && newValue > oldValue) { //we buffed
            System.out.println("we just buffed building " + building);
        } else if (oldValue < 50 && newValue < oldValue) { //they buffed
            System.out.println("They just buffed building " + building);
        } else if (oldValue < 50 && newValue > 50) { //we took a building
            System.out.println("we took a building");
        }
         */
    }

}

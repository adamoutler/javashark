/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building;

import com.adamoutler.ADB;
import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CarFactory;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.Navigation;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageWriter;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public class BuildingFactory {

    final public static PicCoords BUILDING1NAME = new PicCoords(178, 345, 110, 25);
    final public static PicCoords BUILDING1SIZE = new PicCoords(314, 345, 36, 22);
    final public static PicCoords BUILDING2NAME = new PicCoords(122, 495, 108, 25);
    final public static PicCoords BUILDING2SIZE = new PicCoords(258, 495, 36, 22);
    final public static PicCoords BUILDING3NAME = new PicCoords(366, 584, 109, 25);
    final public static PicCoords BUILDING3SIZE = new PicCoords(504, 585, 36, 22);
    final public static PicCoords BUILDING4NAME = new PicCoords(641, 595, 110, 25);
    final public static PicCoords BUILDING4SIZE = new PicCoords(779, 595, 36, 22);
    final public static PicCoords BUILDING5NAME = new PicCoords(848, 500, 110, 25);
    final public static PicCoords BUILDING5SIZE = new PicCoords(984, 499, 36, 22);
    final public static PicCoords BUILDING6NAME = new PicCoords(832, 328, 110, 25);
    final public static PicCoords BUILDING6SIZE = new PicCoords(968, 329, 36, 22);
    final public static int RED = 0xFF615B;
    final public static int BLUE = 0xA9CAFA;
    final public static XYCoords friendlyFirstCar=new XYCoords(926, 271);
    final public static XYCoords foeFirstCar=new XYCoords(477, 271);
    private BufferedImage mapScreenshot;

    static private CityKingsBuildingRaw createBuilding(VirtualBoxMachine vm, BufferedImage screenshot, int buildingNumber, boolean friendly) {
        CityKingsBuildingRaw ckb = new CityKingsBuildingRaw(vm, buildingNumber, ByteImage.getPngBytes(screenshot));
        setbuildingNamePic(buildingNumber, screenshot, ckb);
        setBuildingSizePic(buildingNumber, screenshot, ckb);
        setBuildingXY(buildingNumber, ckb);
        ckb.setBuildingNumber(buildingNumber);

        BufferedImage ckscreen = null;
        try {
            for (int i = 0; i < 5; i++) {

                ckscreen = ckb.navigateToBuilding(friendly);
                ScreenData sd = vm.getScreen();
                if (sd.equals(ScreenData.CITYKINGSBUILDINGDEFEND) || sd.equals(ScreenData.CITYKINGSBUILDINGATTACK)) {
                    break;
                }
            }
            Screenshottable.writePic(ckscreen, "test");
            ckb.setIsLinked(isLinkedBuilding(ckb, ckscreen));
            ckb.setMapScreenshot(ckscreen);
            //required to set the size of the building before inspection. 
            ckb.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(vm.screenshot())));
            ensureTeamOwnershipPercentageBarIsSet(ckb, vm, friendly);
            for (int i = 0; i < 5; i++) {
                if (vm.getScreen().equals(ScreenData.CITYKINGSBUILDINGNOSCOUT)){
                    System.out.println("No scout is active! Skipping building");
                } else {
                    ckb.scan(friendly);
                }
                System.out.println("Checking final building navigational results.");

                sleep(2000);

                if (new PicColors(vm.screenshot()).checkColorPlusOrMinus(friendly?friendlyFirstCar:foeFirstCar, friendly?BLUE:RED, 10)) {//red ){
                    System.out.println("Cursor Position Verified. Report is solid.");
                    break;
                } else {
                    vm.adbShell("input", "tap", friendly?friendlyFirstCar.getXStr():foeFirstCar.getXStr(), friendly?friendlyFirstCar.getYStr():foeFirstCar.getYStr());
                    System.out.println("Could not verify all cars were accounted in building");
                    sleep(1000);
                }

            }
        } catch (IOException | InterruptedException | JadbException ex) {
            Logger.getLogger(BuildingFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ckb.writePics("Building" + buildingNumber);
//        try {
//           // CityKingsScreen.navigateTo(dev);
//        } catch (JadbException ex) {
//            Logger.getLogger(BuildingFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return ckb;
    }

    private static void sleep(int time) throws InterruptedException {
        Thread.sleep(time);
    }

    private static void ensureTeamOwnershipPercentageBarIsSet(CityKingsBuildingRaw ckb, VirtualBoxMachine vm, boolean friendly) throws IOException, InterruptedException, JadbException {
        int retries = 0;
        while (ckb.getOwnershipBar().getTotalSlots() < 5) {
            if (retries++ > 3) {
                throw new JadbException("Machine is not responding!  I couldn't acquire data from the building.");
            }
            ckb.navigateToBuilding(friendly);
            try {
                ckb.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(vm.screenshot())));
            } catch (NullPointerException ex) {
                System.out.println("Retrying to set TOSB");

            }
        }
    }

    static public LinkedList<CityKingsBuildingRaw> createBuildings(VirtualBoxMachine vm, boolean friendly) {
        LinkedList<CityKingsBuildingRaw> buildings = new LinkedList<>();

        for (int i = 1; i <= 6; i++) {
            CityKingsBuildingRaw building = BuildingFactory.createBuilding(vm, vm.screenshot(), i, friendly);

            buildings.add(building);

        }

        return buildings;
    }

    static private void setBuildingSizePic(int buildingNumber, BufferedImage pic, CityKingsBuildingRaw ckb) {
        try {
            PicCoords coords = getPicCoordinates(buildingNumber, "SIZE");
            BufferedImage sizePic = pic.getSubimage(coords.getX(), coords.getY(), coords.getW(), coords.getH());
            ckb.setBuildingSizePic(sizePic);
        } catch (SecurityException | IllegalArgumentException ex) {
            Logger.getLogger(BuildingFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static private void setbuildingNamePic(int buildingNumber, BufferedImage pic, CityKingsBuildingRaw ckb) {
        try {
            PicCoords coords = getPicCoordinates(buildingNumber, "NAME");
            BufferedImage namePic = pic.getSubimage(coords.getX(), coords.getY(), coords.getW(), coords.getH());
            ckb.setBuildingNamePic(namePic);
        } catch (SecurityException | IllegalArgumentException ex) {
            Logger.getLogger(BuildingFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static private PicCoords getPicCoordinates(int buildingNumber, String nameOrSize) {
        try {
            Field name = BuildingFactory.class.getDeclaredField("BUILDING" + buildingNumber + nameOrSize);
            name.setAccessible(true);
            PicCoords nameCoords = (PicCoords) name.get(null);
            return nameCoords;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(BuildingFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void setBuildingXY(int buildingNumber, CityKingsBuildingRaw ckb) {
        PicCoords coords = getPicCoordinates(buildingNumber, "NAME");
        ckb.setCityKingsBuildingScreenCoordinates(coords);
    }

    private static boolean isLinkedBuilding(CityKingsBuildingRaw ckb, BufferedImage newScreenshot) {
        PicCoords coords = ckb.getCityKingsBuildingScreenCoordinates();
        int startX = coords.getX();
        int y = coords.getY() + 7;
        int foundLoc = new PicColors(newScreenshot).scanRightXCoordinateForColorAtYCoordinate((int) 0xECAD21, startX, y, startX + 110, 10);
        return foundLoc > 0;
    }

    /**
     * @return the mapScreenshot
     */
    public BufferedImage getMapScreenshot() {
        return mapScreenshot;
    }

}

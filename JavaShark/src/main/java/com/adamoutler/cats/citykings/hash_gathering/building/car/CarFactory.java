/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building.car;

import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import static com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw.NEXTBUTTON;
import static com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw.PREVIOUSBUTTON;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.Navigation;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Swipe;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public class CarFactory {

    final public static XYCoords CAR1 = new XYCoords(820, 350);
    final public static XYCoords CAR2 = new XYCoords(820, 500);
    final public static XYCoords CAR3 = new XYCoords(820, 650);

    final public static XYCoords CAR4 = new XYCoords(630, 350);
    final public static XYCoords CAR5 = new XYCoords(630, 500);
    final public static XYCoords CAR6 = new XYCoords(630, 650);

    final public static XYCoords CAR7 = new XYCoords(430, 350);
    final public static XYCoords CAR8 = new XYCoords(430, 500);
    final public static XYCoords CAR9 = new XYCoords(430, 650);

    final public static Swipe SWIPE = new Swipe(375, 500, 940, 500, 5500);
    final public static Swipe BACKSWIPE = new Swipe(940, 433, 375, 433, 600);

    final public static XYCoords CAR10 = new XYCoords(880, 350);
    final public static XYCoords CAR11 = new XYCoords(880, 500);
    final public static XYCoords CAR12 = new XYCoords(880, 650);

    final public static XYCoords CAR13 = new XYCoords(660, 350);
    final public static XYCoords CAR14 = new XYCoords(660, 500);
    final public static XYCoords CAR15 = new XYCoords(660, 650);

    final public static XYCoords CAR16 = new XYCoords(440, 350);
    final public static XYCoords CAR17 = new XYCoords(440, 500);
    final public static XYCoords CAR18 = new XYCoords(440, 650);

    final public static XYCoords CAR19 = new XYCoords(460, 350);
    final public static XYCoords CAR20 = new XYCoords(460, 500);
    final public static XYCoords CAR21 = new XYCoords(460, 650);

    final public static XYCoords DISTINGUISHEDPIXEL1 = new XYCoords(90, 147);
    final public static XYCoords DISTINGUISHEDPIXEL2 = new XYCoords(93, 155);
    final public static XYCoords DISTINGUISHEDPIXEL3 = new XYCoords(104, 175);
    final public static XYCoords DISTINGUISHEDPIXEL4 = new XYCoords(96, 187);

    PicCoords carBadge = new PicCoords(60, 238, 310, 290);
    final static int[] SWIPEREQUIRED = new int[]{10, 18};

    private final CityKingsBuildingRaw building;
    private BufferedImage lastCarBadge;
    private VirtualBoxMachine vm;
    private static int sleepTime =500;
    private boolean friendly;
    
    public CarFactory(VirtualBoxMachine vm, CityKingsBuildingRaw building,boolean friendly) {
        this.vm = vm;
        this.building = building;
        this.friendly=friendly;
    }

    public LinkedList<CityKingsCarRaw> buildCars(final int buildingNumber) throws IOException, InterruptedException, JadbException {
        LinkedList<CityKingsCarRaw> buildingCars = new LinkedList<>();
        System.out.println("recorded car 1");
        CityKingsCarRaw lastCar = new CityKingsCarRaw("f");
        int redo = 0;
        for (int i = 1; i <= (friendly?building.getOwnershipBar().numberWeOwn():building.getOwnershipBar().numberTheyOwn()); i++) {
            //announce the car we are on
            System.out.println("Building:"+ buildingNumber+" Car:" + i);
            //sleep for a bit to verify no additional activity is required. 
            sleep(sleepTime);
            CityKingsCarRaw playerCar = createCarStore(buildingNumber, i);
            //we have a problem with the acquisition
            if (playerCar.equals(lastCar)) {

                redo++;
                sleep(sleepTime );
                System.out.println("correction level " + redo);
                switch (redo) {
                    case 1: //tap the screen and try again
                        vm.tap(new XYCoords(40, 40));
                        playerCar = createCarStore(buildingNumber, i);
                        break;
                    case 2: //tap the screen and wait, then try again
                        vm.longTap(new XYCoords(40, 40));
                        sleep(sleepTime );
                        playerCar = createCarStore(buildingNumber, i);
                        break;
                    case 3: //set previous position, then next position.
                        this.setPreviousCar();
                        sleep(sleepTime);
                        setNextCar();
                        sleep(sleepTime);
                        playerCar = createCarStore(buildingNumber, i);
                        if (this.isDuplicatedInList(buildingCars, playerCar)){
                            setNextCar();
                            sleep(sleepTime);
                            playerCar = createCarStore(buildingNumber, i);
                        }
                        break;
                    default: //tap forward and then tap on the screen
                        setNextCar();
                        vm.longTap(new XYCoords(40, 40));
                        sleep(sleepTime);
                        playerCar = createCarStore(buildingNumber, i);
                        break;

                }
                //if we corrected the problem
                if (!playerCar.equals(lastCar) && ! this.isDuplicatedInList(buildingCars, playerCar)) {
                    System.out.println("Correction Successful!");
                    redo = 0;
                    navigateToLocation(i);
                } 
                if (playerCar.isParked()){
                    redo=5;
                }
                
            } else {
                               
                redo = 0;
                navigateToLocation(i);
            }

            if (redo > 0) {
                System.out.println("Rescanning car.");
                i--;
                continue;
            } 
            
            lastCar = playerCar;

            //todo: check swipe here.  if this car's hashes match another car's hashes, we have not swiped.  We need to set location again.
            buildingCars.add(playerCar);
            //Screenshottable.writePic(playerCar.getCarOCRImage(), ""+i);

        }
        return buildingCars;
    }

    private void navigateToStartLocation() {
        vm.adbShell("input", "tap", CAR1.getXStr(), CAR1.getYStr());
    }

    private CityKingsCarRaw level1ErrorCorrection(int building, int location) throws IOException, InterruptedException {
        this.sleepTime = sleepTime + 50;
        System.out.println("Increased sleep time to " + sleepTime + "ms");
        sleep(sleepTime);
        return createCar(building, location);
    }

    private CityKingsCarRaw level2ErrorCorrection(int building, int location) throws IOException, InterruptedException {
        sleep(3000);
        return createCar(building, location);
    }

    /**
     * returns true if the car is different than previous cars.
     *
     * @param lastCar
     * @return
     */
    private CityKingsCarRaw level3ErrorCorrection(CityKingsCarRaw lastCar, int building, int location) throws IOException, InterruptedException {
        BufferedImage bi = getCarBadge();
        //check DISTINGUISHEDPIXEL1,DISTINGUISHEDPIXEL2,DISTINGUISHEDPIXEL3,DISTINGUISHEDPIXEL4
        //against last known good car pic
        PicColors current = new PicColors(bi);
        PicColors previous = new PicColors(this.lastCarBadge);
        if (current.getColor(DISTINGUISHEDPIXEL1) != previous.getColor(DISTINGUISHEDPIXEL1)
                && current.getColor(DISTINGUISHEDPIXEL2) != previous.getColor(DISTINGUISHEDPIXEL2)
                && current.getColor(DISTINGUISHEDPIXEL3) != previous.getColor(DISTINGUISHEDPIXEL3)
                && current.getColor(DISTINGUISHEDPIXEL4) != previous.getColor(DISTINGUISHEDPIXEL4)) {
            lastCar.setUniqueHash(Integer.toHexString(new Random().nextInt(Integer.MAX_VALUE) + 0x0));
            System.out.println("Level 3 sucess!");
        }
        return lastCar;
    }

    private CityKingsCarRaw level4ErrorCorrection(LinkedList<CityKingsCarRaw> buildingCars, int building, int location) throws IOException, InterruptedException {
        for (int i = 0; i <= this.building.getOwnershipBar().getNumberWeOwn(); i++) {
            this.setNextCar();
            Thread.sleep(this.sleepTime);
            CityKingsCarRaw playerCar = createCarStore(building, i);
            System.out.println("Adjusting to expected position + " + (i + 1));
            playerCar.setLocation(location);
            if (!isDuplicatedInList(buildingCars, playerCar)) {
                System.out.println("Level 4 success on attempt " + (i + 1));
                break;
            }

        }

        sleep(sleepTime);
        return createCar(building, location);
    }

    private boolean navigateToLocation(int carPosition) throws IOException, InterruptedException, JadbException {

        this.setNextCar();
        return true;
    }

    private BufferedImage getCarBadge() {
        return getSubImage(vm.screenshot(), this.carBadge);
    }

    private BufferedImage getSubImage(BufferedImage fullScreenshot, PicCoords p) {
        return fullScreenshot.getSubimage(p.getX(), p.getY(), p.getW(), p.getH());
    }

    private boolean isDuplicatedInList(LinkedList<CityKingsCarRaw> buildingCars, CityKingsCarRaw playerCar) {
        for (CityKingsCarRaw previousCar : buildingCars) {
            if (playerCar.equals(previousCar)) {
                return true;
            }
        }
        return false;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setOwnershipBar() throws IOException, InterruptedException {
        this.building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(vm.screenshot())));
    }

    private CityKingsCarRaw createCar(int buildingNumber, int i) throws InterruptedException, IOException {
        CityKingsCarRaw playerCar = new CityKingsCarRaw(ByteImage.getPngBytes(getSubImage(vm.screenshot(), carBadge)), buildingNumber, i);
        return playerCar;
    }

    private CityKingsCarRaw createCarStore(int buildingNumber, int i) throws InterruptedException, IOException {
        lastCarBadge = getSubImage(vm.screenshot(), carBadge);
        CityKingsCarRaw playerCar = new CityKingsCarRaw(ByteImage.getPngBytes(lastCarBadge), buildingNumber, i);
        return playerCar;
    }

    public void setPreviousCar() {

        try {
            Navigation.pressLocation(vm.getJadb(), (PREVIOUSBUTTON));
            Thread.sleep(500);
        } catch (IOException | JadbException ex) {
            Logger.getLogger(CityKingsCarRaw.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setNextCar() {

        try {
            Navigation.pressLocation(vm.getJadb(), (NEXTBUTTON));
            Thread.sleep(1000);
        } catch (IOException | JadbException ex) {
            Logger.getLogger(CityKingsCarRaw.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XYCoords getXY(int i) {
        XYCoords xy = null;
        try {
            Field f = CarFactory.class.getField("CAR" + i);
            xy = (XYCoords) f.get(null);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(CarFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xy;
    }

}

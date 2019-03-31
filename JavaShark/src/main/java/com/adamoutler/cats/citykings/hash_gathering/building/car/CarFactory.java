/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building.car;

import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import static com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw.NEXTBUTTON;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.Navigation;
import static com.adamoutler.imagerecognition.Navigation.sleep;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Swipe;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbDevice;
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

    PicCoords carPic = new PicCoords(60, 238, 310, 290);
    final static int[] SWIPEREQUIRED = new int[]{10, 18};

    private final CityKingsBuildingRaw building;
    private BufferedImage screenshot;
    private VirtualBoxMachine vm;

    public CarFactory(VirtualBoxMachine vm, CityKingsBuildingRaw building) {
        this.vm=vm;
        this.building = building;
    }

    private boolean navigateToLocation(int carPosition) throws IOException, InterruptedException, JadbException {
        if (carPosition > building.getOwnershipBar().numberWeOwn()) {
            return false;
        }

//        XYCoords xy = getXY(carPosition);
//        Navigation.tapLocation(vm.getJadb(), xy);
//        Thread.sleep(1200);
//        if (((carPosition) % 9) == 0 && carPosition != 1) {
//            Navigation.swipe(vm.getJadb(), SWIPE);
//            sleep(8000);
//        }
         this.setNextCar();
        return true;
    }

    private BufferedImage getSubImage(BufferedImage fullScreenshot, PicCoords p) {
        System.out.println(fullScreenshot.hashCode());
        return fullScreenshot.getSubimage(p.getX(), p.getY(), p.getW(), p.getH());
    }

    public LinkedList<CityKingsCarRaw> buildCars(int buildingNumber) throws IOException, InterruptedException, JadbException {
        LinkedList<CityKingsCarRaw> buildingCars = new LinkedList<>();
        
        for (int i = 1; i <= this.building.getOwnershipBar().numberWeOwn(); i++) {
            if (navigateToLocation(i)) {
                System.out.println("Checking for car #" + i);
                CityKingsCarRaw playerCar = createCar(buildingNumber, i);
                playerCar.setLocation(i);

                //correction algo to verify and ensure we hit every single car.
                for (CityKingsCarRaw previousCar : buildingCars) {
                    if (playerCar.equals(previousCar)) {
                        //problem was detected
                        boolean swiped = false;
                        System.out.println("Level 1 correction initiated. checking current screen.");
                        setOwnershipBar();
                        for (int correction = 1; correction <= this.building.getOwnershipBar().numberWeOwn(); correction++) {
                            System.out.println("Car check failed. Checking car " + correction);
                            navigateToLocation(correction);
                            CityKingsCarRaw correctedCar = createCar(buildingNumber, i);
                            boolean isThisUnique=true;
                            //we found a new car, so lets check that one
                            for (CityKingsCarRaw car:buildingCars){
                                if (car.equals(correctedCar)){
                                    isThisUnique=false;
                                    break;
                                }
                            }
                            if (isThisUnique) {
                                System.out.println("Correction sucessful!  Missing car located at " + correction);
                                correctedCar.setLocation(i);
                                playerCar=correctedCar;
                                break;
                            } else {
                                System.out.println("This car is not unique.");
                            }
                            if (!swiped && correction == 11) {
                                System.out.println("Level 2 correction initiated. Swiping back to start and searching for car again.");
                                Navigation.swipe(vm.getJadb(), BACKSWIPE);
                                correction = 1;
                                swiped = true;
                            }
                        }
                    }
                }

                //todo: check swipe here.  if this car's hashes match another car's hashes, we have not swiped.  We need to set location again.
                buildingCars.add(playerCar);
                //Screenshottable.writePic(playerCar.getCarOCRImage(), ""+i);

            }
        }
        return buildingCars;
    }

    private void setOwnershipBar() throws IOException, InterruptedException {
        this.building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(vm.screenshot())));
    }

    private CityKingsCarRaw createCar(int buildingNumber, int i) throws InterruptedException, IOException {
        CityKingsCarRaw playerCar = new CityKingsCarRaw( ByteImage.getPngBytes(getSubImage(vm.screenshot(), carPic)), buildingNumber, i);
        return playerCar;
    }
    public void setNextCar(){
        
        try {
            Navigation.tapLocation(vm.getJadb(), NEXTBUTTON);
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

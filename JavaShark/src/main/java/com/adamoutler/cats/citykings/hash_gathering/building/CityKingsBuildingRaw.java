/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building;

import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CarFactory;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.cats.citykings.hash_gathering.building.car.FastCarFactory;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.Navigation;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public final class CityKingsBuildingRaw {

    transient final public static XYCoords BUILDINGOWNERLOCATION = new XYCoords(507, 139);
    transient final public static XYCoords DEFENDBUTTON = new XYCoords(349, 390);
    transient final public static XYCoords ATTACKBUTTON = new XYCoords(670, 390);
    transient private byte[] buildingNamePic = new byte[0];
    transient private PicCoords cityKingsBuildingScreenCoordinates = new PicCoords(0, 0, 0, 0);
    private String buildingName = "";
    private int buildingNumber = 0;
    private OwnedBy buildingOwnedBy = OwnedBy.NOBODY;
    private int slotsTotal = 0;
    private int slotsTheirs = 0;
    private int slotsOurs = 0;
    private int slotsUnowned = 0;
    transient private byte[] buildingSizePic = new byte[0];
    private boolean isLinked = false;
    private LinkedList<CityKingsCarRaw> cars = new LinkedList<>();
    transient private TeamOwnershipPercentageBar tosb;
    transient VirtualBoxMachine vm;
    static boolean fastScan=false;
    private transient BufferedImage mapScreenshot;

    public CityKingsBuildingRaw(VirtualBoxMachine vm, int buildingNumber) {
        this.buildingNumber = buildingNumber;
        this.vm = vm;
    }

    public CityKingsBuildingRaw(VirtualBoxMachine vm, int buildingNumber, byte[] screenshot) {
        this.vm = vm;
        this.buildingNumber = buildingNumber;
        tosb = new TeamOwnershipPercentageBar(screenshot);
        gatherVariablesFromTosb(vm);
    }

    private void gatherVariablesFromTosb(VirtualBoxMachine vm) {
        setBuildingOwner(tosb.buildingOwnedBy());
        slotsTotal = tosb.getTotalSlots();
        slotsTheirs = tosb.numberTheyOwn();
        slotsOurs = tosb.numberWeOwn();
        slotsUnowned = tosb.getNumberNobodyOwn();
        System.out.println("Owner: " + this.getBuildingOwner("Us", "Them") + "slots- total:" + getSlotsTotal() + " us:" + getSlotsOurs() + " them:" + getSlotsTheirs() + " unowned:" + getSlotsUnowned());
    }

    /**
     * @return the buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String s) {
        buildingName = s;
    }

    /**
     * @return the cars
     */
    public LinkedList<CityKingsCarRaw> getCars() {
        return cars;
    }

    public void addCar(CityKingsCarRaw car) {
        cars.add(car);
    }

    /**
     * @return the buildingNamePic
     */
    public BufferedImage getBuildingNamePic() {
        return ByteImage.getImageFromBytes(buildingNamePic);
    }

    /**
     * @param buildingNamePic the buildingNamePic to set
     */
    public void setBuildingNamePic(BufferedImage buildingNamePic) {
        this.buildingNamePic = ByteImage.getPngBytes(buildingNamePic);
    }

    public void clearBuildingPics() {
        this.buildingSizePic = null;
        this.buildingNamePic = null;
       
    }

    /**
     * @return the buildingSizePic
     */
    public BufferedImage getBuildingSizePic() {
        return ByteImage.getImageFromBytes(buildingSizePic);
    }

    /**
     * @param buildingSizePic the buildingSizePic to set
     */
    public void setBuildingSizePic(BufferedImage buildingSizePic) {
        this.buildingSizePic = ByteImage.getPngBytes(buildingSizePic);
    }

    public BufferedImage navigateToBuilding(boolean friendly) throws IOException, JadbException {

        Navigation.navigateToCityKings(vm);
        sleep(4000);
        BufferedImage bi=vm.screenshot();
        Navigation.tapLocation(vm.getJadb(), this.getCityKingsBuildingScreenCoordinates());
        sleep(3000);
        if (friendly){
            Navigation.tapLocation(vm.getJadb(), DEFENDBUTTON);
        } else {
            Navigation.tapLocation(vm.getJadb(), ATTACKBUTTON);
        }
        sleep(3000);
        return bi;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(CityKingsBuildingRaw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = "\n";
        sb.append("Building Name: ").append(this.getBuildingName()).append(n);
        sb.append("Building Size: ").append(tosb.getTotalSlots()).append(n);
        sb.append("Their Spots: ").append(tosb.numberTheyOwn()).append(n);
        sb.append("Our Spots: ").append(tosb.numberWeOwn()).append(n);
        sb.append("Not Taken: ").append(tosb.numberUnowned()).append(n);
        sb.append("Owned By: ").append(tosb.buildingOwnedBy()).append(n);
        sb.append("X: ").append(this.cityKingsBuildingScreenCoordinates.getX())
                .append(" Y: ")
                .append(this.cityKingsBuildingScreenCoordinates.getY())
                .append(n);
        getCars().forEach((c) -> {
            sb.append(c).append(n);
        });

        return sb.toString().trim();
    }

    public String getBuildingOwner(String ourTeamName, String otherTeamName) {
        int color = new PicColors(vm.screenshot()).getColor(BUILDINGOWNERLOCATION);
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = color & 0x000000ff;
        if (red < 200 && green < 200 && blue < 200) {
            return "Nobody";
        } else if (red > 200) {
            return otherTeamName;
        } else {
            return ourTeamName;
        }

    }

    /**
     * @return the mainScreenCoordinates
     */
    public PicCoords getCityKingsBuildingScreenCoordinates() {
        return cityKingsBuildingScreenCoordinates;
    }

    /**
     * @param cityKingsBuildingScreenCoordinates
     */
    public void setCityKingsBuildingScreenCoordinates(PicCoords cityKingsBuildingScreenCoordinates) {
        this.cityKingsBuildingScreenCoordinates = cityKingsBuildingScreenCoordinates;
    }


    public void scan(boolean friendly) throws IOException, InterruptedException, JadbException {
        if (fastScan) {
            FastCarFactory bf = new FastCarFactory(vm, this, friendly);
            this.cars = bf.buildCars(this.getBuildingNumber(),friendly);
        } else {
            CarFactory bf = new CarFactory(vm, this,friendly);
            this.cars = bf.buildCars(this.getBuildingNumber());
        }
        // this.cars = removeDuplicates(cars);

    }

 

 
    void setBuildingOwner(OwnedBy buildingOwnedBy) {
        this.buildingOwnedBy = buildingOwnedBy;
    }

    /**
     * @return the buildingOwnedBy
     */
    public OwnedBy getBuildingOwnedBy() {
        return buildingOwnedBy;
    }

    /**
     * @return the tosb
     */
    public TeamOwnershipPercentageBar getOwnershipBar() {
        return tosb;
    }

    /**
     * sets the tosb
     *
     * @param tosb the bar to set
     */
    public void setOwnershipBar(TeamOwnershipPercentageBar tosb) {
        this.tosb = tosb;
    }

    /**
     * @param tosb the tosb to set
     */
    public void setTosb(TeamOwnershipPercentageBar tosb) {
        this.tosb = tosb;
        gatherVariablesFromTosb(vm);
    }

    /**
     * @return the buildingNumber
     */
    public int getBuildingNumber() {
        return buildingNumber;
    }

    /**
     * @param buildingNumber the buildingNumber to set
     */
    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    /**
     * @param isLinked the isLinked to set
     */
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    /**
     * @return the slotsTotal
     */
    public int getSlotsTotal() {
        return slotsTotal;
    }

    /**
     * @return the slotsTheirs
     */
    public int getSlotsTheirs() {
        return slotsTheirs;
    }

    /**
     * @return the slotsOurs
     */
    public int getSlotsOurs() {
        return slotsOurs;
    }

    /**
     * @return the slotsUnowned
     */
    public int getSlotsUnowned() {
        return slotsUnowned;
    }

    /**
     * @return the isLinked
     */
    public boolean isIsLinked() {
        return isLinked;
    }

    void setMapScreenshot(BufferedImage ckscreen) {
        mapScreenshot=ckscreen;
    }

    /**
     * @return the mapScreenshot
     */
    public BufferedImage getMapScreenshot() {
        return mapScreenshot;
    }

}

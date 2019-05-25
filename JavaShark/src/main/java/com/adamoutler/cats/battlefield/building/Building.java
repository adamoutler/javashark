/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building;

import com.adamoutler.cats.battlefield.building.car.Car;
import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class Building {

    transient private byte[] buildingNamePic = new byte[0];
    transient private PicCoords cityKingsBuildingScreenCoordinates = new PicCoords(0, 0, 0, 0);
    private String buildingName = "";
    private int buildingNumber = 0;
    private OwnedBy buildingOwnedBy = OwnedBy.NOBODY;
    private int slotsTotal = 0;
    private int slotsTheirs = 0;
    private int slotsOurs = 0;
    private int slotsUnowned = 0;
    private boolean friendlyScan;
    transient private byte[] buildingSizePic = new byte[0];
    private boolean isLinked = false;
    private LinkedList<Car> cars = new LinkedList<>();
    transient private TeamOwnershipPercentageBar tosb;
    private transient VirtualBoxMachine vm;
    private boolean suspectProblem = false;
    private String suspsectProblemReason = "";
    private transient BufferedImage mapScreenshot;

    /**
     * @return the buildingNamePic
     */
    public byte[] getBuildingNamePic() {
        return buildingNamePic;
    }

    /**
     * @param buildingNamePic the buildingNamePic to set
     */
    public void setBuildingNamePic(byte[] buildingNamePic) {
        this.buildingNamePic = buildingNamePic;
    }

    /**
     * @return the cityKingsBuildingScreenCoordinates
     */
    public PicCoords getCityKingsBuildingScreenCoordinates() {
        return cityKingsBuildingScreenCoordinates;
    }

    /**
     * @param cityKingsBuildingScreenCoordinates the
     * cityKingsBuildingScreenCoordinates to set
     */
    public void setCityKingsBuildingScreenCoordinates(PicCoords cityKingsBuildingScreenCoordinates) {
        this.cityKingsBuildingScreenCoordinates = cityKingsBuildingScreenCoordinates;
    }

    /**
     * @return the buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName the buildingName to set
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
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
     * @return the buildingOwnedBy
     */
    public OwnedBy getBuildingOwnedBy() {
        return buildingOwnedBy;
    }

    /**
     * @param buildingOwnedBy the buildingOwnedBy to set
     */
    public void setBuildingOwnedBy(OwnedBy buildingOwnedBy) {
        this.buildingOwnedBy = buildingOwnedBy;
    }

    /**
     * @return the slotsTotal
     */
    public int getSlotsTotal() {
        return slotsTotal;
    }

    /**
     * @param slotsTotal the slotsTotal to set
     */
    public void setSlotsTotal(int slotsTotal) {
        this.slotsTotal = slotsTotal;
    }

    /**
     * @return the slotsTheirs
     */
    public int getSlotsTheirs() {
        return slotsTheirs;
    }

    /**
     * @param slotsTheirs the slotsTheirs to set
     */
    public void setSlotsTheirs(int slotsTheirs) {
        this.slotsTheirs = slotsTheirs;
    }

    /**
     * @return the slotsOurs
     */
    public int getSlotsOurs() {
        return slotsOurs;
    }

    /**
     * @param slotsOurs the slotsOurs to set
     */
    public void setSlotsOurs(int slotsOurs) {
        this.slotsOurs = slotsOurs;
    }

    /**
     * @return the slotsUnowned
     */
    public int getSlotsUnowned() {
        return slotsUnowned;
    }

    /**
     * @param slotsUnowned the slotsUnowned to set
     */
    public void setSlotsUnowned(int slotsUnowned) {
        this.slotsUnowned = slotsUnowned;
    }

    /**
     * @return the buildingSizePic
     */
    public byte[] getBuildingSizePic() {
        return buildingSizePic;
    }

    /**
     * @param buildingSizePic the buildingSizePic to set
     */
    public void setBuildingSizePic(byte[] buildingSizePic) {
        this.buildingSizePic = buildingSizePic;
    }

    /**
     * @return the isLinked
     */
    public boolean isIsLinked() {
        return isLinked;
    }

    /**
     * @param isLinked the isLinked to set
     */
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    /**
     * @return the cars
     */
    public LinkedList<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(LinkedList<Car> cars) {
        this.cars = cars;
    }

    /**
     * @return the tosb
     */
    public TeamOwnershipPercentageBar getTosb() {
        return tosb;
    }

    /**
     * @param tosb the tosb to set
     */
    public void setTosb(TeamOwnershipPercentageBar tosb) {
        this.tosb = tosb;
        this.slotsOurs = tosb.getNumberWeOwn();
        this.slotsTheirs = tosb.getNumberTheyOwn();
        this.slotsUnowned = tosb.getNumberNobodyOwn();
        this.slotsTotal = tosb.getTotalSlots();
        this.buildingOwnedBy = tosb.buildingOwnedBy();
    }

    /**
     * @return the vm
     */
    public VirtualBoxMachine getVm() {
        return vm;
    }

    /**
     * @param vm the vm to set
     */
    public void setVm(VirtualBoxMachine vm) {
        this.vm = vm;
    }

    /**
     * @return the suspectProblem
     */
    public boolean isSuspectProblem() {
        return suspectProblem;
    }

    /**
     * @param suspectProblem the suspectProblem to set
     */
    public void setSuspectProblem(boolean suspectProblem) {
        this.suspectProblem = suspectProblem;
    }

    /**
     * @return the suspsectProblemReason
     */
    public String getSuspsectProblemReason() {
        return suspsectProblemReason;
    }

    /**
     * @param suspsectProblemReason the suspsectProblemReason to set
     */
    public void setSuspsectProblemReason(String suspsectProblemReason) {
        System.out.println(vm.getUsername() + "- SCAN ISSUE: " + suspsectProblemReason);
        this.suspectProblem = true;
        this.suspsectProblemReason = this.suspsectProblemReason + "... " + suspsectProblemReason;
    }

    /**
     * @return the mapScreenshot
     */
    public BufferedImage getMapScreenshot() {
        return mapScreenshot;
    }

    /**
     * @param mapScreenshot the mapScreenshot to set
     */
    public void setMapScreenshot(BufferedImage mapScreenshot) {
        this.mapScreenshot = mapScreenshot;
    }

    public void clearBuildingPics() {
        setBuildingNamePic(null);
        setBuildingSizePic(null);
        for (Car car : this.getCars()) {
            car.clearPics();
        }
    }

    /**
     * @return the friendlyScan
     */
    public boolean isFriendlyScan() {
        return friendlyScan;
    }

    /**
     * @param friendlyScan the friendlyScan to set
     */
    public void setFriendlyScan(boolean friendlyScan) {
        this.friendlyScan = friendlyScan;
    }

    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Building ").append(this.buildingNumber).append(" owned by").append(this.buildingOwnedBy);
        return sb.toString();
    }
    
}

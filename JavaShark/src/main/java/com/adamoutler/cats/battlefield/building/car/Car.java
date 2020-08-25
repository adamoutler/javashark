/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building.car;

import com.adamoutler.cats.battlefield.building.Locations;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CarBadgeHash;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.HashedPicCoords;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class Car {

    transient public static PicCoords friendlyCarBadgeLocation = new PicCoords(60, 238, 310, 290);
    transient public static PicCoords foeCarBadgeLocation = new PicCoords(60, 238, 310, 282);
    transient public static XYCoords diagonalScanStartPosition = new XYCoords(45, 120);

    private static transient HashedPicCoords[] parkedCarIndicator = new HashedPicCoords[]{new HashedPicCoords(143, 14, 24, 25, -1260088858)};
    private String nameHash = "";
    private String attackHash = "";
    private int location = 0;
    private int building;
    private byte[] carBadge = new byte[]{};
    private boolean parked = false;
    private boolean unscoutted = false;
    private String tieBreaker = "";

    Car() {
    }

    public Car(BufferedImage bi, boolean friendly) {
        this.setCarScreenshot(bi, friendly);
    }

    /**
     * @return the parkedCarIndicator
     */
    public static HashedPicCoords[] getParkedCarIndicator() {
        return parkedCarIndicator;
    }

    /**
     * @param aParkedCarIndicator the parkedCarIndicator to set
     */
    public static void setParkedCarIndicator(HashedPicCoords[] aParkedCarIndicator) {
        parkedCarIndicator = aParkedCarIndicator;
    }

    /**
     * @return the nameHash
     */
    public String getNameHash() {
        return nameHash;
    }

    /**
     * @param nameHash the nameHash to set
     */
    public void setNameHash(String nameHash) {
        this.nameHash = nameHash;
    }

    /**
     * @return the uniqueHash
     */
    public String getAttackHash() {
        return attackHash;
    }

    /**
     * @param uniqueHash the uniqueHash to set
     */
    public void setAttackHash(String uniqueHash) {
        this.attackHash = uniqueHash;
    }

    public String getCarNameHash() {
        return this.nameHash + this.attackHash + this.tieBreaker;
    }

    /**
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     * @return reference to this object
     */
    public Car setLocation(int location) {
        this.location = location;
        return this;
    }

    /**
     * @return the building
     */
    public int getBuilding() {
        return building;
    }

    /**
     * @param building the building to set
     * @return reference to this object
     */
    public Car setBuilding(int building) {
        this.building = building;
        return this;
    }

    /**
     * @return the carBadge
     */
    public byte[] getCarBadge() {
        return carBadge;
    }

    final public Car setCarScreenshot(BufferedImage bi, boolean friendly) {
        PicCoords badgeCoords = getBadgeCoords(friendly);

        if (ScreenData.CITYKINGSBUILDINGNOSCOUT.matchesHashes(bi)) {
            unscoutted = true;
            System.out.println("Parked Car detected");
        }
        System.out.print("ParkedCarCoordinatesHash: " + ScreenData.PARKEDCAR.getHashes().get(0).getImageHash(bi));
        if (ScreenData.PARKEDCAR.matchesHashes(bi)) {
            parked = true;
        }

        BufferedImage badge = badgeCoords.getImageAtCoords(bi);
        setParked(false);

        this.setNameHash(Integer.toHexString(CarBadgeHash.getNameHash(badge)));
        this.setAttackHash(Integer.toHexString(CarBadgeHash.getAttackHash(badge)));
        this.carBadge = ByteImage.getPngBytes(badge);
        if (this.nameHash.equals("591f2758")) {
            this.parked = true;
        }
        this.tieBreaker = getTieBreakerValueFromImage(badge);

        System.out.println("Car " + this.location + " name:" + this.nameHash + " attack:" + this.attackHash + " Tiebreaker: " + this.tieBreaker);
        return this;
    }

    private PicCoords getBadgeCoords(boolean friendly) {
        PicCoords badgeCoords;
        if (friendly) {
            badgeCoords = friendlyCarBadgeLocation;
        } else {
            badgeCoords = foeCarBadgeLocation;
        }
        return badgeCoords;
    }

    /**
     * @return the parked
     */
    public boolean isParked() {
        return parked;
    }

    /**
     * @param parked the parked to set
     */
    public void setParked(boolean parked) {
        this.parked = parked;
    }

    /**
     * sets the picture of the car in bytes
     *
     * @param bytes bytes of the picture
     * @return reference to this object
     */
    public Car setCarBadge(byte[] bytes) {
        this.carBadge = bytes;
        return this;
    }

    public void convertCarPicToJpg() {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.getCarBadge());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage image = ImageIO.read(bais);
            ImageIO.write(image, "jpg", baos);
            setCarBadge(baos.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] getCarJpg() {
        return ByteImage.getJpgBytes(getCarBadge());
    }

    public BufferedImage getCarAsJpg() {
        return ByteImage.getImageFromBytes(ByteImage.getJpgBytes(getCarBadge()));
    }

    public int getRow() {
        int offset = 1;
        return ((location - offset) % 3) + offset;
    }

    public int getColumn() {
        int offset = 1;
        return Math.floorDiv(location - offset, 3) + offset;
    }

    /**
     * returns true if hashcode matches
     *
     * @param o object to check
     * @return true if match hashcode
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(this.getClass())) {
            Car car = (Car) o;
            if (car.hashCode() == this.hashCode()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSameImage(Car c) {
        return tieBreaker(c);
    }
    
    private boolean tieBreaker(Car car){
        TieBreaker tb1=new TieBreaker(car);
        TieBreaker tb2=new TieBreaker(this);
        return tb1.equals(tb2);
    }

    /**
     * get a hashcode of the namehash and the carhash.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.nameHash);
        hash = 97 * hash + Objects.hashCode(this.attackHash);
        return hash;
    }

    @Override
    public String toString() {
        return "Location:" + this.location + " user:" + this.getNameHash() + " unique:" + this.getAttackHash();

    }

    public void clearPics() {
        this.carBadge = null;
    }

    /**
     * @return the unscoutted
     */
    public boolean isUnscoutted() {
        return unscoutted;
    }

    private String getTieBreakerValueFromImage(BufferedImage badge) {
        PicColors pc = new PicColors(badge);
        XYCoords xy = pc.scanUpRightForLackOfColor(new int[]{Locations.BLUE}, diagonalScanStartPosition.getX(), diagonalScanStartPosition.getY(), 30);
        int color = pc.getColor(xy);
        return xy.getXStr() + "/" + xy.getYStr() + "/" + color;
    }

    /**
     * @return the tieBreaker
     */
    public String getTieBreaker() {
        return tieBreaker;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building.car;

import com.adamoutler.cats.citykings.hash_gathering.building.car.CarBadgeHash;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.HashedPicCoords;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.Font;
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

   transient PicCoords carBadgeLocation = new PicCoords(60, 238, 310, 290);
    private static transient HashedPicCoords parkedCarIndicator = new HashedPicCoords(206, 252, 19, 22, 34185158);
    private String nameHash = "";
    private String uniqueHash = "";
    private int location = 0;
    private int building;
    private byte[] carBadge = new byte[]{};
    private boolean parked = false;

    Car() {
    }

    /**
     * @return the parkedCarIndicator
     */
    public static HashedPicCoords getParkedCarIndicator() {
        return parkedCarIndicator;
    }

    /**
     * @param aParkedCarIndicator the parkedCarIndicator to set
     */
    public static void setParkedCarIndicator(HashedPicCoords aParkedCarIndicator) {
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
    public String getUniqueHash() {
        return uniqueHash;
    }

    /**
     * @param uniqueHash the uniqueHash to set
     */
    public void setUniqueHash(String uniqueHash) {
        this.uniqueHash = uniqueHash;
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

    public Car setCarScreenshot(BufferedImage bi) {
        BufferedImage badge = carBadgeLocation.getImageAtCoords(bi);
        this.setParked(parkedCarIndicator.hashMatches(badge));
        this.setNameHash(Integer.toHexString(CarBadgeHash.getNameHash(badge)));
        this.setUniqueHash(Integer.toHexString(CarBadgeHash.getUniqueHash(badge)));
        this.carBadge = ByteImage.getPngBytes(badge);
        return this;
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

    /**
     * get a hashcode of the namehash and the carhash.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.nameHash);
        hash = 97 * hash + Objects.hashCode(this.uniqueHash);
        return hash;
    }

    @Override
    public String toString() {
        return "Location:" + this.location + " user:" + this.getNameHash() + " unique:" + this.getUniqueHash();

    }

    public void clearPics() {
        this.carBadge = null;
    }

}

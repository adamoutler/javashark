/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building.car;

import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.HashedPicCoords;
import com.adamoutler.imagerecognition.PicColors;
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
public final class CityKingsCarRaw implements Cloneable {

    transient public final static XYCoords NEXTBUTTON = new XYCoords(302, 576);
    transient public final static XYCoords PREVIOUSBUTTON = new XYCoords(132, 576);
    transient public final static PicCoords NAMECOORDS = new PicCoords(6, 6, 300, 38);
    transient public final static PicCoords UNIQUECOORDS = new PicCoords(6, 230, 300, 24);
    transient final private static int SWORDCOLOR = 0x48a1b8;
    transient private final static PicCoords SMALL = new PicCoords(0, 0, 100, 300);
    transient final static Font FONT = new Font("Arial", Font.PLAIN, 35);
    transient final static HashedPicCoords parkedCarIndicator = new HashedPicCoords(206, 252, 19, 22, 34185158);
    private String nameHash = "";
    private String uniqueHash = "";
    private String bottomLineHash = "";
    private int location = 0;
    private Integer row = 0;
    private Integer column = 0;
    private int building;
    private byte[] carBadge = new byte[]{};
    private boolean parked = false;

    public CityKingsCarRaw(byte[] screenshot, int building, int location) {
        this.building = building;
        this.location = location;
        setCarScreenshot(screenshot);
        setLocation(location);

    }

    public CityKingsCarRaw(String test) {
        this.building = 0;
        this.location = 0;
        this.uniqueHash = "ffff";
        this.carBadge = new byte[]{33};
        this.uniqueHash = "fffff";
    }

    /**
     * @return the namePic
     */
    public BufferedImage getNamePic() {
        return getSubImage(ByteImage.getImageFromBytes(getCarBadge()), NAMECOORDS);
    }

    /**
     *
     * @param carScreenshot
     * @return
     */
    private CityKingsCarRaw setCarScreenshot(byte[] carScreenshot) {
        this.setCarBadge(carScreenshot);
        BufferedImage bi = ByteImage.getImageFromBytes(carScreenshot);
        Screenshottable.writePic(bi, "CarInBuilding" + this.getBuilding() + "Slot" + this.location);

        return this;
    }

    public void clearCarScreenshot() {
        this.setCarBadge(null);
    }

    /**
     * @return the uniquePic
     */
    public BufferedImage getUniquePic() {
        BufferedImage bi = ByteImage.getImageFromBytes(getCarBadge());
        PicColors pc = new PicColors(bi);
        int startX = pc.scanRightXCoordinateForColorAtYCoordinate(SWORDCOLOR, 150, 253, 300);
        if (startX == -1) {
            return null;
        }
        PicCoords attackImage = new PicCoords(startX, UNIQUECOORDS.getY(), bi.getWidth() - startX, UNIQUECOORDS.getH());
        BufferedImage retval = getSubImage(bi, attackImage);
        Screenshottable.writePic(retval, "testing image generation");

        return retval;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = "\n";
        sb.append("R").append(this.row).append("C").append(this.column).append(" slot ").append(this.location).append(n);
        sb.append("Name: ").append(" - namehash: ").append(getNameHash()).append(" - carhash: ").append(getUniqueHash()).append(n);
        return sb.toString();
    }

    /**
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(int location) {
        int offset = 1;
        column = Math.floorDiv(location - offset, 3) + offset;
        row = ((location - offset) % 3) + offset;
        this.location = location;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    public byte[] getCarJpg() {
        return ByteImage.getJpgBytes(getCarBadge());
    }

    /**
     * @return the carScreenshot
     */
    public BufferedImage getCarScreenshot() {
        return ByteImage.getImageFromBytes(getCarBadge());
    }

    /**
     * @param carScreenshot the carScreenshot to set
     */
    public void setCarScreenshot(BufferedImage carScreenshot) {
        this.setCarScreenshot(ByteImage.getPngBytes(carScreenshot));
    }

    private BufferedImage getSubImage(BufferedImage fullScreenshot, PicCoords p) {
        return fullScreenshot.getSubimage(p.getX(), p.getY(), p.getW(), p.getH());
    }

    /**
     *
     * @return @throws CloneNotSupportedException
     */
    @Override
    public CityKingsCarRaw clone() throws CloneNotSupportedException {
        super.clone();
        CityKingsCarRaw car = new CityKingsCarRaw(this.getCarBadge(), this.getBuilding(), location);
        return car;
    }

    /**
     * @return the nameHash
     */
    public String getNameHash() {
        return nameHash;
    }

    /**
     * @param nameHash the nameHash to set
     * @return ref to this
     */
    public CityKingsCarRaw setNameHash(String nameHash) {
        this.nameHash = nameHash;
        return this;
    }

    /**
     * @return the carHash
     */
    public String getUniqueHash() {
        return uniqueHash;
    }

    /**
     * @param carHash the carHash to set
     * @return ref to this
     */
    public CityKingsCarRaw setUniqueHash(String carHash) {
        this.uniqueHash = carHash;
        return this;
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
            CityKingsCarRaw car = (CityKingsCarRaw) o;
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

    public void convertCarPicToJpg() {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.getCarBadge());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage image = ImageIO.read(bais);
            ImageIO.write(image, "jpg", baos);
            setCarBadge(baos.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(CityKingsCarRaw.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public CityKingsCarRaw setBuilding(int building) {
        this.building = building;
        return this;
    }

    /**
     * @return the carBadge
     */
    public byte[] getCarBadge() {
        return carBadge;
    }

    /**
     * @param carBadge the carBadge to set
     * @return reference to this object
     */
    public CityKingsCarRaw setCarBadge(byte[] carBadge) {
        this.carBadge = carBadge;
        return this;
    }

    public void clearRowAndColumn() {
        this.row = null;
        this.column = null;
        this.bottomLineHash = null;
        this.uniqueHash = null;

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
}

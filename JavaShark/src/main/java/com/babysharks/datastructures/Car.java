/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures;

import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.imagerecognition.ByteImage;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adamo
 */
public class Car {

    private List<String> name_hash=new ArrayList<>();
    private String unique_hash;
    private int location;
    private int row;
    private int columnn;
    private int building;
    private byte[] car_badge;

    /**
     * @return the name_hash
     */
    public List<String> getName_hash() {
        return name_hash;
    }

    /**
     * @param name_hash the name_hash to set
     */
    public void setName_hash(List<String> name_hash) {
        this.name_hash = name_hash;
    }
    /**
     * @param name_hash the name_hash to set
     */
    public void addName_hash(String name_hash) {
        this.name_hash.add(name_hash);
    }
    /**
     * @return the unique_hash
     */
    public String getUnique_hash() {
        return unique_hash;
    }

    /**
     * @param unique_hash the unique_hash to set
     */
    public void setUnique_hash(String unique_hash) {
        this.unique_hash = unique_hash;
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
        this.location = location;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the columnn
     */
    public int getColumnn() {
        return columnn;
    }

    /**
     * @param columnn the columnn to set
     */
    public void setColumnn(int columnn) {
        this.columnn = columnn;
    }

    /**
     * @return the building
     */
    public int getBuilding() {
        return building;
    }

    /**
     * @param building the building to set
     */
    public void setBuilding(int building) {
        this.building = building;
    }

    /**
     * @return the car_badge
     */
    public byte[] getCar_badge() {
        return car_badge;
    }

    /**
     * @param car_badge the car_badge to set
     */
    public void setCar_badge(byte[] car_badge) {
        this.car_badge = car_badge;
    }
    
    public BufferedImage getCarBufferedImage(){
         return ByteImage.getImageFromBytes(car_badge);
    }
    
    
    public static Car convertCar(CityKingsCarRaw car) {
        Car c=new Car();
        c.setBuilding(car.getBuilding());
        c.setCar_badge(car.getCarBadge());
        c.setColumnn(car.getColumn());
        c.setLocation(car.getLocation());
        c.setRow(car.getRow());
        c.setUnique_hash(car.getUniqueHash());
        c.addName_hash(car.getNameHash());
        return c;
    }

    
}

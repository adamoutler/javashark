/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures;

import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.imagerecognition.ImageJoiner;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author adamo
 */
public class Gangster {

    String username = "MISSING";
    String in_game_username;
    Float UTC_offset;
    String location;
    private String imageLocation;
    List<String> name_hashes = new ArrayList<>();
    transient private BufferedImage allCarsPic;
    transient private LinkedList<BufferedImage> carPics = new LinkedList<>();
    private List<GangCar> cars = new ArrayList<>();

    Gangster(CityKingsCarRaw car) {
        this.name_hashes.add(car.getNameHash());
    }

    public Gangster(String hash) {
        this.name_hashes.add(hash);
    }

    /**
     * Creates a new gangster
     *
     * @param hash hash of user to create
     * @return new gangster
     */
    public static Gangster create_User(String hash) {
        Gangster g = new Gangster(hash);
        return g;
    }

    /**
     * gets the gangster's username
     *
     * @return username of gangster
     */
    public String get_Username() {
        return username;
    }

    /**
     * sets the gangster name
     *
     * @param name new name of gangster
     */
    public void set_Username(String name) {
        username = name;
    }

    /**
     * gets the in-game name
     *
     * @return gangster name
     */
    public String get_in_game_username() {
        return in_game_username;
    }

    /**
     * sets the gangster name
     *
     * @param name new name of gangster
     */
    public void set_in_game_username(String name) {
        in_game_username = name;
    }

    /**
     * gets the UTC offset of gangster
     *
     * @return float value from -12 to +13 for UTC time correction
     */
    public Float get_UTC_offset() {
        return UTC_offset;
    }

    /**
     * sets the utc offset of the gangster
     *
     * @param offset desired UTC offset
     */
    public void set_UTC_offset(Float offset) {
        UTC_offset = offset;
    }

    /**
     * gets the location of the gangster
     *
     * @return location of user
     */
    public String get_location() {
        return location;
    }

    /**
     * sets the location of the user
     *
     * @param loc desired location
     */
    public void set_location(String loc) {
        location = loc;
    }

    /**
     * Adds a user hash
     *
     * @param hash new hash to be added to user
     */
    public void add_hash(String hash) {
        if (!name_hashes.contains(hash)) {
            name_hashes.add(hash);
        }
    }

    /**
     * gets the hash of the user's in-game nametag
     *
     * @return a list of 0 or more hashes to identify the user
     */
    public List<String> get_hashes() {
        return name_hashes;
    }

    /**
     * @return the carPics
     */
    public LinkedList<BufferedImage> getCarPics() {
        return carPics;
    }

    /**
     * @param carPics the carPics to set
     * @return reference to this object
     */
    public Gangster setCarPics(LinkedList<BufferedImage> carPics) {
        this.carPics = carPics;
        return this;
    }

    /**
     * @param carPic the carPics to set
     * @return reference to this object
     */
    public Gangster addCarPics(BufferedImage carPic) {
        this.carPics.add(carPic);
        return this;
    }

    /**
     * @return the cars
     */
    public List<GangCar> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     * @return reference to this object
     */
    public Gangster setCars(List<GangCar> cars) {
        this.cars = cars;
        return this;
    }

    /**
     * @param car the cars to set
     * @return reference to this object
     */
    public Gangster addCar(GangCar car) {
        this.cars.add(car);
        this.carPics.add(car.getCarBufferedImage());
        this.allCarsPic = ImageJoiner.joinImagesSide(carPics);
        return this;
    }

    /**
     * @return the allCarsPic
     */
    public BufferedImage getAllCarsPic() {
        return allCarsPic;
    }

    /**
     * @param allCarsPic the allCarsPic to set
     * @return reference to this object
     */
    public Gangster setAllCarsPic(BufferedImage allCarsPic) {
        this.allCarsPic = allCarsPic;
        return this;
    }

    /**
     * @return the imageLocation
     */
    public String getImageLocation() {
        return imageLocation;
    }

    /**
     * @param imageLocation the imageLocation to set
     */
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

}

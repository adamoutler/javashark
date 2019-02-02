/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures;

import java.util.List;

/**
 *
 * @author adamo
 */
class Gangster {

    String username;
    String in_game_username;
    Float UTC_offset;
    String location;
    List<String> name_hashes;
    List<Car> cars;

    /**
     * Creates a new gangster
     *
     * @param name name of the gangster
     * @return new gangster
     */
    static Gangster create_User(String name) {
        Gangster g = new Gangster();
        g.username = name;
        return g;
    }

    /**
     * gets the gangster's username
     *
     * @return username of gangster
     */
    String get_Username() {
        return username;
    }

    /**
     * sets the gangster name
     *
     * @param name new name of gangster
     */
    void set_Username(String name) {
        username = name;
    }

    /**
     * gets the in-game name
     *
     * @return gangster name
     */
    String get_in_game_username() {
        return in_game_username;
    }

    /**
     * sets the gangster name
     * @param name new name of gangster
     */
    void set_in_game_username(String name) {
        in_game_username = name;
    }

    /**
     * gets the UTC offset of gangster
     * @return float value from -12 to +13 for UTC time correction
     */
    Float get_UTC_offset() {
        return UTC_offset;
    }

    /**
     * sets the utc offset of the gangster
     * @param offset desired UTC offset
     */
    void set_UTC_offset(Float offset) {
        UTC_offset = offset;
    }

    /**
     * gets the location of the gangster
     * @return location of user
     */
    String get_location() {
        return location;
    }

    /**
     * sets the location of the user
     * @param loc desired location
     */
    void set_location(String loc) {
        location = loc;
    }

    /**
     * Adds a user hash
     * @param hash new hash to be added to user
     */
    void add_hash(String hash) {
        name_hashes.add(hash);
    }

    /**
     * gets the hash of the user's in-game nametag
     * @return a list of 0 or more hashes to identify the user
     */
    List<String> get_hashes() {
        return name_hashes;
    }

    /**
     * gets car badge of user
     * @return a byte array jpeg
     */
    byte[] get_car_badge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * sets the user car badge
     * @param badge byte array jpeg
     * @return 
     */
    void set_car_badge(byte[] badge) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * writes the badge of all current cars to a file
     * @return path to file
     */
    String write_badge_to_www_file() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

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
class Gang {

    String gang_name;
    List<Gangster> users;

    /**
     * 
     * @return gang name
     */
    String get_Gang_Name() {
        return gang_name;
    }

    /**
     * sets the gang name
     * @param name name of gang
     */
    void set_Gang_Name(String name) {
        gang_name = name;
    }

    /**
     * Adds a gangster to the gang
     * @param gangster g to be added
     */
    void add_gangster(Gangster gangster) {
        users.add(gangster);
    }

    /**
     * gets a gangster by the name 
     * @param name identifier of gangster
     * @return Gangster object representing the request
     */
    Gangster get_gangster_by_name(String name) {
        for (Gangster g : users) {
            if (g.username.equals(name)) {
                return g;
            }
        }
        return null;
    }
/**
     * gets a gangster by a hash
     * @param name identifier of gangster
     * @return Gangster object representing the request
     */
    Gangster get_gangster_by_hash(String hash) {
        for (Gangster g : users) {
            if (g.name_hashes.contains(hash)) {
                return g;
            }
        }
        return null;
    }
/**
     * gets a gangster by the in game name 
     * @param name identifier of gangster
     * @return Gangster object representing the request
     */
    Gangster get_gangster_by_in_game_username(String name) {
        for (Gangster g : users) {
            if (g.in_game_username.equals(name)) {
                return g;
            }
        }
        return null;
    }

    /**
     * creates a new gang
     * @param name name of the gang to be added
     * @return a new gang object
     */
    static Gang create_gang(String name) {
        Gang gang = new Gang();
        gang.gang_name = name;
        return gang;
    }

}

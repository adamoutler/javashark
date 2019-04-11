/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

/**
 *
 * @author adamo
 */
public class Gang {

    String gang_name;
   ArrayList<Gangster>  users = new ArrayList<>();
    String reportName;

    public Gang(String gang) {
        this.gang_name = gang;
    }

    /**
     *
     * @return gang name
     */
    public String get_Gang_Name() {
        return gang_name;
    }

    /**
     * sets the gang name
     *
     * @param name name of gang
     */
    public void set_Gang_Name(String name) {
        gang_name = name;
    }

    /**
     * Adds a gangster to the gang
     *
     * @param gangster g to be added
     */
    public void add_gangster(Gangster gangster) {
        users.add(gangster);
    }

    /**
     * gets a gangster by the name
     *
     * @param name identifier of gangster
     * @return Gangster object representing the request
     */
    public Gangster get_gangster_by_name(String name) {
        for (Gangster g : users) {
            if (g.username.equals(name)) {
                return g;
            }
        }
        return null;
    }

    /**
     * gets a gangster by a hash
     *
     * @param name identifier of gangster
     * @return Gangster object representing the request
     */
    public Gangster get_gangster_by_hash(String hash) {

        for (Gangster g : users) {
            if (g.name_hashes.contains(hash)) {
                return g;
            }
        }
        Gangster g = new Gangster(hash);
        users.add(g);
        return g;
    }

    /**
     * gets a gangster by the in game name
     *
     * @param name identifier of gangster
     * @return Gangster object representing the request
     */
    public Gangster get_gangster_by_in_game_username(String name) {
        for (Gangster g : users) {
            if (g.in_game_username.equals(name)) {
                return g;
            }
        }
        return null;
    }

    /**
     * creates a new gang
     *
     * @param name name of the gang to be added
     * @return a new gang object
     */
    public static Gang create_gang(String name) {
        Gang gang = new Gang(name);
        gang.gang_name = name;
        return gang;
    }

}

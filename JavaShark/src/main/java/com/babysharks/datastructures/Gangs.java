/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures;

import java.util.HashMap;

/**
 *
 * @author adamo
 */
public class Gangs {

    HashMap<String, Gang> gangs = new HashMap<>();

    /**
     * adds a gang to the list
     *
     * @param gang gang to add
     */
    public Gang add_gang(Gang gang) {
        gangs.put(gang.gang_name, gang);
        return gang;
    }

    /**
     * retrieves a gang from the list
     *
     * @param name name to search
     * @return Gang object representing search
     */
    public Gang get_Gang_By_Name(String name) {

        if (gangs.get(name).gang_name.equals(name)) {
            return gangs.get(name);
        }

        return add_gang(new Gang(""));
    }

    /**
     * retrieves a list of all gangs
     *
     * @return all gangs
     */
    HashMap<String,Gang> get_gangs() {
        return gangs;
    }
}

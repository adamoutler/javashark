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
public class Gangs {

    List<Gang> gangs;

    /**
     * adds a gang to the list
     * @param gang gang to add
     */
    void add_gang(Gang gang) {
        gangs.add(gang);
    }

    /**
     * retrieves a gang from the list
     * @param name name to search
     * @return Gang object representing search
     */
    Gang get_Gang_By_Name(String name) {
        for (Gang gang : gangs) {
            if (gang.gang_name.equals(name)) {
                return gang;
            }
        }
        return null;
    }

    /**
     * retrieves a list of all gangs
     * @return all gangs
     */
    List<Gang> get_gangs() {
        return gangs;
    }
}

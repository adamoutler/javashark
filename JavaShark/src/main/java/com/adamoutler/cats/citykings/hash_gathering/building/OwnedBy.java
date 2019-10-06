/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building;

/**
 *
 * @author adamo
 */
public enum OwnedBy {
    NOBODY(0x5e6a83, "noone"),
    US(0x90b3eb, "us"),
    THEM(0xFF6564, "them"),
    WHITEBORDER(0xffffff,"White Border");
    int color;
    String friendlyName;

    OwnedBy(int color, String friendlyName) {
        this.color = color;
        this.friendlyName = friendlyName;
    }

    public int getColor() {
        return color;
    }

    public String getFriendlyname() {
        return friendlyName;
    }
}

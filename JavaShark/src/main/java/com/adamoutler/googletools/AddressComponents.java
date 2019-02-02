/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

/**
 *
 * @author adamo
 */
public class AddressComponents {

    private String long_name;
    private String short_name;
    private String[] types;

    /**
     * @return the long_name
     */
    public String getLong_name() {
        return long_name;
    }

    /**
     * @param long_name the long_name to set
     */
    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    /**
     * @return the short_name
     */
    public String getShort_name() {
        return short_name;
    }

    /**
     * @param short_name the short_name to set
     */
    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    /**
     * @return the types
     */
    public String[] getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(String[] types) {
        this.types = types;
    }

}

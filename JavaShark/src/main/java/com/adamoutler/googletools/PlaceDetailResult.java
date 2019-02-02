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
public class PlaceDetailResult {

    private AddressComponents[] address_components;
    private int utc_offset;

    /**
     * @return the address_components
     */
    public AddressComponents[] getAddressComponents() {
        return getAddress_components();
    }

    /**
     * @param address_components the address_components to set
     */
    public void setAddress_components(AddressComponents[] address_components) {
        this.address_components=address_components;
    }

    /**
     * @return the address_components
     */
    public AddressComponents[] getAddress_components() {
        return address_components;
    }


    /**
     * @return the utc_offset
     */
    public int getUtc_offset() {
        return utc_offset;
    }

    /**
     * @param utc_offset the utc_offset to set
     */
    public void setUtc_offset(int utc_offset) {
        this.utc_offset = utc_offset;
    }

}

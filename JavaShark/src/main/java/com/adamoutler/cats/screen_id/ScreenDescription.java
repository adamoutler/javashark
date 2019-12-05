/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.PicCoords;

/**
 *
 * @author adamo
 */
public class ScreenDescription {

    final private String name;
    final private String description;
    transient final private PicCoords screenArea;
    final private boolean calibratable;

    public ScreenDescription(String name, String description, PicCoords screenArea) {
        calibratable = screenArea != null;
        this.screenArea = screenArea;
        this.name = name;
        this.description = description;

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the screenArea
     */
    public PicCoords getScreenArea() {
        return screenArea;
    }

    /**
     * @return the calibratable
     */
    public boolean isCalibratable() {
        return calibratable;
    }

}

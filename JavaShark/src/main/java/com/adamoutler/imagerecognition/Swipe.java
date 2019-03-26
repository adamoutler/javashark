/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

/**
 *
 * @author adamo
 */
public class Swipe {

    final private int fromX;
    final private int fromY;
    final private int toX;
    final private int toY;
    final private int speed;

    public Swipe(int fromX, int fromY, int toX, int toY, int speed) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.speed = speed;

    }

    /**
     * @return the fromX
     */
    public int getFromX() {
        return fromX;
    }

    /**
     * @return the fromY
     */
    public int getFromY() {
        return fromY;
    }

    /**
     * @return the toX
     */
    public int getToX() {
        return toX;
    }

    /**
     * @return the toY
     */
    public int getToY() {
        return toY;
    }

    /**
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

}

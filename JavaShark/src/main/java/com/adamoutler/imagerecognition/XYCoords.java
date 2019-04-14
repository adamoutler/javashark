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
public class XYCoords {

    int x;
    int y;

    public XYCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }
    
    public String getXStr(){
        return Integer.toString(x);
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    public String getYStr(){
        return Integer.toString(y);
    }

    @Override
    public boolean equals(Object o){
        if (o.getClass().equals(this.getClass())){
            XYCoords xy=(XYCoords)o;
            if (xy.getX()==this.getX() && xy.getY()==this.getY()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.x;
        hash = 37 * hash + this.y;
        return hash;
    }
    @Override
    public String toString(){
        return "x:"+x+"  y:"+y;
    }
}

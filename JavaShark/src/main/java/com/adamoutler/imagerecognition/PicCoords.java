/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class PicCoords extends XYCoords {
    private int w;
    private int h;
    public PicCoords(int x, int y, int w, int h){
        super(x,y);
        this.w=w;
        this.h=h;
    }

  
    /**
     * @return the w
     */
    public int getW() {
        return w;
    }

    /**
     * @return the h
     */
    public int getH() {
        return h;
    }
    
    public  BufferedImage getImageAtCoords(BufferedImage bi){
        BufferedImage subImage=bi.getSubimage(x, y, getW(), getH());
        return subImage;
    }
    
    
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(super.toString());
        sb.append(" W:").append(getW()).append(" H:").append(this.getH());
        return sb.toString();
    }
           
}

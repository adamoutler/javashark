/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author adamo
 */
public class HashedPicCoords extends PicCoords {

    Integer hash;

    private static boolean debug = false;

    public HashedPicCoords(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public HashedPicCoords(int x, int y, int w, int h, Integer hash) {
        super(x, y, w, h);
        this.hash = hash;
    }

    public int getImageHash(BufferedImage image) {
        BufferedImage bi = image.getSubimage(x, y, getW(), getH());
        byte[] bytes = ByteImage.getPngBytes(bi);
        int imageHash = Arrays.hashCode(bytes);

        if (debug) {
            System.out.println("" + x + "," + y + "," + getW() + "," + getH() + "," + imageHash);
        }
        return imageHash;
    }
    
    public Integer getHash(){
        return hash;
    }

    public boolean hashMatches(BufferedImage image) {
        int testhash = getImageHash(image);
        if (hash==null) return false;
        return testhash == hash;
    }

    /**
     * @param aDebug the debug to set
     */
    public static void setDebug(boolean aDebug) {
        debug = aDebug;
    }

    @Override
    public BufferedImage getImageAtCoords(BufferedImage bi) {
        BufferedImage subImage = bi.getSubimage(x, y, getW(), getH());
        return subImage;
    }

    @Override
    public String toString() {
        return super.toString() + "," + hash;

    }
    
    public HashedPicCoords clone(int newHash) throws CloneNotSupportedException{
        super.clone();
        HashedPicCoords hpc=new HashedPicCoords(x,y,this.getW(),getH(),newHash);
        return hpc;
    }
    
    public HashedPicCoords clone() throws CloneNotSupportedException{
        super.clone();
        HashedPicCoords hpc=new HashedPicCoords(x,y,this.getW(),getH(),hash);
        return hpc;
    }

}

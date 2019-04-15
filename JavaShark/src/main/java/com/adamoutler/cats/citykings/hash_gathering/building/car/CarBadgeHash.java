/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building.car;


import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.PicCoords;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 *
 * @author adamo
 */
public class CarBadgeHash {

    final static PicCoords NAMEHASHCOORD = new PicCoords(89, 15, 143, 24);
    final static PicCoords UNIQUEHASHCOORD = new PicCoords(6, 232, 300, 38);
    final static PicCoords BOTTOMLINEHASHCOORD=new PicCoords(0,0,300, 38);
    
    /**
     * gets the hash of the name portion of the picture.
     *
     * @param carBadge the standard car badge
     * @return hashcode representing the image.
     */
    public static int getNameHash(BufferedImage carBadge) {
        BufferedImage name = carBadge.getSubimage(NAMEHASHCOORD.getX(), NAMEHASHCOORD.getY(), NAMEHASHCOORD.getW(), NAMEHASHCOORD.getH());
        return getHash(name);
    }

    /**
     * gets the hash of the car portion of the picture.
     *
     * @param attackInfo the standard car badge
     * @return hashcode representing the image.
     */
    public static int getUniqueHash(BufferedImage attackInfo) {
        BufferedImage name = attackInfo.getSubimage(0, 0, attackInfo.getWidth()-1, attackInfo.getHeight()-1);
        return getHash(name);
    }
    
    public static int getBottomHash(BufferedImage attackInfo) {
        BufferedImage name = attackInfo.getSubimage(attackInfo.getHeight()-4, 0, attackInfo.getWidth()-1, attackInfo.getHeight()-1);
        return getHash(name);
    }
    

    private static int getHash(BufferedImage bi) {
        byte[] bytes = imageToByteArray(bi);
        return Arrays.hashCode(bytes);
    }

    private static byte[] imageToByteArray(BufferedImage image) {
        return ByteImage.getPngBytes(image);
    }

    private static byte[] concatenate(byte[] a, byte[] b) {
        byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}

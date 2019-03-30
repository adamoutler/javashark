/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class ImageJoiner {

    public static BufferedImage joinImagesBelow(LinkedList<BufferedImage> images) {

        BufferedImage image = new BufferedImage(getMaxWidth(images), 1, BufferedImage.TYPE_4BYTE_ABGR);
        image.setRGB(0, 0, new Color(0, 0, 0, 0).getRGB());
        for (int i = 0; i < images.size(); i++) {
            image = joinImagesBelow(image, images.get(i));
        }
        return image;
    }

    private static int getMaxWidth(LinkedList<BufferedImage> images) {
        int width = 0;
        for (BufferedImage bi : images) {
            if (bi.getWidth() > width) {
                width = bi.getWidth();
            }
        }
        return width;
    }

    public static BufferedImage joinImagesSide(LinkedList<BufferedImage> images) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        image.setRGB(0, 0, new Color(0, 0, 0, 0).getRGB());
        for (int i = 0; i < images.size(); i++) {
            image = joinImagesSide(image, images.get(i));
        }
        return image;
    }

    public static BufferedImage joinImagesBelow(BufferedImage img1, BufferedImage img2) {

        //do some calculate first
        int height = img1.getHeight() + img2.getHeight();
        int wid = Math.max(img1.getWidth(), img2.getWidth());
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight());
        g2.dispose();
        return newImage;
    }

    public static BufferedImage joinImagesSide(BufferedImage img1, BufferedImage img2) {

        //do some calculate first
        int h1 = img1.getHeight();
         int h2 = img2.getHeight();
        int height=h1>=h2?h1:h2;
        int wid = img1.getWidth() + img2.getWidth();
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth(), 0);
        g2.dispose();
        return newImage;
    }
}

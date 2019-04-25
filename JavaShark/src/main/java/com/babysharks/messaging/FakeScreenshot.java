/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.imagerecognition.ImageJoiner;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.WritePicture;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class FakeScreenshot {

    public static BufferedImage getScreenshot(String gang, String name) {
        BufferedImage image = WritePicture.createImage(new PicCoords(0, 0, 1024, 500), new XYCoords(150, 300), "Hey, @" + name , false, Color.BLACK, Color.GREEN, new Font("helvetica", 10, 70));
        BufferedImage image2=  WritePicture.createImage(new PicCoords(0, 0, 1024, 300), new XYCoords(100, 100),  gang + " has no active sentries!", false, Color.BLACK, Color.GREEN, new Font("helvetica", 10, 50));
        LinkedList<BufferedImage> list=new LinkedList<>();
        list.add(image);
        list.add(image2);
        BufferedImage joinedImage=ImageJoiner.joinImagesBelow(list);
        return joinedImage;

    }
}

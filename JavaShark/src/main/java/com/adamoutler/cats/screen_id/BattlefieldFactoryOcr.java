/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.cats.battlefield.Battlefield;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.TesseractBuilder;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;

/**
 *
 * @author adamo
 */
public class BattlefieldFactoryOcr {

    public static PicCoords opponentName = new PicCoords(684, 71, 160, 60);

    public static String setOpponentName(BufferedImage screenshot, Battlefield battlefield) {
       
        String returnValue = "";
        try {
            TesseractBuilder tb = new TesseractBuilder(screenshot)
                    .setCharset("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-#@^")
                    .crop(opponentName)
                    .setMode(TesseractBuilder.Mode.SINGLEWORD)
                    .invert().brightenImage().brightenImage().invert()
                    .resize (5,2).resize (4,3).resize (4,3)
                    .invert().bicubic().invert()
                    .greyScale().brightenImage().writePicture();
            returnValue = tb.execute();
            tb.writePicture(returnValue);
            battlefield.setOcrNamePic(opponentName.getImageAtCoords(screenshot));

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnValue;
    }

}

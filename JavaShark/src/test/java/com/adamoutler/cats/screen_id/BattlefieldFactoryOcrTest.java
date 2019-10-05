/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.cats.battlefield.Battlefield;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class BattlefieldFactoryOcrTest {

    public BattlefieldFactoryOcrTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    File ss1 = new File("src/test/java/com/adamoutler/cats/screen_id/resources/CITYKINGS.1.png");
    File ss2 = new File("src/test/java/com/adamoutler/cats/screen_id/resources/CITYKINGS.2.png");
    File battlefield = new File("src/test/java/com/adamoutler/cats/screen_id/resources/scan-1561227027773-development-AdamOutler-c165a434-3956-40af-883d-2dfb88ac3065OCRName-E9557;");

    /**
     * Test of setOpponentName method, of class BattlefieldFactoryOcr.
     */
    @Test
    public void testSetOpponentName() {
        try {
            System.out.println("setOpponentName");
            BufferedImage screenshot = ImageIO.read(ss1);
            FileReader fr = new FileReader(battlefield);
            Battlefield battlefield = new GsonBuilder().create().fromJson(fr, Battlefield.class);
            String expResult = "-IMMORTALS-";
            String result = BattlefieldFactoryOcr.setOpponentName(screenshot, battlefield);
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
        } catch (IOException ex) {
            Logger.getLogger(BattlefieldFactoryOcrTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testSetOpponentName2() {
        try {
            System.out.println("setOpponentName");
            BufferedImage screenshot = ImageIO.read(ss2);
            FileReader fr = new FileReader(battlefield);
            Battlefield battlefield = new GsonBuilder().create().fromJson(fr, Battlefield.class);
            String expResult = "CAPTAINCATS";
            String result = BattlefieldFactoryOcr.setOpponentName(screenshot, battlefield);
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
        } catch (IOException ex) {
            Logger.getLogger(BattlefieldFactoryOcrTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

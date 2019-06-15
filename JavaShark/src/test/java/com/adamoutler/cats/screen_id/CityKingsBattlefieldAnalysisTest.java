/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.XYCoords;
import java.awt.image.BufferedImage;
import java.io.File;
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
public class CityKingsBattlefieldAnalysisTest {

    public CityKingsBattlefieldAnalysisTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            bi = ImageIO.read(f);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        instance = new CityKingsBattlefieldAnalysis(bi);
    }

    @After
    public void tearDown() {
    }
    CityKingsBattlefieldAnalysis instance;
    BufferedImage bi = null;

    File f = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.4.png");

    /**
     * Test of writePicture method, of class CityKingsBattlefieldAnalysis.
     */
    @Test

    public void testWritePicture() {
        System.out.println("writePicture");
        PicCoords pc = CityKingsBattlefieldAnalysis.OURSCORECOORDS;
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getLeftSideOfOurGainingScore method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetLeftSideOfOurGainingScore() {
        System.out.println("getLeftSideOfOurGainingScore");
        XYCoords expResult = new XYCoords(207, 136);
        XYCoords result = instance.getLeftSideOfOurGainingScore();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinatesOurGaining method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetCoordinatesOurGaining() {
        System.out.println("getCoordinatesOurGaining");
        PicCoords expResult = new PicCoords(207, 136, 58, 45);
        PicCoords result = instance.getCoordinatesOurGaining();
        assertEquals(expResult, result);

    }

    /**
     * Test of getCoordinatesOurScore method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetCoordinatesOurScore() {
        System.out.println("getCoordinatesOurScore");
        PicCoords expResult = new PicCoords(274, 126, 116, 45);
        PicCoords result = instance.getCoordinatesOurScore();
        assertEquals(expResult, result);

    }

    /**
     * Test of getRightSideOfTheirGainingScore method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetRightSideOfTheirGainingScore() {
        System.out.println("getRightSideOfTheirGainingScore");
        XYCoords expResult = new XYCoords(739, 136);
        XYCoords result = instance.getLeftSideOfTheirGainingScore();
        assertEquals(expResult, result);

    }

    /**
     * Test of getCoordinatesTheirGaining method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetCoordinatesTheirGaining() {
        System.out.println("getCoordinatesTheirGaining");
        PicCoords expResult = new PicCoords(739, 136, 59, 45);
        PicCoords result = instance.getCoordinatesTheirGaining();

        assertEquals(expResult, result);

    }

    /**
     * Test of getCoordinatesTheirScore method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetCoordinatesTheirScore() {
        System.out.println("getCoordinatesTheirScore");
        PicCoords expResult = new PicCoords(632, 131, 99, 45);
        PicCoords result = instance.getCoordinatesTheirScore();
        assertEquals(expResult, result);

    }

    @Test
    public void testCoords() {
        try {
            File test;
            BufferedImage bbi;
            CityKingsBattlefieldAnalysis minstance;
            
             test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.5.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            
//I'm getting a 31 here instead of 81    
            assertEquals("81", minstance.getOurGainingScore());
            assertEquals("2382", minstance.getOurCurrentScore());
            assertEquals("23", minstance.getHours());
            assertEquals("25", minstance.getMinutes());
           
//i'm getting a 133000 here but i'm expecting 138000 
            assertEquals("138000", minstance.getIVScore());
            assertEquals("0", minstance.getTheirCurrentScore());
            assertEquals("0", minstance.getTheirGainingScore());
            System.out.println("5" + minstance);
     
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            System.out.println("1" + minstance.toString());
            assertEquals("17", minstance.getMinutes());
            assertEquals("17", minstance.getHours());
            assertEquals("3255", minstance.getTheirCurrentScore());
            assertEquals("43353", minstance.getOurCurrentScore());
            assertEquals("144", minstance.getOurGainingScore());
            assertEquals("0", minstance.getTheirGainingScore());
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.3.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            assertEquals("77", minstance.getOurGainingScore());
            assertEquals("2699", minstance.getOurCurrentScore());
            assertEquals("23", minstance.getHours());
            assertEquals("16", minstance.getMinutes());
            assertEquals("129000", minstance.getIVScore());
            assertEquals("0", minstance.getTheirCurrentScore());
            assertEquals("0", minstance.getTheirGainingScore());

            System.out.println("3" + minstance);
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.4.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
///  Expected 61 but got 31
            assertEquals("61", minstance.getOurGainingScore());
            assertEquals("28877", minstance.getOurCurrentScore());
            assertEquals("13", minstance.getHours());
            assertEquals("19", minstance.getMinutes());
            assertEquals("106000", minstance.getIVScore());
            assertEquals("8216", minstance.getTheirCurrentScore());
            assertEquals("13", minstance.getTheirGainingScore());

           
            System.out.println("5" + minstance);
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.6.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            assertEquals("144", minstance.getOurGainingScore());
            assertEquals("106134", minstance.getOurCurrentScore());
            assertEquals("11", minstance.getHours());
            assertEquals("27", minstance.getMinutes());
            
//expected 138000 but got 133000
            assertEquals("138000", minstance.getIVScore());
            assertEquals("735", minstance.getTheirCurrentScore());
            assertEquals("0", minstance.getTheirGainingScore());
            System.out.println("6" + minstance);
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.7.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            assertEquals("144", minstance.getOurGainingScore());
            assertEquals("100518", minstance.getOurCurrentScore());
            assertEquals("12", minstance.getHours());
            assertEquals("6", minstance.getMinutes());
//expected 138000 but got 133000
            assertEquals("138000", minstance.getIVScore());
            assertEquals("735", minstance.getTheirCurrentScore());
            assertEquals("0", minstance.getTheirGainingScore());
            System.out.println("7" + minstance);

        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

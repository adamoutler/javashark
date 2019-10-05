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

    File f = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.png");

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
        XYCoords expResult = new XYCoords(227, 136);
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
        PicCoords expResult = new PicCoords(227, 136, 58, 45);
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
        PicCoords expResult = new PicCoords(294, 126, 96, 60);
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
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            assertEquals("63", minstance.getOurGainingScore());
            assertEquals("5355", minstance.getOurCurrentScore());
            assertEquals("22", minstance.getHours());
            assertEquals("20", minstance.getMinutes());
            assertEquals("138000", minstance.getIVScore());
            assertEquals("7665", minstance.getTheirCurrentScore());
            assertEquals("81", minstance.getTheirGainingScore());
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void testCoords2() {
        try {
            File test;
            BufferedImage bbi;
            CityKingsBattlefieldAnalysis minstance;
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.1.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            assertEquals("63", minstance.getOurGainingScore());
            assertEquals("12285", minstance.getOurCurrentScore());
            assertEquals("20", minstance.getHours());
            assertEquals("30", minstance.getMinutes());
            assertEquals("138000", minstance.getIVScore());
            assertEquals("16575", minstance.getTheirCurrentScore());
            assertEquals("81", minstance.getTheirGainingScore());
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void testCoords3() {
        try {
            File test;
            BufferedImage bbi;
            CityKingsBattlefieldAnalysis minstance;
            test = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.2.png");
            bbi = ImageIO.read(test);
            minstance = new CityKingsBattlefieldAnalysis(bbi);
            assertEquals("82", minstance.getOurGainingScore());
            assertEquals("85573", minstance.getOurCurrentScore());
            assertEquals("2", minstance.getHours());
            assertEquals("12", minstance.getMinutes());
            assertEquals("140000", minstance.getIVScore());
            assertEquals("89060", minstance.getTheirCurrentScore());
            assertEquals("64", minstance.getTheirGainingScore());
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

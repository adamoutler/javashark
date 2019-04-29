/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.OCR;
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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        instance.writePicture(pc, bi);
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
        CityKingsBattlefieldAnalysis.writePicture(result, bi);
        assertEquals(expResult, result);

    }

    /**
     * Test of getCoordinatesOurScore method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetCoordinatesOurScore() {
        System.out.println("getCoordinatesOurScore");
        PicCoords expResult = new PicCoords(274,126,116,60);
        PicCoords result = instance.getCoordinatesOurScore();
        CityKingsBattlefieldAnalysis.writePicture(result, bi);

        assertEquals(expResult, result);

    }

    /**
     * Test of getRightSideOfTheirGainingScore method, of class
     * CityKingsBattlefieldAnalysis.
     */
    @Test
    public void testGetRightSideOfTheirGainingScore() {
        System.out.println("getRightSideOfTheirGainingScore");
        XYCoords expResult = new XYCoords(739,136);
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
        PicCoords expResult = new PicCoords(739,136,59,45);
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
        PicCoords expResult = new PicCoords(632,131,99,55);
        PicCoords result = instance.getCoordinatesTheirScore();
        CityKingsBattlefieldAnalysis.writePicture(result, bi);
        assertEquals(expResult, result);

    }
    
    @Test
    public void testCoords(){
        try {
            File test=new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.6.png");
            BufferedImage bbi = ImageIO.read(f);
            CityKingsBattlefieldAnalysis minstance = new CityKingsBattlefieldAnalysis(bbi);
            System.out.println(minstance);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        
        
    }

}

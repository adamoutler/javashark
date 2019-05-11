/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TesseractBuilderTest {

    public TesseractBuilderTest() {
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

    File f = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.4.png");

    /**
     * Test of resize method, of class TesseractBuilder.
     */
    @Test
    public void testResize_float() {
        try {
            System.out.println("resize");
            float scale = 2.0F;
            TesseractBuilder instance = new TesseractBuilder(f);
            TesseractBuilder result = instance.resize(scale);
            int expectedResult = 2048;
            assertEquals(expectedResult, result.getImage().getWidth());

        } catch (IOException ex) {
            Logger.getLogger(TesseractBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of resize method, of class TesseractBuilder.
     */
    @Test
    public void testResize_float_float() {
        try {
            System.out.println("resize");
            float xValue = 2.0F;
            float yValue = 2.0F;
            TesseractBuilder instance = new TesseractBuilder(f);
            int expectedWidth = 2048;
            int expectedHeight = 1536;
            TesseractBuilder result = instance.resize(xValue, yValue);
            assertEquals(expectedWidth, result.getImage().getWidth());
            assertEquals(expectedHeight, result.getImage().getHeight());
        } catch (IOException ex) {
            Logger.getLogger(TesseractBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Test of setCharset method, of class TesseractBuilder.
     */
    @Test
    public void testSetCharset() {
        try {
            System.out.println("setCharset");
            String charset = "afghnfgd";
            TesseractBuilder result = new TesseractBuilder(f).setCharset(charset);

            assertEquals(charset, result.getCharset());

        } catch (IOException ex) {
            Logger.getLogger(TesseractBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of invert method, of class TesseractBuilder.
     */
    @Test
    public void testInvert() {
        try {
            System.out.println("invert");
            TesseractBuilder instance = new TesseractBuilder(f);
            int invertedColor = -13771558;
            int result = instance.invert().getImage().getRGB(100, 100);
            assertEquals(invertedColor, result);

        } catch (IOException ex) {
            Logger.getLogger(TesseractBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of execute method, of class TesseractBuilder.
     */
    @Test
    public void testExecute() throws Exception {
        File f1 = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/CITYKINGS.png");

        System.out.println("execute");
        String expResult = "143353";
        String result = new TesseractBuilder(f1)
                .setCharset("1234567890")
                .setMode(TesseractBuilder.Mode.SINGLEWORD)
                .crop(CityKingsBattlefieldAnalysis.OURSCORECOORDS)
                .resize(3).writePicture().execute();
        assertEquals(expResult, result);
        expResult = "17h 17m";
        result = new TesseractBuilder(f1)
                .setCharset("0123456789hms")
                .crop(CityKingsBattlefieldAnalysis.TIMEREMAININGCOORDS)
                .setMode(TesseractBuilder.Mode.SINGLELLINE)
                .resize(9, 5).invert().greyScale().brightenImage().brightenImage()
                .brightenImage().brightenImage()
                .writePicture().execute();
        assertEquals(expResult, result);

    }

}

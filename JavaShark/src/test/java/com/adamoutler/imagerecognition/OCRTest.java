/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.awt.image.BufferedImage;
import java.io.File;
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
public class OCRTest {
    
    public OCRTest() {
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

    /**
     * Test of main method, of class OCR.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        OCR instance = new OCR();
        instance.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTesseractPath method, of class OCR.
     */
    @Test
    public void testSetTesseractPath() {
        System.out.println("setTesseractPath");
        File path = null;
        OCR instance = new OCR();
        OCR expResult = null;
        OCR result = instance.setTesseractPath(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTextFromPicture method, of class OCR.
     */
    @Test
    public void testGetTextFromPicture() {
        System.out.println("getTextFromPicture");
        File f = null;
        OCR instance = new OCR();
        String expResult = "";
        String result = instance.getTextFromPicture(f);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of discern method, of class OCR.
     */
    @Test
    public void testDiscern() {
        System.out.println("discern");
        BufferedImage bi = null;
        OCR instance = new OCR();
        String expResult = "";
        String result = instance.discern(bi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

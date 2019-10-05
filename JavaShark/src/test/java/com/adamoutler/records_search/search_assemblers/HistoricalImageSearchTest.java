/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search.search_assemblers;

import java.awt.image.BufferedImage;
import java.util.Date;
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
public class HistoricalImageSearchTest {

    public HistoricalImageSearchTest() {
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
     * Test of getLastCars method, of class HistoricalImageSearch.
     */
    @Test
    public void testGetLastCars() throws Exception {
        System.out.println("getLastCars");
        String hash = "8a80e9b2";

        String folder = "src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/";
        String gang = "development";
        BufferedImage expResult = null;
        BufferedImage result = HistoricalImageSearch.getLastCars(hash, folder, gang);
        assert (result.getWidth() > 0); //this will fail if we do a scan which does not include that hash above.

    }

    /**
     * Test of getCarsByDate method, of class HistoricalImageSearch.
     */
    @Test
    public void testGetCarsByDate() throws Exception {
        System.out.println("getCarsByDate");
        String hash = "8a80e9b2";
        String folder = "src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/";
        String gang = "development";
        Date[] dates = new Date[]{};
        BufferedImage expResult = null;
        BufferedImage result = HistoricalImageSearch.getCarsByDate(hash, folder, gang, dates, true);
        assertEquals(expResult, result);
    }

}

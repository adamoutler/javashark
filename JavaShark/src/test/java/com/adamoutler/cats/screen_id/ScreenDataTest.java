/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;


import com.adamoutler.imagerecognition.HashedPicCoords;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class ScreenDataTest {
    
    public ScreenDataTest() {
    }
    
    static File[] files;

    @BeforeClass
    public static void setUpClass() {
        System.out.println(new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/").getAbsolutePath());
        files = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/").listFiles();
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
     * Test of values method, of class ScreenDataHash.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        ScreenData[] result = ScreenData.values();
        assert(result.length>20);
    }

    /**
     * Test of valueOf method, of class ScreenDataHash.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "CITYKINGS";
        ScreenData expResult = ScreenData.CITYKINGS;
        ScreenData result = ScreenData.valueOf(name);
        assertEquals(expResult, result);
 
    }

    /**
     * Test of getHashes method, of class ScreenDataHash.
     */
    @Test
    public void testGetHashes() {
        System.out.println("getHashes");
        ScreenData instance = ScreenData.CITYKINGS;
        ArrayList<HashedPicCoords> expResult = null;
        ArrayList<HashedPicCoords> result  = instance.getHashes();
        assert(result.size()>0);

    }


    /**
     * Test of getDescription method, of class ScreenDataHash.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        ScreenData instance = ScreenData.CITYKINGS;
        String expResult = "City Kings, battlefield screen";
        String result = instance.getDescription();
        assertEquals(expResult, result);

    }

   

   

    /**
     * Test of matchesHashes method, of class ScreenDataHash.
     */
    @Test
    public void testMatchesHashes() {
        System.out.println("matchesHashes");
        BufferedImage screen = null;
        ScreenData instance = ScreenData.BLACKSCREEN;
        boolean expResult = false;
        boolean result = instance.matchesHashes(screen);
        assertEquals(expResult, result);

    }
     /**
     * Test of identifyScreen method, of class ScreenData.
     */
    @Test
    public void testIdentifyScreen() {
        System.out.println("identifyScreen");

        for (File f : files) {

            try {
                System.out.println(f.getAbsolutePath());
                if (f.getAbsolutePath().endsWith("svn")) {
                    continue;
                }
                BufferedImage bi = ImageIO.read(f);
                String id = f.getName().split("\\.", 2)[0];
                ScreenData expResult = ScreenData.valueOf(id);
                if (expResult.equals(ScreenData.EXITSCREEN)){
                    System.out.println("exitscreen");
                }
                ScreenData result = ScreenData.identifyScreen(bi);
                if (expResult.getHashes().size() > 0) {
                    HashedPicCoords hpc = expResult.getHashes().get(0);
                    if (!result.equals(expResult)) {
                        System.out.println("Expected values for " + id + " are as follows");
                       System.out.println(expResult.getHashesPropertyEntry(bi));
                    }
                    if (expResult.name().equals("ULTIMATEGARAGE")) {
                        System.out.println(expResult);
                    }
                    System.out.println(new File(".").getAbsolutePath());
                    ///assertEquals(expResult.name(), result.name());
                    expResult.hashes.add(expResult.getNewHash(bi));
                    //assertEquals(expResult, result);
                }
                if (id.equals(result.name())) {
                    System.out.println("Found: " + result + " within file named " + id);
                } else if (!id.equals(result.name())) {
                    System.out.println("File: " + f.getName() + " did not match against expectd result: " + expResult.name() + ". got " + result.name());
                } else {
                    System.out.println("MISMATCH found: " + result + " within file named " + id);
                }

            } catch (IOException ex) {
                Logger.getLogger(ScreenDataTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        System.out.println(ScreenData.toProperties());
    }
}

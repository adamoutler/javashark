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
     * Test of identifyScreen method, of class ScreenData.
     */
    @Test
    public void testIdentifyScreen() {
        System.out.println("identifyScreen");

        for (File f : files) {

            try {
                System.out.println(f.getName());
                if (!f.getAbsolutePath().endsWith("svn")){
                    break;
                }
                BufferedImage bi = ImageIO.read(f);
                String id = f.getName().split("\\.", 2)[0];
                ScreenData expResult = ScreenData.valueOf(id);
                ScreenData result = ScreenData.identifyScreen(bi);
                if (expResult.getHashes().length > 0) {
                    HashedPicCoords hpc = expResult.getHashes()[0];
                    if (!result.equals(expResult)){
                        System.out.println("Expected values for "+ id+" are as follows");
                        for (int i=0; i<expResult.getHashes().length;i++){
                            HashedPicCoords coords =expResult.getHashes()[i];
                            System.out.println(i+": new HashedPicCoords("+coords.getX() + "," + coords.getY() + "," + coords.getW() + "," + coords.getH() + "," + coords.getHash(bi)+")");
                            
                        }
                    }
                    if (expResult.name().equals("ULTIMATEGARAGE")){
                        System.out.println(expResult);
                    }
                    assertEquals(expResult.name(),result.name());
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
    }


    private File getTestFile(ScreenData sd) {

        for (File f : files) {
            if (f.getName().startsWith(sd.name())) {
                return f;
            }
        }
        return null;
    }

}

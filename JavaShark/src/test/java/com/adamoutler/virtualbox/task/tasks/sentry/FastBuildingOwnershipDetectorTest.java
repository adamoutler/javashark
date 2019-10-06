/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.virtualbox.task.tasks.sentry.FastBuildingOwnershipDetector.BuildingBar;
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
public class FastBuildingOwnershipDetectorTest {

    public FastBuildingOwnershipDetectorTest() {
    }
    File f = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.png");

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
     * Test of getPercentOurCars method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void testGetPercentOurCars() {
        try {
            System.out.println("getPercentOurCars");
            FastBuildingOwnershipDetector.BuildingBar bar = BuildingBar.BAR1;
            BufferedImage bi = ImageIO.read(f);
            

            PicColors pc = new PicColors(bi);
            
            float expResult =  52.531647F;
            float result = FastBuildingOwnershipDetector.getPercentOurCars(bar, pc);
            assertEquals(expResult, result, 0.0);
            // TODO review the generated test code and remove the default call to fail.
        } catch (IOException ex) {
            Logger.getLogger(FastBuildingOwnershipDetectorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("The test case threw an error");
        }
    }

}

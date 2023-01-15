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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class FastBuildingOwnershipDetectorTest {

    public FastBuildingOwnershipDetectorTest() {
    }
    File f = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.png");
    File f2 = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGSBLOCKED.png");
    File f3 = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGSBLOCKED.1.png");
    File f4 = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGSBLOCKED.2.png");
    File f5 = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/RECONNECT.1.png");
    File f6 = new File("src/test/java/com/adamoutler/cats/catsobjects/resources/CITYKINGS.3.png");
    
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
            BufferedImage bi2 = ImageIO.read(f3);
            PicColors pc2 = new PicColors(bi2);
            float expResult2 =  54.43037796020508F;
            float result2 = FastBuildingOwnershipDetector.getPercentOurCars(bar, pc2);
            assertEquals(expResult2, result2, 0.0);
        } catch (IOException ex) {
            Logger.getLogger(FastBuildingOwnershipDetectorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("The test case threw an error");
        }
    }
    
    /**
     * Test of getPercentTheirCars method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void testGetPercentTheirCars() {
        try {
            System.out.println("getPercentTheirCars");
            FastBuildingOwnershipDetector.BuildingBar bar = BuildingBar.BAR4;
            BufferedImage bi = ImageIO.read(f);
            PicColors pc = new PicColors(bi);
            float expResult =  66.455696F;
            float result = FastBuildingOwnershipDetector.getPercentTheirCars(bar, pc);
            assertEquals(expResult, result, 0.0);
            BufferedImage bi2 = ImageIO.read(f3);
            PicColors pc2 = new PicColors(bi2);
            float expResult2 =  47.468357F;
            float result2 = FastBuildingOwnershipDetector.getPercentTheirCars(bar, pc2);
            assertEquals(expResult2, result2, 0.0);
        } catch (IOException ex) {
            Logger.getLogger(FastBuildingOwnershipDetectorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("The test case threw an error");
        }
    }
    
    /**
     * Test of getPercentNobodyCars method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void getPercentNobodyCars() {
        try {
            System.out.println("getPercentNobodyCars");
            // build nobody
            FastBuildingOwnershipDetector.BuildingBar bar = BuildingBar.BAR5;
            BufferedImage bi = ImageIO.read(f3);
            PicColors pc = new PicColors(bi);
            float expResult =  100.0F;
            float result = FastBuildingOwnershipDetector.getPercentNobodyCars(bar, pc);
            assertEquals(expResult, result, 0.0);
            
            // build nobody - red started
            bar = BuildingBar.BAR4;
            expResult =  52.531647F;
            result = FastBuildingOwnershipDetector.getPercentNobodyCars(bar, pc);
            assertEquals(expResult, result, 0.0);
            
            // build nobody - blue started
            FastBuildingOwnershipDetector.BuildingBar bar2 = BuildingBar.BAR4;
            BufferedImage bi2 = ImageIO.read(f2);
            PicColors pc2 = new PicColors(bi2);
            float expResult2 =  90.506325F;
            float result2 = FastBuildingOwnershipDetector.getPercentNobodyCars(bar2, pc2);
            assertEquals(expResult2, result2, 0.0);
        } catch (IOException ex) {
            Logger.getLogger(FastBuildingOwnershipDetectorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("The test case threw an error");
        }
    }
    
    /**
     * Test of getPercentOfCars method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void getPercentOfCars() {
        try {
            System.out.println("getPercentOfCars");
            // build nobody - blue/red started
            FastBuildingOwnershipDetector.BuildingBar bar3 = BuildingBar.BAR4;
            BufferedImage bi3 = ImageIO.read(f6);
            PicColors pc3 = new PicColors(bi3);
            float expResultNobody =  6.962025F;
            float expResultUs =  60.126583F;
            float expResultThem =  32.911392F;
            float expResultAll =  100.0F; 
            float resultNobody = FastBuildingOwnershipDetector.getPercentNobodyCars(bar3, pc3);
            float resultUs = FastBuildingOwnershipDetector.getPercentOurCars(bar3, pc3);
            float resultThem = FastBuildingOwnershipDetector.getPercentTheirCars(bar3, pc3);
            var resultAll = resultNobody+resultUs+resultThem;
            
            assertEquals(expResultNobody, resultNobody, 0.0);
            assertEquals(expResultUs, resultUs, 0.0);
            assertEquals(expResultThem, resultThem, 0.0);
            assertEquals(expResultAll, resultAll, 0.0);
        } catch (IOException ex) {
            Logger.getLogger(FastBuildingOwnershipDetectorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("The test case threw an error");
        }
    }
    
    /**
     * Test of testOwnershipB6Nobody method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void testOwnershipB6Nobody() throws IOException, Exception{
        BufferedImage bi2 = ImageIO.read(f2);
        OwnedBy[] owned=FastBuildingOwnershipDetector.scan(bi2);
        System.out.println("B6 should be NOBODY");
        var buildingNumber =1;
        for (OwnedBy building:owned){
            System.out.println("B" + buildingNumber + ": " + building);
            buildingNumber++;
        }
        assert (owned[5] == OwnedBy.NOBODY);
    }
    
    /**
     * Test of testOwnershipB6Them method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void testOwnershipB6Them() throws IOException, Exception{
        BufferedImage bi2 = ImageIO.read(f3);
        OwnedBy[] owned=FastBuildingOwnershipDetector.scan(bi2);
        System.out.println("B6 should be THEM");
        var buildingNumber =1;
        for (OwnedBy building:owned){
            System.out.println("B" + buildingNumber + ": " + building);
            buildingNumber++;
        }
        assert (owned[5] == OwnedBy.THEM);
    }
    
    /**
     * Test of testOwnershipB6Our method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void testOwnershipB6Our() throws IOException, Exception{
        BufferedImage bi2 = ImageIO.read(f4);
        OwnedBy[] owned = FastBuildingOwnershipDetector.scan(bi2);
        System.out.println("B6 should be US");
        var buildingNumber =1;
        for (OwnedBy building:owned){
            System.out.println("B" + buildingNumber + ": " + building);
            buildingNumber++;
        }
        assert (owned[5] == OwnedBy.US);
    }

    /**
     * Test of testRun method, of class FastBuildingOwnershipDetector.
     */
    @Test
    public void testOwnershipWhenReconnect() throws IOException, Exception{
        BufferedImage bi2 = ImageIO.read(f5);
        OwnedBy[] owned = FastBuildingOwnershipDetector.scan(bi2);
        System.out.println("Buildings should be null");
        assert (owned[0] == null);
        assert (owned[1] == null);
        assert (owned[2] == null);
        assert (owned[3] == null);
        assert (owned[4] == null);
        assert (owned[5] == null);
    }
}

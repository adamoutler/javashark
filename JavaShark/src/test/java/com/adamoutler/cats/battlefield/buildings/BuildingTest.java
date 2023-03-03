/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adamoutler.cats.battlefield.buildings;

import com.adamoutler.cats.battlefield.building.Building;
import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import com.adamoutler.imagerecognition.ByteImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author fabio
 */
public class BuildingTest {
    
    File fileBuilding1 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.1.png");
    File fileBuilding2 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.2.png");
    File fileBuilding3 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.3.png");
    File fileBuilding4 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.4.png");
    File fileBuilding5 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.5.png");
    File fileBuilding6 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.6.png");
    File fileBuilding7 = new File("src/test/java/com/adamoutler/cats/battlefield/buildings/resources/BUILDING.7.png");
    
    /**
     * Test of extract building information 1 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation1() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding1.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.US);
        assertEquals(building.getSlotsTotal(),  21);
        assertEquals(building.getSlotsOurs(),  11);
        assertEquals(building.getSlotsTheirs(),  0);
        assertEquals(building.getSlotsUnowned(),  10);
    }
    
    /**
     * Test of extract building information 2 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation2() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding2.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.THEM);
        assertEquals(building.getSlotsTotal(),  19);
        assertEquals(building.getSlotsOurs(),  8);
        assertEquals(building.getSlotsTheirs(),  11);
        assertEquals(building.getSlotsUnowned(),  0);
    }
    
    /**
     * Test of extract building information 3 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation3() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding3.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.THEM);
        assertEquals(building.getSlotsTotal(),  19);
        assertEquals(building.getSlotsOurs(),  0);
        assertEquals(building.getSlotsTheirs(),  10);
        assertEquals(building.getSlotsUnowned(),  9);
    }
    
    /**
     * Test of extract building information 4 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation4() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding4.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.US);
        assertEquals(building.getSlotsTotal(),  21);
        assertEquals(building.getSlotsOurs(),  11);
        assertEquals(building.getSlotsTheirs(),  1);
        assertEquals(building.getSlotsUnowned(),  9);
    }
    
    /**
     * Test of extract building information 5 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation5() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding5.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.US);
        assertEquals(building.getSlotsTotal(),  21);
        assertEquals(building.getSlotsOurs(),  11);
        assertEquals(building.getSlotsTheirs(),  0);
        assertEquals(building.getSlotsUnowned(),  10);
    }
    
    /**
     * Test of extract building information 6 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation6() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding6.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.US);
        assertEquals(building.getSlotsTotal(),  13);
        assertEquals(building.getSlotsOurs(),  7);
        assertEquals(building.getSlotsTheirs(),  0);
        assertEquals(building.getSlotsUnowned(),  6);
    }
    
    /**
     * Test of extract building information 7 setTosb method, of class Building.
     * @throws java.io.IOException
     */
    @Test
    public void testExtractBuildingInformation7() throws IOException { 
        Building building = new Building();
        BufferedImage bufferedImage = ImageIO.read(fileBuilding7.getAbsoluteFile());
        building.setTosb(new TeamOwnershipPercentageBar(ByteImage.getPngBytes(bufferedImage)));
        assertEquals(building.getBuildingOwnedBy(),  OwnedBy.US);
        assertEquals(building.getSlotsTotal(),  11);
        assertEquals(building.getSlotsOurs(),  6);
        assertEquals(building.getSlotsTheirs(),  0);
        assertEquals(building.getSlotsUnowned(),  5);
    }
}

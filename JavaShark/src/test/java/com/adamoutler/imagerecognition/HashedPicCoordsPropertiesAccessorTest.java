/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;
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
public class HashedPicCoordsPropertiesAccessorTest {

    public HashedPicCoordsPropertiesAccessorTest() {
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
     * Test of toPropertiesString method, of class PicCoordsWithHashes.
     */
    @Test
    public void testToPropertiesString() {
        System.out.println("toPropertiesString");
        String name = "foo";
        HashedPicCoordsPropertiesAccessor instance = new HashedPicCoordsPropertiesAccessor("416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986");
        String expResult = "416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986";
        String result = instance.toPropertiesString(name);
        assertEquals(expResult, result);

    }

    /**
     * Test of getPicCoords method, of class PicCoordsWithHashes.
     */
    @Test
    public void testGetPicCoords() {
        System.out.println("getPicCoords");
        String hash = "234234";
        HashedPicCoordsPropertiesAccessor instance = new HashedPicCoordsPropertiesAccessor("416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986");
        PicCoords expResult = new PicCoords(416, 130, 219, 81);
        PicCoords result = instance.getPicCoords();
        assertEquals(expResult, result);

    }

    /**
     * Test of getHpcl method, of class PicCoordsWithHashes.
     */
    @Test
    public void testGetHpcl() {
        System.out.println("getHpcl");
        HashedPicCoordsPropertiesAccessor instance = new HashedPicCoordsPropertiesAccessor("416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986");

        ArrayList<HashedPicCoords> expResult = new ArrayList<>();
        expResult.add(new HashedPicCoords(416, 130, 219, 81, 565959893));
        expResult.add(new HashedPicCoords(416, 130, 219, 81, 526377387));
        expResult.add(new HashedPicCoords(416, 130, 219, 81, -131898965));
        expResult.add(new HashedPicCoords(416, 130, 219, 81, -1607526986));

        ArrayList<HashedPicCoords> result = instance.getHpcl();
        assertEquals(expResult, result);

    }

    /**
     * Test of getHashes method, of class PicCoordsWithHashes.
     */
    @Test
    public void testGetHashes() {
        System.out.println("getHashes");
        HashedPicCoordsPropertiesAccessor instance = new HashedPicCoordsPropertiesAccessor("416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986");
        int[] expResult = new int[]{565959893, 526377387, -131898965, -1607526986};
        int[] result = instance.getHashes();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getName method, of class PicCoordsWithHashes.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "adsfasdfasdfzxcvzxcvrwet23654";
        HashedPicCoordsPropertiesAccessor instance = new HashedPicCoordsPropertiesAccessor(expResult, "416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986");
        String result = instance.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setName method, of class PicCoordsWithHashes.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "testingname";
        HashedPicCoordsPropertiesAccessor instance = new HashedPicCoordsPropertiesAccessor("ajdsflakjsd0qwer", "416, 130, 219, 81, 565959893, 526377387, -131898965, -1607526986");;
        instance.setName(name);
    }

    @Test
    public void testInAction() {
        ArrayList al=screens.BOOTED.hashes.getHpcl();
        System.out.println(al);
        screens.BOOTED.hashes.getName();
        screens.BOOTED.hashes.getPicCoords();
        screens.BOOTED.hashes.getHpcl();
        
                

    }
    static Properties props=new Properties();
    static {
        try {
            String hashprop = "BOOTED=200, 200, 29, 29, 1020915441, 321694902\n"
                    + "CITYKINGCAR2=125, 500, 30, 50, 139952256\n"
                    + "CITYKINGCAR1=50, 500, 30, 50, -2124867224\n"
                    + "CHAMPIONSHIPGARAGE=26, 594, 260, 11";
            StringReader sr = new StringReader(hashprop);
            props.load(sr);
            System.out.println("");
        } catch (IOException ex) {
            fail("IOException encountered while loading properties");
            Logger.getLogger(HashedPicCoordsPropertiesAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    enum screens {

        BOOTED(new HashedPicCoordsPropertiesAccessor(), "foo"),
        CITYKINGSCAR1(new HashedPicCoordsPropertiesAccessor(), "foo2");

        
        HashedPicCoordsPropertiesAccessor hashes;
        String description;
        screens(HashedPicCoordsPropertiesAccessor hashes, String description) {
            this.description=description;
            hashes.init(this.name(),props);
            this.hashes=hashes;
        }
    }


}

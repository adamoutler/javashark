/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.babysharks.datastructures.Gangster;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class ReportToolsTest {

    public ReportToolsTest() {
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
    String name = "https://javashark.baby-sharks.com/excel/1556476463787-development-AdamOutler-ed7c7a7f-d278-4642-a69f-a6b27887254a.xlsx";
    String repo="src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/";
    String gangName = "staging";
    /**
     * Test of getReport method, of class ReportTools.
     */
    @Test
    public void testGetReport() throws Exception {
        System.out.println("getReport");
        ReportTools expResult = null;
        ReportTools result = ReportTools.getReport(gangName, repo, name);
       // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getGangster method, of class ReportTools.
     */
    @Test
    public void testGetGangster() {
        System.out.println("getGangster");
        String[] nameHashes = null;
        ReportTools instance = null;
        Gangster expResult = null;
      //  Gangster result = instance.getGangster(nameHashes);

    }

    /**
     * Test of getUserBadgePaneFromUserHash method, of class ReportTools.
     */
    @Test
    public void testGetUserBadgePaneFromUserHash() {
        System.out.println("getUserBadgePaneFromUserHash");
        String[] nameHashes = null;
        ReportTools instance = null;
        Gangster expResult = null;
    //    Gangster result = instance.getUserBadgePaneFromUserHash(nameHashes);

    }

    /**
     * Test of getUserBadgesFromHashes method, of class ReportTools.
     */
    @Test
    public void testGetUserBadgesFromHashes() {
        System.out.println("getUserBadgesFromHashes");
        String[] nameHashes = null;
        ReportTools instance = null;
        Gangster expResult = null;
     //   Gangster result = instance.getUserBadgesFromHashes(nameHashes);

    }

}

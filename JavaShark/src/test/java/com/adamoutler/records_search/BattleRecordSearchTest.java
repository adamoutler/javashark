/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search;

import java.io.File;
import java.util.List;
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
public class BattleRecordSearchTest {

    public BattleRecordSearchTest() {
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

    final static String SEARCHFOLDER = "src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/";

    /**
     * Test of setGang method, of class BattleRecordSearch.
     */
    @Test
    public void testSetGang() {
        System.out.println("setGang");
        String gang = "development";
        BattleRecordSearch instance = new BattleRecordSearch().setOpponentGangOCRName("Test");
        instance.setFolder(SEARCHFOLDER).setExtension("json");
        String expResult = "scan-1560729059379-development-AdamOutler-65f0517a-2b54-42c6-ac42-10ca6dcef67dOCRName-Test.json";
        List<File> result = instance.setGang(gang).search();
        assert (result.size() > 0);
        assertEquals(expResult, result.get(0).getName());

    }

}

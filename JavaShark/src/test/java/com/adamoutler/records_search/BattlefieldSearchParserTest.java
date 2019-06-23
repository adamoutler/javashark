/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
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
public class BattlefieldSearchParserTest {

    public BattlefieldSearchParserTest() {
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

    String folder = "src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/";
    String fileExtension = "json";
    String gang = "development";
    String opponentName = "";

    /**
     * Test of getRecords method, of class BattlefieldSearchParser.
     *
     * @return list of files
     * @throws java.lang.Exception when issue is encountered.
     */
    @Test
    public void testGetRecords() throws Exception {
        System.out.println("getRecords");
        BattlefieldSearchParser instance = new BattlefieldSearchParser();
        List<File> result = instance.getRecords(folder, fileExtension, gang, opponentName);
        assert (result.size() > 2);

    }

    /**
     * Test of findOneRecordWithinTemporalFieldForEachDate method, of class
     * BattlefieldSearchParser.
     */
    @Test
    public void testFindOneRecordForEachDate() throws Exception {
        System.out.println("findOneRecordWithinTemporalFieldForEachDate");
        List<File> files = new BattlefieldSearchParser().getRecords(folder, fileExtension, gang, opponentName);
        Date[] dates=new Date[]{new DateTime(2019,6,22,0,0).toDate()};
        BattlefieldSearchParser instance = new BattlefieldSearchParser();
        
        Map<Date, File> result = instance.findOneRecordForEachDate(files, dates);
        String expResult="recon-1561226780585-development-AdamOutler-3b78f06a-0821-486f-a176-fa579af8859aOCRName-E9557;.json";
        assertEquals(expResult, result.get((Date)result.keySet().toArray()[0]).getName());

        dates=new Date[]{new DateTime(2019,6,17,0,0).toDate()};
        result = instance.findOneRecordForEachDate(files, dates);
        expResult="scan-1560729059379-development-AdamOutler-65f0517a-2b54-42c6-ac42-10ca6dcef67dOCRName-Test.json";
        assertEquals(expResult, result.get((Date)result.keySet().toArray()[0]).getName());
        
        dates=new Date[]{new DateTime(2019,6,23,19,0).toDate()};
        result = instance.findOneRecordForEachDate(files, dates);
        expResult="recon-1561079896895-development-AdamOutler-2f184fb4-fafc-467e-bf7f-2986ec96c271OCRName-5531655158.json";
        assertEquals(expResult, result.get((Date)result.keySet().toArray()[0]).getName());
        
        
        
    }

}

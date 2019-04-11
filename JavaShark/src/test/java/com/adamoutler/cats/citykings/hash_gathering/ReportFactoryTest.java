/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

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
public class ReportFactoryTest {
    
    public ReportFactoryTest() {
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
     * Test of getReport method, of class ReportFactory.
     */
    @Test
    public void testGetReport() throws Exception {
        System.out.println("getReport");
        String filename = "https://javashark.baby-sharks.com/excel/1554671457358-staging-AdamOutler-b460f0c2-cd56-4d91-a9e6-29e8ced0b3fe.xlsx";
        ReportFactory instance = new ReportFactory();
        instance.setRepoFolder("src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/");
        CityKingsMatchRaw expResult = null;
        CityKingsMatchRaw result = instance.getReport(filename);
       assert (result.getBuildings().size()==6);
       
       
    }

    /**
     * Test of getRepoFolder method, of class ReportFactory.
     */
    @Test
    public void testGetRepoFolder() {
        System.out.println("getRepoFolder");
        ReportFactory instance = new ReportFactory();
        String expResult = "/var/www/baby-sharks/excel/";
        String result = instance.getRepoFolder();
        assertEquals(expResult, result);

    }

    /**
     * Test of setRepoFolder method, of class ReportFactory.
     */
    @Test
    public void testSetRepoFolder() {
        System.out.println("setRepoFolder");
        String repoFolder = "test";
        ReportFactory instance = new ReportFactory();
        instance.setRepoFolder(repoFolder);
        assertEquals(repoFolder, instance.getRepoFolder());
    }
    
}

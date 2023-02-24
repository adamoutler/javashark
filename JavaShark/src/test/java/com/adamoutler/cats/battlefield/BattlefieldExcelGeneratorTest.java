/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class BattlefieldExcelGeneratorTest {

    public BattlefieldExcelGeneratorTest() {
        detailReport = new File("src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/scan-1560715362325-development-AdamOutler-dfb3c7cd-69ba-417b-947f-8cc6aea5490eOCRName-Test");
    }

    @BeforeClass
    public static void setUpClass() {

    }

    final File detailReport;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public Battlefield testSomeMethod() {
        try {
            FileReader fr = new FileReader(detailReport);
            System.out.println(detailReport.getAbsolutePath() + " " + detailReport.exists());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Battlefield match = gson.fromJson(fr, Battlefield.class);
            // new BattlefieldExcelGenerator().createOutputFiles(detailReport, match, true);
            return match;

        } catch (IOException ex) {
            Logger.getLogger(BattlefieldExcelGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Test of createExcelFile method, of class CityKingsJsonOutput.
     */
    @Test
    public void testCreateExcelFile() throws Exception {        
        System.out.println("createExcelFile");
        File f = File.createTempFile("excel", ".xls");
        Battlefield match = testSomeMethod();
        BattlefieldExcelGenerator instance = new BattlefieldExcelGenerator();
        String result = instance.createOutputFiles(f, match, false);
        if (!GraphicsEnvironment.isHeadless()) {
            instance.openSheet(f);
        }
    }
}
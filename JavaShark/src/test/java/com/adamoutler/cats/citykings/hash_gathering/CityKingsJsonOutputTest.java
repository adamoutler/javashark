/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

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
public class CityKingsJsonOutputTest {

    public CityKingsJsonOutputTest() {
        detailReport = new File("src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/1556476463787-development-AdamOutler-ed7c7a7f-d278-4642-a69f-a6b27887254a");

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

    
    public CityKingsMatchRaw testSomeMethod() {
        try {
            FileReader fr=new FileReader(detailReport);
            System.out.println(detailReport.getAbsolutePath()+" "+ detailReport.exists());
            Gson gson=new GsonBuilder().setPrettyPrinting().create();
            CityKingsMatchRaw match=gson.fromJson(fr, CityKingsMatchRaw.class);
            return match;
            
        } catch (IOException ex) {
            Logger.getLogger(CityKingsJsonOutputTest.class.getName()).log(Level.SEVERE, null, ex);
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
        CityKingsMatchRaw match = testSomeMethod();
        CityKingsJsonOutput instance = new CityKingsJsonOutput();
        String result = instance.createOutputFiles(f, match);
        if (!GraphicsEnvironment.isHeadless()){
            instance.openSheet(f);
        }

    }

}

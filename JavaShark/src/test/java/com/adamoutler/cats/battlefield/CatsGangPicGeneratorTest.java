/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class CatsGangPicGeneratorTest {

    private final File detailReport;
    private final File detailReport2;

    public CatsGangPicGeneratorTest() {
        detailReport = new File("src/test/java/com/adamoutler/cats/screen_id/resources/recon-1561839526047-development-AdamOutler-ab44b7ec-4eaf-40f5-aadb-24e044061064OCRName-22555-573573-");
        detailReport2 = new File("src/test/java/com/adamoutler/cats/screen_id/resources/recon-1561821584807-staging-AdamOutler-4afbb70a-b348-4ea5-9801-400f2e5455e6OCRName-mesa-25w");

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
     * Test of organizePictures method, of class BattlefieldExcelGenerator.
     */
    @Test
    public void testOrganizePictures() {
        System.out.println("organizePictures");
        Battlefield battlefield = testSomeMethod(detailReport);
        Battlefield battlefield2 = testSomeMethod(detailReport2);
        BufferedImage result = CatsGangPicGenerator.organizePictures(battlefield, battlefield2);
        File f = null;
        try {
            f = File.createTempFile("file3", ".png");
            ImageIO.write(result, "PNG", new FileOutputStream(f));
        } catch (IOException ex) {
            Logger.getLogger(BattlefieldExcelGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!GraphicsEnvironment.isHeadless()) {
            try {
                openSheet(f);
            } catch (IOException ex) {
                Logger.getLogger(BattlefieldExcelGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Test
    public void testOrganizePictures2() {
        System.out.println("organizePictures");
        Battlefield battlefield = testSomeMethod(detailReport);
        Battlefield battlefield2 = testSomeMethod(detailReport2);
        BufferedImage result = CatsGangPicGenerator.organizePictures(battlefield2, battlefield);
        File f = null;
        try {
            f = File.createTempFile("file3", ".png");
            ImageIO.write(result, "PNG", new FileOutputStream(f));
        } catch (IOException ex) {
            Logger.getLogger(BattlefieldExcelGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!GraphicsEnvironment.isHeadless()) {
            try {
                openSheet(f);
            } catch (IOException ex) {
                Logger.getLogger(BattlefieldExcelGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void openSheet(File f) throws IOException {
        Desktop d = Desktop.getDesktop();
        d.open(f);
    }

    public Battlefield testSomeMethod(File f) {
        try {
            FileReader fr = new FileReader(f);
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

}

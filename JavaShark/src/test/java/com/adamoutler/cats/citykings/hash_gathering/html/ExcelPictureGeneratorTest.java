/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class ExcelPictureGeneratorTest {

    public ExcelPictureGeneratorTest() {
    }

    File f = new File("src/test/java/com/adamoutler/cats/citykings/hash_gathering/resources/1554671457358-staging-AdamOutler-b460f0c2-cd56-4d91-a9e6-29e8ced0b3fe.xls");

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

    @Test
    public void testSomeMethod() {
        try {
            String s;
            //s=ExcelToHTML.excel2Html(f);

            //System.out.println(s);
            HSSFWorkbook workbook = (HSSFWorkbook) new HSSFWorkbookFactory().create(new FileInputStream(f));

            ToHtml.main(f.getAbsolutePath(), "a/file.html");

            s = new ExcelPictureGenerator().toHTML(workbook);
            System.out.println(s);
        } catch (IOException | EncryptedDocumentException ex) {
            Logger.getLogger(ExcelPictureGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ExcelPictureGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

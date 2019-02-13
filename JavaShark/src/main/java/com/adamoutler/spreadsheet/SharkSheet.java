/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.spreadsheet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author adamo
 */
public class SharkSheet {
   transient Sheet sheet;
    String[] times=new String[]{"12am","1am","2am","3am","4am","5am","6am","7am","8am","9am","10am","11am","12pm","1pm","2pm","3pm","4pm","5pm","6pm","7pm","8pm","9pm","10pm","11pm"};
    ArrayList<TimeUser> users=new ArrayList<>();
    SharkSheet(File f){
        try {
            Workbook workbook = WorkbookFactory.create(f);
            
            this.sheet=workbook.getSheetAt(0);
            parseSheet();
        } catch (IOException ex) {
            Logger.getLogger(SharkSheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(SharkSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void parseSheet() {
        
        for (int i=0; i<sheet.getLastRowNum(); i++){
            Row row=sheet.getRow(i);
            try {
            if (row.getCell(0).toString().isEmpty()){
                continue;
            } else {
                String s=row.getCell(0).getStringCellValue();
                users.add(new TimeUser(row));
            }
            } catch (NullPointerException ex){
                System.out.println("Null pointer on line " + i);
                
            }
            
        }
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        
        System.out.println(gson.toJson(this));
        
    }

    private Object GsonBuilder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.html;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author adamo
 */
public class ExcelPictureGenerator {
    HSSFSheet sheet;
    
   public String toHTML(HSSFWorkbook wb){
       StringBuffer app=new StringBuffer();
       ToHtml.create(wb, app);
       
       
       return "";
       
   }
}

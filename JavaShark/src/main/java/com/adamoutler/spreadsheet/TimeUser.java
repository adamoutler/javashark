/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.spreadsheet;

import java.util.HashMap;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author adamo
 */
public class TimeUser {

    String name;
    float utcOffset;
    HashMap<Integer,String> times = new HashMap<>();
    transient Row row;

    public TimeUser(Row row) {
        this.row = row;
        parseRow();
    }

    /**
     * here we parse the Name, UTC Offset and the value within each cell into the hashmap
     */
    private void parseRow() {
        for (int i = 0; i <= 25; i++) {
            switch (i) {
                case 0:
                    name = row.getCell(i).getStringCellValue();
                    break;
                case 1:
                    String uoffset = getString(i).split(" ")[1].replace(",", ".").replace("+", "");
                    utcOffset = Float.parseFloat(uoffset);
                    break;
                default:
                    times.put(i-2, getString(i));
            }
        }
    }

    private String getString(int i) {
        return row.getCell(i).getStringCellValue();
    }

}

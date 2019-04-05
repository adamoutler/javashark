/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author adamo
 */
public class CityKingsJsonOutput {

    public CityKingsJsonOutput() {

    }

    public String createExcelFile(File f, CityKingsMatchRaw match) throws IOException {
        HSSFWorkbook workbook = HSSFWorkbookFactory.createWorkbook();
        HSSFSheet carsByBuilding = createCarsByBuildingSheet(workbook, match);
        createCarsByHashSheet(workbook, match);

        writeSheet(f, workbook);
        return "";
    }

    private void createCarsByHashSheet(HSSFWorkbook workbook, CityKingsMatchRaw match) throws IOException {
        HSSFSheet carsByHash = workbook.createSheet("Cars By User");
        
        HashMap<String, ArrayList<CityKingsCarRaw>> hm = getCarHashmapByHash(match);
        Row header = carsByHash.createRow(0);
        header.createCell(0).setCellValue("Car 1");
        header.createCell(1).setCellValue("Car 2");
        header.createCell(2).setCellValue("Car 3");
        header.createCell(3).setCellValue("hash value");
        header.createCell(4).setCellValue("Telegram Username");
        carsByHash.setColumnWidth(3, 2500);
        carsByHash.setColumnWidth(4, 6000);
        int row = 1;
        for (Entry<String, ArrayList<CityKingsCarRaw>> entry : hm.entrySet()) {
            int carPos = 0;
            for (CityKingsCarRaw car : entry.getValue()) {
                writePictureTo(workbook, carsByHash, carPos, row, car.getCarJpg());
                carPos++;
            }
            carsByHash.getRow(row).createCell(3).setCellValue(entry.getKey());
            row++;
        }
    }

    private HashMap<String, ArrayList<CityKingsCarRaw>> getCarHashmapByHash(CityKingsMatchRaw match) {
        HashMap<String, ArrayList<CityKingsCarRaw>> hm = new HashMap();
        for (CityKingsBuildingRaw b : match.getBuildings()) {
            for (CityKingsCarRaw c : b.getCars()) {
                ArrayList<CityKingsCarRaw> cars = hm.get(c.getNameHash());
                if (cars == null) {
                    cars = new ArrayList<>();
                }
                cars.add(c);
                hm.put(c.getNameHash(), cars);
            }
        }
        return hm;
    }

    private HSSFSheet createCarsByBuildingSheet(HSSFWorkbook workbook, CityKingsMatchRaw match) throws IOException {
        HSSFSheet sheet = workbook.createSheet("Cars By Building");
        for (int building = 0; building < 6; building++) {
            System.out.println();
            int buildingLabelNumber = building * 6;
            sheet.createRow(buildingLabelNumber).createCell(0).setCellValue("Cars in Building " + (building + 1));

            int buildingRow = buildingLabelNumber;

            for (CityKingsCarRaw car : match.getBuildings().get(building).getCars()) {
                writePictureTo(workbook, sheet, car.getColumn(), car.getRow() + buildingRow, car.getCarJpg());
            }

        }
        return sheet;
    }

    private void parseSheet(HSSFSheet s, CityKingsMatchRaw match) {

    }

    private void writePictureTo(Workbook wb, HSSFSheet sheet, int x, int y, byte[] bi) throws IOException {
        if (sheet.getRow(y) == null) {
            sheet.createRow(y);
        }
        if (sheet.getRow(y + 1) == null) {
            sheet.createRow(y + 1);
        }
        ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bi);
        //ImageIO.write(bi, "jpg", baos);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) x, y, (short) (x + 1), y + 1);
        sheet.getRow(y).setHeight((short) 1500);
        sheet.setColumnWidth(x, 4000);
        anchor.setAnchorType(AnchorType.DONT_MOVE_DO_RESIZE);
        patriarch.createPicture(anchor, wb.addPicture(baos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

    }

    private void writeSheet(File f, Workbook wb) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(f)) {
            wb.write(fos);
        }
        System.out.println("Wrote Excel Sheet to: " + f.getAbsolutePath());
    }

    public void openSheet(File f) throws IOException {
        Desktop d = Desktop.getDesktop();
        d.open(f);
    }

}

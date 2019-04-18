/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author adamo
 */
public class CityKingsJsonOutput {

    CellStyle largeStyle;

    public CityKingsJsonOutput() {

    }

    public String createOutputFiles(File f, CityKingsMatchRaw match) throws IOException {
        HSSFWorkbook workbook = HSSFWorkbookFactory.createWorkbook();
        createStyles(workbook);
        HSSFSheet buildingStatistics = createStatisticsPage(workbook, match);
        HSSFSheet carsByBuilding = createCarsByBuildingSheet(workbook, match);
        HSSFSheet carsByHash = createCarsByHashSheet(workbook, match);

        writeSheet(f, workbook);
        return "";
    }

    private HSSFSheet createCarsByHashSheet(HSSFWorkbook workbook, CityKingsMatchRaw match) throws IOException {
        HSSFSheet carsByHash = workbook.createSheet("Cars By User");

        HashMap<String, ArrayList<CityKingsCarRaw>> hm = getCarHashmapByHash(match);
        Row header = carsByHash.createRow(0);
        header.createCell(0).setCellValue("Car 1");
        header.createCell(1).setCellValue("Car 2");
        header.createCell(2).setCellValue("Car 3");
        header.createCell(3).setCellValue("hash value");
        header.createCell(4).setCellValue("Papa Command");
        header.createCell(5).setCellValue("Telegram Username");
        carsByHash.setColumnWidth(3, 2500);
        carsByHash.setColumnWidth(4, 6000);
        carsByHash.setColumnWidth(4, 7000);
        int row = 1;
        for (Entry<String, ArrayList<CityKingsCarRaw>> entry : hm.entrySet()) {
            int carPos = 0;
            for (CityKingsCarRaw car : entry.getValue()) {
                writePictureTo(workbook, carsByHash, carPos, row, car.getCarJpg());
                carPos++;
            }
            carsByHash.getRow(row).createCell(3).setCellValue(entry.getKey());
            carsByHash.getRow(row).createCell(4).setCellValue("/hashes " + entry.getKey() + " ");
            carsByHash.getRow(row).createCell(5).setCellValue(" ");
            row++;

        }
        return carsByHash;
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

    private void createStyles(HSSFWorkbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 50);
        font.setFontName("Helvetica");
        largeStyle = wb.createCellStyle();
        largeStyle.setFont(font);
    }

    private HSSFSheet createCarsByBuildingSheet(HSSFWorkbook workbook, CityKingsMatchRaw match) throws IOException {
        HSSFSheet sheet = workbook.createSheet("Cars By Building");

        for (int building = 0; building < 6; building++) {
            System.out.println();
            int buildingLabelNumber = building * 6;
            sheet.createRow(buildingLabelNumber);
            sheet.createRow(buildingLabelNumber + 1);

            Cell c = sheet.createRow(buildingLabelNumber + 2).createCell(0);
            c.setCellValue((building + 1));
            c.setCellStyle(largeStyle);

            int buildingRow = buildingLabelNumber;
            int length = match.getBuildings().get(building).getCars().size();
            int totalRows = (int) Math.ceil(length / 3) + 2;
            if (length % 3 == 0) {
                totalRows--;
            }

            for (CityKingsCarRaw car : match.getBuildings().get(building).getCars()) {
                writePictureTo(workbook, sheet, totalRows - car.getColumn(), car.getRow() + buildingRow, car.getCarJpg());
            }

        }
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Building");
        header.createCell(1).setCellValue("     Car Locations (Accuracy is affected by movement)");
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

    private HSSFSheet createStatisticsPage(HSSFWorkbook workbook, CityKingsMatchRaw match) {
        HSSFSheet sheet = workbook.createSheet("Statistics");
        int i = 0;
        String title = "Buildings owned by us";
        String us = "";
        String them = "";
        String nobody = "";
        int ourCarsTotal = 0;
        int theirCarsTotal = 0;
        int availableSpots = 0;
        int totalSpots = 0;
        int gainingUs = 0;
        int gainingThem = 0;
        int gainingNobody = 0;
        LinkedList<Integer> buildingsUs = new LinkedList();
        LinkedList<Integer> buildingsThem = new LinkedList();
        LinkedList<Integer> buildingsNobody = new LinkedList();
        LinkedList<Boolean> linked = new LinkedList<>();
        for (CityKingsBuildingRaw building : match.getBuildings()) {
            linked.add(building.isIsLinked());
            if (null != building.getBuildingOwnedBy()) switch (building.getBuildingOwnedBy()) {
                case US:
                    gainingUs = gainingUs + (building.isIsLinked() ? building.getSlotsTotal() * 2 : building.getSlotsTotal());
                    break;
                case THEM:
                    gainingThem = gainingThem + (building.isIsLinked() ? building.getSlotsTotal() * 2 : building.getSlotsTotal());
                    break;
                case NOBODY:
                    gainingNobody = gainingNobody + (building.isIsLinked() ? building.getSlotsTotal() * 2 : building.getSlotsTotal());
                    break;
                default:
                    break;
            }
            buildingsNobody.add(building.getSlotsUnowned());
            availableSpots = building.getSlotsUnowned() + availableSpots;
            buildingsUs.add(building.getSlotsOurs());
            ourCarsTotal = ourCarsTotal + building.getSlotsOurs();
            buildingsThem.add(building.getSlotsTheirs());
            theirCarsTotal = theirCarsTotal + building.getSlotsTheirs();
            totalSpots = totalSpots + building.getSlotsTotal();

            switch (building.getBuildingOwnedBy()) {
                case US:
                    us = us + building.getBuildingNumber() + " ";
                    break;
                case THEM:
                    them = them + building.getBuildingNumber() + " ";
                    break;
                case NOBODY:
                    nobody = nobody + building.getBuildingNumber() + " ";

            }
        }
        Row r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Overall Statistics");
        i++;

        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Owned Buildings");
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Us:");
        r.createCell(1).setCellValue(us.length() == 0 ? "-" : us);
        r.createCell(2).setCellValue("Them:");
        r.createCell(3).setCellValue(them.length() == 0 ? "-" : them);
        r.createCell(4).setCellValue("Nobody:");
        r.createCell(5).setCellValue(nobody.length() == 0 ? "-" : nobody);

        //do points values here.
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Potential Points:");
        r = sheet.createRow(i++);
         r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(" +" + (gainingUs+gainingThem+gainingNobody));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(" +" + gainingUs);
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(" +" + gainingThem);
        r.createCell(6).setCellValue("Available:");
        r.createCell(7).setCellValue(" +" + gainingNobody);
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total Slots");

        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(totalSpots);
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(ourCarsTotal);
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(theirCarsTotal);
        r.createCell(6).setCellValue("Available:");
        r.createCell(7).setCellValue(availableSpots);

        i++;
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Building Breakdown");
        r = sheet.createRow(i++);
        int building = 0;
        r.createCell(0).setCellValue("Building " + (building + 1));
        r.createCell(2).setCellValue(linked.get(building) == true ? "Linked" : "");
        r.createCell(3).setCellValue("value:");
        r.createCell(4).setCellValue(linked.get(building) == true ? (buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building)) * 2 : buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(buildingsUs.get(building));
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(buildingsThem.get(building));
        r.createCell(6).setCellValue("Nobody:");
        r.createCell(7).setCellValue(buildingsNobody.get(building++));
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Building " + (building + 1));
        r.createCell(2).setCellValue(linked.get(building) == true ? "Linked" : "");
        r.createCell(3).setCellValue("value:");
        r.createCell(4).setCellValue(linked.get(building) == true ? (buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building)) * 2 : buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));

        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(buildingsUs.get(building));
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(buildingsThem.get(building));
        r.createCell(6).setCellValue("Nobody:");
        r.createCell(7).setCellValue(buildingsNobody.get(building++));
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Building " + (building + 1));
        r.createCell(2).setCellValue(linked.get(building) == true ? "Linked" : "");
        r.createCell(3).setCellValue("value:");
        r.createCell(4).setCellValue(linked.get(building) == true ? (buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building)) * 2 : buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(buildingsUs.get(building));
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(buildingsThem.get(building));
        r.createCell(6).setCellValue("Nobody:");
        r.createCell(7).setCellValue(buildingsNobody.get(building++));
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Building " + (building + 1));
        r.createCell(2).setCellValue(linked.get(building) == true ? "Linked" : "");
        r.createCell(3).setCellValue("value:");
        r.createCell(4).setCellValue(linked.get(building) == true ? (buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building)) * 2 : buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(buildingsUs.get(building));
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(buildingsThem.get(building));
        r.createCell(6).setCellValue("Nobody:");
        r.createCell(7).setCellValue(buildingsNobody.get(building++));
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Building " + (building + 1));
        r.createCell(2).setCellValue(linked.get(building) == true ? "Linked" : "");
        r.createCell(3).setCellValue("value:");
        r.createCell(4).setCellValue(linked.get(building) == true ? (buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building)) * 2 : buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(buildingsUs.get(building));
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(buildingsThem.get(building));
        r.createCell(6).setCellValue("Nobody:");
        r.createCell(7).setCellValue(buildingsNobody.get(building++));
        i++;
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Building " + (building + 1));
        r.createCell(2).setCellValue(linked.get(building) == true ? "Linked" : "");
        r.createCell(3).setCellValue("value:");
        r.createCell(4).setCellValue(linked.get(building) == true ? (buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building)) * 2 : buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r = sheet.createRow(i++);
        r.createCell(0).setCellValue("Total:");
        r.createCell(1).setCellValue(buildingsUs.get(building) + buildingsThem.get(building) + buildingsNobody.get(building));
        r.createCell(2).setCellValue("Us:");
        r.createCell(3).setCellValue(buildingsUs.get(building));
        r.createCell(4).setCellValue("Them:");
        r.createCell(5).setCellValue(buildingsThem.get(building));
        r.createCell(6).setCellValue("Nobody:");
        r.createCell(7).setCellValue(buildingsNobody.get(building++));
        i++;

        return sheet;
    }

}

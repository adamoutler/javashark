
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.BuildingFactory;
import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.imagerecognition.OCR;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CityKingsMatchRaw {

    private final long time = System.currentTimeMillis();
    String ourScore="";
    String ourCurrentlyGaining="";
    String theirScore="";
    String theirCurrentlyGaining="";
    String targetScore="";
    private LinkedList<CityKingsBuildingRaw> buildings;
    byte[] screenshot;
    private String excelFile = "";
    private String jsonFile = "";
    transient public final static PicCoords OURSCORECOORDS=new PicCoords(150,135,244,45);
    transient public final static PicCoords THEIRSCORECOORDS=new PicCoords(632,135,244,45);
    transient public final static PicCoords TARGETSCORECOORDS=new PicCoords(447,149,141,38);
    
    transient private BufferedImage boardScreenshot;
    transient private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void main(String[] args) { //main method
        //      JadbDevice jadb = new se.vidstige.jadb.JadbConnection().getAnyDevice();
//        new CityKingsMatchRaw().scanBuildings(jadb);
    }

    public CityKingsMatchRaw scanBuildings(VirtualBoxMachine vm) {

        try {

            //CityKingsScreen.navigateTo(jadb);
            BufferedImage bi = vm.screenshot();
            Screenshottable.writePic(bi, "PictureUsedForCreateBuildings");
            setBuildings(BuildingFactory.createBuildings(vm));

            getBuildings().stream().map((building) -> {
                return building;
            }).forEachOrdered((building) -> {
                System.out.println(building);
                if (building.getOwnershipBar().getTotalSlots() < 3) {
                    System.err.println("Failure to acquire building");
                    System.out.println(building.toString());
                }
                building.getCars().forEach((car) -> {
                    car.convertCarPicToJpg();
                });
                boardScreenshot=building.getMapScreenshot();
            });
            
            
            OCR ocr=new OCR();
            ocr.setTesseractPath(ocr.tesseractWindows);
            targetScore=ocr.discern(TARGETSCORECOORDS.getImageAtCoords(boardScreenshot)).trim();
            ourScore=ocr.discern(OURSCORECOORDS.getImageAtCoords(bi));
            theirScore=ocr.discern(THEIRSCORECOORDS.getImageAtCoords(bi));
            //ourScore=ocr.discern(targetScoreCoords.getImageAtCoords(boardScreenshot)).trim();
            
            
            
            vm.setTaskComplete(true);
            
            String filename = System.currentTimeMillis() +"-"+ vm.getGang()+"-"+vm.getUsername()+"-" + UUID.randomUUID().toString();
            this.excelFile = writeToWebServer(filename);
            writeLocalJsonFile("/var/www/baby-sharks/excel/"+filename);

            return this;
        } catch (IOException ex) {
            Logger.getLogger(CityKingsMatchRaw.class.getName()).log(Level.SEVERE, null, ex);
        }
        vm.setTaskComplete(true);
        return null;
    }

    private void writeLocalJsonFile(String file) throws IOException, JsonIOException {
        String outputFile = file;
        System.out.println("Writing " + outputFile);
        try (FileWriter fw = new FileWriter(outputFile)) {
            gson.toJson(this, fw);
            fw.flush();
        }
    }

    public void writeJson(File f) throws IOException{
        try (FileWriter fw = new FileWriter(f.getAbsolutePath())) {
            gson.toJson(this, fw);
            fw.flush();
        }
        
    }
    
    private String writeToWebServer(String outputUUID) throws IOException {
        String outputFolder = "/var/www/baby-sharks/excel/";
        File folder=new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File f = new File(folder.getAbsolutePath() + "/" + outputUUID+".xls");
        new CityKingsJsonOutput().createOutputFiles(f, this);
        writeJson(new File(outputFolder+outputUUID+".json"));
        this.jsonFile=f.getAbsolutePath();
        this.excelFile="https://javashark.baby-sharks.com/"+outputUUID+".xls";
        return f.getName();
    }

    public void clearScreenshots() {
        this.screenshot = null;
        for (CityKingsBuildingRaw building : this.getBuildings()) {
            building.clearBuildingPics();
            for (CityKingsCarRaw car : building.getCars()) {
                car.clearCarScreenshot();
            }
        }
    }


    /**
     * @return the buildings
     */
    public LinkedList<CityKingsBuildingRaw> getBuildings() {
        return buildings;
    }

    /**
     * @param buildings the buildings to set
     */
    public void setBuildings(LinkedList<CityKingsBuildingRaw> buildings) {
        this.buildings = buildings;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the excelFile
     */
    public String getExcelFile() {
        return excelFile;
    }

    /**
     * @param excelFile the excelFile to set
     */
    public void setExcelFile(String excelFile) {
        this.excelFile = excelFile;
    }

}

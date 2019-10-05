
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.BuildingFactory;
import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.datastructures.Gang;
import com.babysharks.datastructures.GangCar;
import com.babysharks.datastructures.Gangster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class CityKingsMatchVisual {

    public CityKingsMatchVisual(boolean friendly) {
        this.friendly = friendly;
    }
    final private boolean friendly;
    private final long time = System.currentTimeMillis();

    private LinkedList<CityKingsBuildingRaw> buildings;
    byte[] screenshot;
    private String excelFile = "";
    private String jsonFile = "";
    private boolean reportContainsFlaws = false;
    Gang gang;
    private CityKingsBattlefieldAnalysis battlefield;
    transient public final static PicCoords OURSCORECOORDS = new PicCoords(150, 135, 244, 45);
    transient public final static PicCoords THEIRSCORECOORDS = new PicCoords(632, 135, 244, 45);
    transient public final static PicCoords TARGETSCORECOORDS = new PicCoords(447, 149, 141, 38);

    transient private BufferedImage boardScreenshot;
    transient private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void main(String[] args) { //main method
        //      JadbDevice jadb = new se.vidstige.jadb.JadbConnection().getAnyDevice();
//        new CityKingsMatchRaw().scanBuildings(jadb);
    }

    public CityKingsMatchVisual scanBuildings(VirtualBoxMachine vm) {

        try {

            //CityKingsScreen.navigateTo(jadb);
            BufferedImage bi = vm.screenshot();
            Screenshottable.writePic(bi, "PictureUsedForCreateBuildings");
            setBuildings(BuildingFactory.createBuildings(vm, friendly));
            Gang gang = new Gang(vm.getGang());

            getBuildings().stream().forEachOrdered((building) -> {
                System.out.println(building);
                if (building.isSuspectProblem()) {
                    reportContainsFlaws = true;
                }
                if (building.getOwnershipBar().getTotalSlots() < 3) {
                    System.err.println("Failure to acquire building");
                    reportContainsFlaws = true;
                    System.out.println(building.toString());
                }
                building.getCars().forEach((car) -> {
                    Gangster g = gang.get_gangster_by_hash(car.getNameHash());
                    g.addCar(GangCar.convertCar(car));
                    for (Entry<String, String> e : vm.getHashes().entrySet()) {
                        if (g.get_hashes().contains(e.getKey())) {
                            g.set_Username(e.getValue());
                        }
                    }

                    car.convertCarPicToJpg();
                });
                boardScreenshot = building.getMapScreenshot();
            });
            String OUTPUT = "/var/www/baby-sharks/screenshots/";
            Gang newGang = new Gang(gang.get_Gang_Name());
            System.out.println("adding gangsters");
            for (Gangster g : gang.getGangsters()) {
                if (g.get_Username().equals("MISSING")) {

                    newGang.add_gangster(g);
                    String gfilename = g.getCars().get(0).getName_hash() + ".png";
                    File outputFile = new File(OUTPUT + gfilename);
                    ImageIO.write(g.getAllCarsPic(), "PNG", outputFile);
                    g.setImageLocation("https://javashark.baby-sharks.com/screenshots/" + gfilename);

                }
            }
            System.out.println("gangsters added");
            this.gang = newGang;
            this.battlefield = new CityKingsBattlefieldAnalysis(boardScreenshot);

            vm.setTaskComplete(true);

            String filename = System.currentTimeMillis() + "-" + vm.getGang() + "-" + vm.getUsername() + "-" + UUID.randomUUID().toString();
            this.excelFile = writeToWebServer(filename);
            writeLocalJsonFile("/var/www/baby-sharks/excel/" + filename);

            return this;
        } catch (IOException ex) {
            Logger.getLogger(CityKingsMatchVisual.class.getName()).log(Level.SEVERE, null, ex);
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

    public void writeJson(File f) throws IOException {
        try (FileWriter fw = new FileWriter(f.getAbsolutePath())) {
            gson.toJson(this, fw);
            fw.flush();
        }

    }

    private String writeToWebServer(String outputUUID) throws IOException {
        String outputFolder = "/var/www/baby-sharks/excel/";
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File f = new File(folder.getAbsolutePath() + "/" + outputUUID + ".xls");
        new CityKingsJsonOutput().createOutputFiles(f, this, friendly);
        writeJson(new File(outputFolder + outputUUID + ".json"));
        this.jsonFile = f.getAbsolutePath();
        this.excelFile = "https://javashark.baby-sharks.com/" + outputUUID + ".xls";
        return f.getName();
    }

    public void clearScreenshots() {
        this.screenshot = null;
        for (CityKingsBuildingRaw building : this.getBuildings()) {
            building.clearBuildingPics();
            for (CityKingsCarRaw car : building.getCars()) {
                car.clearCarScreenshot();
                car.clearRowAndColumn();
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

    /**
     * @return the battlefield
     */
    public CityKingsBattlefieldAnalysis getBattlefield() {
        return battlefield;
    }

}

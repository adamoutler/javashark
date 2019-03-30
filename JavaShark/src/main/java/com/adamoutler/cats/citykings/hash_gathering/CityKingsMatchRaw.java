/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.BuildingFactory;
import static com.adamoutler.cats.citykings.hash_gathering.building.BuildingOwnershipDetector.isNumeric;
import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CityKingsMatchRaw {

    private final long time = System.currentTimeMillis();
    private LinkedList<CityKingsBuildingRaw> buildings;
    byte[] screenshot;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void main(String[] args) { //main method
  //      JadbDevice jadb = new se.vidstige.jadb.JadbConnection().getAnyDevice();
//        new CityKingsMatchRaw().scanBuildings(jadb);
    }

    public CityKingsMatchRaw scanBuildings(VirtualBoxMachine vm) {

        try {
            
            //CityKingsScreen.navigateTo(jadb);
            Screenshottable.writePic(vm.screenshot(), "PictureUsedForCreateBuildings");
            setBuildings(BuildingFactory.createBuildings(vm));

            getBuildings().stream().map((building) -> {
                return building;
            }).forEachOrdered((building) -> {
                System.out.println(building);
                if (building.getOwnershipBar().getTotalSlots()<5){
                    System.err.println("Failure to acquire building");
                    System.out.println(building.toString());
                    System.exit(1);
                    
                }
                building.getCars().forEach((car) -> {
                    car.convertCarPicToJpg();
                });

            });
            String outputFile = getBuildingOwnershipFileName(-1);
            System.out.println("Writing " + outputFile);
            try (FileWriter fw = new FileWriter(outputFile)) {
                gson.toJson(this, fw);
                fw.flush();
            }
            return this;
        } catch (IOException ex) {
            Logger.getLogger(CityKingsMatchRaw.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static String getBuildingOwnershipFileName(int numberToGoBack) {
        String tempdir = System.getProperty("java.io.tmpdir");
        String tempName = "BuildingDetailReport";
        String[] fileNames = new File(tempdir).list();
        ArrayList<String> list = new ArrayList<>();
        int ownershipObjectNumber = 1;
        for (int i = 1; i < fileNames.length; i++) {
            if (fileNames[i].startsWith(tempName) && !fileNames[i].startsWith(tempName + "+")) {
                String testName = fileNames[i].replace("/tmp/", "").replace(tempName, "").replace(".json", "");
                if (isNumeric(testName)) {
                    list.add(testName);
                }
            }
        }
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        Collections.reverse(list);

        try {
            return tempdir + File.separator + tempName + (Long.parseLong(list.get(0)) - numberToGoBack) + ".json";
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            return tempdir + File.separator + tempName + "0.json";
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

}

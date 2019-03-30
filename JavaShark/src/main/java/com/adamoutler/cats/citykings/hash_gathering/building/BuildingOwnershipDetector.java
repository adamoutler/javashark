/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering.building;

import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.imagerecognition.XYCoords;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.JadbDevice;

/**
 *
 * @author adamo
 */
public class BuildingOwnershipDetector {

    final static XYCoords BUILDING1 = new XYCoords(165, 353);
    final static XYCoords BUILDING2 = new XYCoords(110, 505);
    final static XYCoords BUILDING3 = new XYCoords(355, 595);
    final static XYCoords BUILDING4 = new XYCoords(630, 607);
    final static XYCoords BUILDING5 = new XYCoords(834, 512);
    final static XYCoords BUILDING6 = new XYCoords(818, 335);

    LinkedList<OwnedBy> status = new LinkedList<>();

    enum OwnedBy {
        NOBODY(0x5e6a83, "noone"),
        US(0x4179d9, "us"),
        THEM(0xeb4c47, "them");
        int color;
        String friendlyName;

        OwnedBy(int color, String friendlyName) {
            this.color = color;
            this.friendlyName = friendlyName;
        }

        public int getColor() {
            return color;
        }

        public String getFriendlyname() {
            return friendlyName;
        }
    }
    final transient BufferedImage screenshot;

    public BuildingOwnershipDetector(BufferedImage screenshot) {
        this.screenshot = screenshot;
    }

    public String scan() throws Exception {
        StringBuilder sb = new StringBuilder();
        System.out.println("Starting scan...");
        System.out.print("Screnshot:" + screenshot.getWidth() + " x " + screenshot.getHeight());
        PicColors pc = new PicColors(screenshot);
        sb.append(this.checkBuilding(1, BUILDING1, pc));
        sb.append(this.checkBuilding(2, BUILDING2, pc));
        sb.append(this.checkBuilding(3, BUILDING3, pc));
        sb.append(this.checkBuilding(4, BUILDING4, pc));
        sb.append(this.checkBuilding(5, BUILDING5, pc));
        sb.append(this.checkBuilding(6, BUILDING6, pc));
        return sb.toString();

    }

    public static String run(JadbDevice jadb) {
        try {

            BuildingOwnershipDetector bod = new BuildingOwnershipDetector(Screenshottable.getScreenshot());
            bod.scan();
            String s = new GsonBuilder().create().toJson(bod);

            String outputFile = getBuildingOwnershipFileName(-1);
            
            try (Writer writer = new FileWriter(outputFile)) {
                Gson gson = new GsonBuilder().create();
                gson.toJson(bod, writer);
                System.out.println("wrote to " + outputFile + "\n" + gson.toJson(bod));
                return gson.toJson(bod);
            }

        } catch (Exception ex) {
            Logger.getLogger(BuildingOwnershipDetector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private static String getBuildingOwnershipFileName(int numberToGoBack) {
        String tempdir = System.getProperty("java.io.tmpdir");
        String tempName = "BuildingOwnershipObject";
        String[] fileNames = new File(tempdir).list((File dir, String name)->{
          return name.startsWith(tempName);
        });
        ArrayList<String> list = new ArrayList<>();
        for (String s:fileNames){
            list.add(s.replaceAll("[^\\d]", ""));
        }        
        
        
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("[^0-9]", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        Collections.reverse(list);
        System.out.println("file numbers: "+list);
        
        Long l=Long.parseLong(list.get(0));
        
        
        System.out.println("newest file: "+l+".  going back: "+numberToGoBack+". Result"+(l-numberToGoBack));
        System.out.println("File name: "+tempdir + File.separator + tempName + (l- numberToGoBack) + ".json");
        try {
            return tempdir + File.separator + tempName + (l- numberToGoBack) + ".json";
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            return tempdir + File.separator + tempName + "0.json";
        }
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private String checkBuilding(int number, XYCoords coords, PicColors screenshot) throws Exception {
        OwnedBy ob = getOwnership(screenshot, coords, number);
        System.out.println("checking building " + number + "  at coords " + coords);
        String returnvalue = "Building " + number + ": " + ob.friendlyName + "\n";
        return returnvalue;
    }

    private OwnedBy getOwnership(PicColors screenshot1, XYCoords coords, int number) throws Exception {
        OwnedBy ownedBy = null;
        if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.US.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
            System.out.print("US;");
            ownedBy = OwnedBy.US;
        } else {
        }
        if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.THEM.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
            System.out.print("THEM;");
            ownedBy = OwnedBy.THEM;
        }
        if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.NOBODY.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {

            System.out.print("NOBODY;");
            ownedBy = OwnedBy.NOBODY;
        }
        if (ownedBy == null) {
            throw new Exception("Could not obtain information on the buildings");
        }
        status.add(ownedBy);
        return ownedBy;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search.search_assemblers;

import com.adamoutler.cats.battlefield.Battlefield;
import com.adamoutler.cats.battlefield.building.car.Car;
import com.adamoutler.imagerecognition.ImageJoiner;
import com.adamoutler.records_search.BattlefieldSearchParser;
import com.adamoutler.records_search.SearchException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class HistoricalImageSearch {

    final static SimpleDateFormat MMM = new SimpleDateFormat("MMM");

    static Gson gson = new GsonBuilder().create();

    public static BufferedImage getLastCars(String hash, String folder, String gang) throws SearchException {
        Date[] date = new Date[]{new Date()};
        return getCarsByDate(hash, folder, gang, date,true);
    }

    public static BufferedImage getCarsByDate(String hash, String folder, String gang, Date[] dates,boolean scan) throws SearchException {
        BattlefieldSearchParser bsp = new BattlefieldSearchParser();
        List<File> files = BattlefieldSearchParser.getRecords(folder, "", gang, "", scan?"scan":"recon");

        Map<Date, File> map = BattlefieldSearchParser.findOneRecordForEachDate(files, dates);
        return combineDatesAndImages(map, hash);
    }
    
     public static LinkedList<File> getReconRecordsFromDayBasedOnOpponent(String opponentName,String folder,  Date date) throws SearchException {
        BattlefieldSearchParser bsp = new BattlefieldSearchParser();
        List<File> files = BattlefieldSearchParser.getRecords(folder, "", "",opponentName, "recon");
        System.out.println ("files found: " +files.size()+" "+files.toString());
        if (files==null||files.isEmpty()) return null;
        LinkedList<File> datedFiles=reduceByDate( files,date,86400000 );
        
        return datedFiles;
    }

    private static LinkedList<File> reduceByDate(List<File> files, Date dates, long maxDistanceFromDateMs) {
        long originDate=dates.getTime();
        LinkedList<File> reducedFiles=new LinkedList<>();
        for (File f:files){
            try {
                long created=BattlefieldSearchParser.getCreatedEpochMillis(f);
                if (Math.abs(originDate-created)<maxDistanceFromDateMs){
                   reducedFiles.add(f);
                   
                }
            } catch (IOException ex) {
                Logger.getLogger(HistoricalImageSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return reducedFiles;
    }
     
    private static BufferedImage combineDatesAndImages(Map<Date, File> map, String hash) {
        LinkedList<BufferedImage> rows = new LinkedList<>();
        map.entrySet().stream().forEach(e -> {
            try {
                String mon = MMM.format(e.getKey());
                if (e.getValue() == null || !e.getValue().exists()) {
                    return;
                }
                Battlefield b = gson.fromJson(new String(Files.readAllBytes(Paths.get(e.getValue().getAbsolutePath()))), Battlefield.class);
                ArrayList<Car> cars = b.getAllCars();
                LinkedList<BufferedImage> reportCars = new LinkedList<>();
                cars.stream().forEach(car -> {
                    if (car.getNameHash().equals(hash)) {
                        reportCars.add(car.getCarAsJpg());
                    }
                });
                BufferedImage row = ImageJoiner.joinImagesSide(reportCars);
                rows.add(row);
            } catch (IOException ex) {
                Logger.getLogger(HistoricalImageSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        BufferedImage returnValue = ImageJoiner.joinImagesSide(rows);
        return returnValue;

    }


}

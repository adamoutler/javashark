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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class HistoricalImageSearch {

    final static SimpleDateFormat f = new SimpleDateFormat("MMM");

    static Gson gson = new GsonBuilder().create();

    public static BufferedImage getLastCars(String hash, String folder, String gang) throws SearchException {
        Date[] date = new Date[]{new Date()};
        return getCarsByDate(hash, folder, gang, date,true);
    }

    public static BufferedImage getCarsByDate(String hash, String folder, String gang, Date[] dates,boolean scan) throws SearchException {
        BattlefieldSearchParser bsp = new BattlefieldSearchParser();
        List<File> files = bsp.getRecords(folder, "", gang, "", scan?"scan":"recon");

        Map<Date, File> map = bsp.findOneRecordForEachDate(files, dates);
        return combineDatesAndImages(map, hash);
    }

    private static BufferedImage combineDatesAndImages(Map<Date, File> map, String hash) {
        LinkedList<BufferedImage> rows = new LinkedList<>();
        map.entrySet().stream().forEach(e -> {
            try {
                String mon = f.format(e.getKey());
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

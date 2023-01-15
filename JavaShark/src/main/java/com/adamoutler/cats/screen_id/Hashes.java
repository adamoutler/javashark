/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.Prop;
import com.adamoutler.imagerecognition.HashedPicCoords;
import com.adamoutler.javashark.MainApp;
import com.babysharks.messaging.HashedMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class Hashes {

    final public static Properties PROPERTIES = new Properties();
    final public static String HASHFILE = Prop.getProp("hashfile");
    
    public static String hashesFileUrl = "https://baby-sharks.hackedyour.info/CatsScreenDataHashes.properties";

    static ArrayList<HashedPicCoords> unknown;
    static ArrayList<HashedPicCoords> off;
    static ArrayList<HashedPicCoords> blackScreen;
    static ArrayList<HashedPicCoords> booted;
    static ArrayList<HashedPicCoords> championshipGarage;
    static ArrayList<HashedPicCoords> catsLoadedChampionshipScreen;
    static ArrayList<HashedPicCoords> catsLoadedUltimateScreen;
    static ArrayList<HashedPicCoords> catsLoadingScreen;
    static ArrayList<HashedPicCoords> cityKingCar1;
    static ArrayList<HashedPicCoords> cityKingCar2;
    static ArrayList<HashedPicCoords> cityKingCar3;
    static ArrayList<HashedPicCoords> cityKingsBeginTheAttack;
    static ArrayList<HashedPicCoords> cityKingsEnlist;
    static ArrayList<HashedPicCoords> cityKings;
    static ArrayList<HashedPicCoords> cityKingsBuildingNoScout;
    static ArrayList<HashedPicCoords> cityKingsBuildingAttack;
    static ArrayList<HashedPicCoords> cityKingsBuildingDefend;
    static ArrayList<HashedPicCoords> cityKingsBuildingScreen;
    static ArrayList<HashedPicCoords> cityKingsVictory;
    static ArrayList<HashedPicCoords> cityKingsDefeat;
    static ArrayList<HashedPicCoords> exitScreen;
    static ArrayList<HashedPicCoords> friends;
    static ArrayList<HashedPicCoords> gangFights;
    static ArrayList<HashedPicCoords> gangFightsBattleScreen;
    static ArrayList<HashedPicCoords> gangFightsComplete;
    static ArrayList<HashedPicCoords> gangFightsDefeat;
    static ArrayList<HashedPicCoords> gangFightsVictory;
    static ArrayList<HashedPicCoords> reconnect;
    static ArrayList<HashedPicCoords> parkedCar;
    static ArrayList<HashedPicCoords> unresponsive;
    static ArrayList<HashedPicCoords> whatCanYouDoToday;
    static ArrayList<HashedPicCoords> store;
    static {        
        loadHashes();
    }

    private static void loadHashes() throws SecurityException {
        // load data from web
        try {
            var inputStream = new URL(hashesFileUrl).openStream();
            var text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            if (text.contains("CITYKINGS"))
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
                PROPERTIES.load(bais);
            }
        } catch (IOException ex) {
            System.out.println("error loading file properties in Hashes (remote " + hashesFileUrl + ")!");
            Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // load from web failed ? -> try to load from local file
        if (PROPERTIES.isEmpty()) {
            try {
                var fileInputStream = new FileInputStream(HASHFILE);
                PROPERTIES.load(fileInputStream);
            } catch (IOException ex) {
                System.out.println("error loading file properties in Hashes (local)!");
                Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!PROPERTIES.isEmpty()) {
            try {
                Enumeration<String> properties = (Enumeration<String>) PROPERTIES.propertyNames();
                while (properties.hasMoreElements()) {
                    String key = properties.nextElement();
                    System.out.println(key);
                    String value = PROPERTIES.getProperty(key);
                    if (value == null || value.isEmpty()) {
                        continue;
                    }
                    for (Field hashArrays : Hashes.class.getDeclaredFields()) {
                        if (hashArrays.getName().toUpperCase().equals(key.toUpperCase())) {
                            ArrayList<HashedPicCoords> al = new ArrayList<>();
                            String[] values = value.split(",");
                            if (values.length > 4) {
                                if (key.equals("BLACKSCREEN")) {
                                    System.out.println("foo");
                                }
                                HashedPicCoords hpc = createHashedPicCoords(values);
                                al.add(hpc);

                                for (int i = 5; i < values.length; i++) {
                                    al.add(createHashedPicCoords(hpc, values[i]));
                                }
                            }
                            if (key.equals("BLACKSCREEN")) {
                                System.out.println("foo");
                            }
                            hashArrays.set(null, al);
                            System.out.println("loaded " + key);
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static HashedPicCoords createHashedPicCoords(HashedPicCoords hpc, String hash) {
        HashedPicCoords newValues = new HashedPicCoords(hpc.getX(),
                hpc.getY(), hpc.getW(), hpc.getH(),
                Integer.parseInt(hash.trim()));
        return newValues;
    }

    private static HashedPicCoords createHashedPicCoords(String[] values) {
        int x = Integer.parseInt(values[0].trim());
        int y = Integer.parseInt(values[1].trim());
        int w = Integer.parseInt(values[2].trim());
        int h = Integer.parseInt(values[3].trim());
        Integer hash; 
        try {
            hash = Integer.parseInt(values[4].trim());
        } catch (NumberFormatException ex){
            hash=null;
        }
        return new HashedPicCoords(x, y, w, h, hash);
    }

    public static Properties addProperty(String key, String value) {
        PROPERTIES.setProperty(key, value);
        return PROPERTIES;
    }

    public static Properties writeProperties() {
        try {
            makeFileIfNotExists();
            File backup=new File(new File(HASHFILE)
                    .getParentFile().getAbsolutePath()+
                    File.separator+"backup"+File.separator+
                    System.currentTimeMillis()+".properties");
            backup.getParentFile().mkdirs();
            
            try (FileOutputStream backupStream = new FileOutputStream(backup)) {
                PROPERTIES.store(backupStream, new Date().toString());
            }        
            try (FileOutputStream fos = new FileOutputStream(HASHFILE)) {
                PROPERTIES.store(fos, new Date().toString());
                return PROPERTIES;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void makeFileIfNotExists() throws IOException {
        File f = new File(HASHFILE);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
    }

}

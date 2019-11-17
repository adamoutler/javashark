/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.HashedPicCoords;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class Hashes {

    static {

        try (FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "/CatsScreenDataHashes.properties")) {
            Properties p = new Properties();
            p.load(fis);
            Enumeration<String> properties = (Enumeration<String>) p.propertyNames();
            while (properties.hasMoreElements()) {
                String key = properties.nextElement();
                System.out.println(key);
                String value = p.getProperty(key);
                if (value==null||value.isEmpty()) continue;
                for (Field hashArrays : Hashes.class.getDeclaredFields()) {
                    if (hashArrays.getName().toUpperCase().equals(key.toUpperCase())) {
                        ArrayList<HashedPicCoords> al=new ArrayList<>();
                        String[] values = value.split(",");
                        if (values.length > 4) {
                            HashedPicCoords hpc = createHashedPicCoords(values);
                           al.add(hpc);
                            
                            
                            for (int i = 5; i < values.length; i++) {
                               al.add(createHashedPicCoords(hpc, values[i]));

                            }
                        }
                        if (key.equals("CITYKINGS")){
                            //System.out.println("foo");
                        }
                        hashArrays.set(null, al);
                        System.out.println("loaded " + key);
                        continue;
                    }

                }
            }
        } catch (IOException ex) {
            System.out.println("error loading properties in Hashes!");
            Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Hashes.class.getName()).log(Level.SEVERE, null, ex);
       
        }
    }
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
        int hash = Integer.parseInt(values[4].trim());
        return new HashedPicCoords(x, y, w, h, hash);
    }
}


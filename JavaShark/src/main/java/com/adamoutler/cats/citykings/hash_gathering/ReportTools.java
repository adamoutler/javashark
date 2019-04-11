/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.cats.citykings.hash_gathering.building.CityKingsBuildingRaw;
import com.adamoutler.cats.citykings.hash_gathering.building.car.CityKingsCarRaw;
import com.adamoutler.imagerecognition.ImageJoiner;
import com.babysharks.datastructures.Car;
import com.babysharks.datastructures.Gang;
import com.babysharks.datastructures.Gangs;
import com.babysharks.datastructures.Gangster;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class ReportTools {


    CityKingsMatchRaw battlefield;
    Gangs gangs = new Gangs();

    ReportTools(String gangName, CityKingsMatchRaw battlefield) {
        this.battlefield = battlefield;
        gangs.add_gang(Gang.create_gang(gangName));

    }

    public static ReportTools getReport(String gangName, String repo, String name) throws FileNotFoundException {
        ReportFactory r = new ReportFactory();
        r.setRepoFolder(repo);
        ReportTools rt = new ReportTools(gangName, r.getReport(name));
        for (CityKingsBuildingRaw building:rt.battlefield.getBuildings()){
            for (CityKingsCarRaw car:building.getCars()){
                rt.gangs.get_Gang_By_Name(gangName).get_gangster_by_hash(car.getNameHash()).addCars(Car.convertCar(car));
            }
        }
        return rt;
    }

    public Gangster getGangster(String... nameHashes) {
        Gangster gangster = new Gangster(nameHashes[0]);
        for (String hash : nameHashes) {
            gangster.add_hash(hash);
        }
        return gangster;
    }

    public Gangster getUserBadgePaneFromUserHash(String[] nameHashes) {
        Gangster g = getUserBadgesFromHashes(nameHashes);
        g.setAllCarsPic(ImageJoiner.joinImagesSide(g.getCarPics()));
        return g;
    }

    public Gangster getUserBadgesFromHashes(String[] nameHashes) {
        Gangster g = getGangster(nameHashes);

        for (CityKingsBuildingRaw building : battlefield.getBuildings()) {
            for (CityKingsCarRaw car : building.getCars()) {
                for (String hash : nameHashes) {
                    if (car.getNameHash().equals(hash)) {
                        g.addCarPics(car.getCarScreenshot());
                        Car c = new Car();
                        c.setBuilding(car.getBuilding());
                        c.setColumnn(car.getColumn());
                        c.setLocation(car.getLocation());
                        c.setRow(car.getRow());
                        c.setUnique_hash(car.getCarHash());
                        break;
                    }
                }
            }
        }

        return g;
    }

}

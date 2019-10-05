/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.battlefield.Battlefield;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.datastructures.Gang;
import com.babysharks.datastructures.GangCar;
import com.babysharks.datastructures.Gangster;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class UnidentifiedGangsters {

    public static void createReport(VirtualBoxMachine vm, Battlefield report) {
        Gang gang = new Gang(vm.getGang());
        report.getBuildings().stream().forEachOrdered((building) -> {
            System.out.println(building);
            if (building.isSuspectProblem()) {
                report.setReportContainsFlaws(true);
            }

            if (building.getCars() == null || building.getCars().isEmpty() || building.getTosb() == null) {
                return;
            }

            if (building.getTosb().getTotalSlots() < 3) {
                System.err.println("Failure to acquire building");
                report.setReportContainsFlaws(true);
                System.out.println(building.toString());
            }
            building.getCars().forEach((car) -> {
                Gangster g = gang.get_gangster_by_hash(car.getNameHash());
                g.addCar(GangCar.convertCar(car));
                try {
                    for (Map.Entry<String, String> e : vm.getHashes().entrySet()) {
                        if (g.get_hashes().contains(e.getKey())) {
                            g.set_Username(e.getValue());
                        }
                    }
                } catch (Exception ex) {

                }
            });
        });
        String OUTPUT = "/var/www/baby-sharks/screenshots/";
        Gang newGang = new Gang(gang.get_Gang_Name());
        System.out.println("adding gangsters");
        for (Gangster g : gang.getGangsters()) {
            if (g.get_Username().equals("MISSING")) {

                newGang.add_gangster(g);
                String gfilename = g.getCars().get(0).getName_hash() + ".png";
                File outputFile = new File(OUTPUT + gfilename);
                try {
                    ImageIO.write(g.getAllCarsPic(), "PNG", outputFile);
                    g.setImageLocation("https://javashark.baby-sharks.com/screenshots/" + gfilename);
                } catch (IOException ex) {
                    Logger.getLogger(UnidentifiedGangsters.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}

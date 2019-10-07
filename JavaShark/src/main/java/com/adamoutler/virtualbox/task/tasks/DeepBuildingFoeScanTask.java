/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.citykings.hash_gathering.CityKingsMatchRaw;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.PapaAsync;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class DeepBuildingFoeScanTask extends Task {

    public DeepBuildingFoeScanTask() {
        System.out.println();
    }

    @Override
    public void initializeTask(VirtualBoxMachine vm) {

        try {
            Thread t = new Thread(() -> {

                try {
                    CityKingsMatchRaw report = new CityKingsMatchRaw(false).scanBuildings(vm);
                    String s = report.getExcelFile();
                    report.setExcelFile("https://javashark.baby-sharks.com/excel/" + s);
                    report.clearScreenshots();
                    Message m = new Message().setCommand("deepbuildingreconcomplete");
                    m.getData().setGang(vm.getGang()).setUsername(vm.getUsername()).setReason("Recon Complete excel").setResultObject("https://javashark.baby-sharks.com/excel/" + s);

                    File f = new File("/var/www/baby-sharks/excel/" + report.getExcelFile().replaceAll(".*/", "").replaceAll(".xls", ".json"));
                    System.out.println();
                    try {
                        report.writeJson(f);
                    } catch (IOException ex) {
                        Logger.getLogger(DeepBuildingFoeScanTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    m.getData().setReason("Recon Complete json").setResultObject("https://javashark.baby-sharks.com/excel/" + s.replaceAll(".xls", ".json"));
                    PapaAsync.send(m);
                } catch (Exception ex) {

                }
            });
            t.setName("Deep Building Scan: " + vm.getUsername() + "-" + vm.getGang());
            t.start();
            ready(vm);
        } catch (Exception ex) {
            Logger.getLogger(DeepBuildingFoeScanTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return true;
    }
}

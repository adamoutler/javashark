/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.citykings.hash_gathering.CityKingsMatchRaw;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.SendPapaAsynch;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class DeepBuildingScanTask extends Task {

    public DeepBuildingScanTask() {
        System.out.println();
    }

    @Override
    public void initializeTask(VirtualBoxMachine vm) {

        try {
            Thread t = new Thread(() -> {

                try {
                    CityKingsMatchRaw report = new CityKingsMatchRaw().scanBuildings(vm);
                    String s = report.getExcelFile();
                    report.setExcelFile("https://javashark.baby-sharks.com/excel/" + s);
                    report.clearScreenshots();
                    Message m = new Message().setCommand("deepbuildingscancomplete");
                    m.getData().setGang(vm.getGang()).setUsername(vm.getUsername()).setReason("Scan Complete excel").setResultObject("https://javashark.baby-sharks.com/excel/" + s);

                    File f = new File("/var/www/baby-sharks/excel/" + report.getExcelFile().replaceAll(".*/", "").replaceAll(".xls", ".json"));
                    System.out.println();
                    try {
                        report.writeJson(f);
                    } catch (IOException ex) {
                        Logger.getLogger(DeepBuildingScanTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    m.getData().setReason("Scan Complete json").setResultObject("https://javashark.baby-sharks.com/excel/" + s.replaceAll(".xls", ".json"));
                    SendPapaAsynch.sendToPapa(m);
                } catch (Exception ex) {

                }
            });
            t.setName("Deep Building Scan: " + vm.getUsername() + "-" + vm.getGang());
            t.start();
            ready(vm);
        } catch (Exception ex) {
            Logger.getLogger(DeepBuildingScanTask.class.getName()).log(Level.SEVERE, null, ex);
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

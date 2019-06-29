/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.cats.battlefield.Battlefield;
import com.adamoutler.cats.battlefield.BattlefieldExcelGenerator;
import com.adamoutler.cats.battlefield.BattlefieldFactory;
import com.adamoutler.cats.battlefield.CatsGangPicGenerator;
import com.adamoutler.cats.battlefield.building.Building;
import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import static com.adamoutler.virtualbox.task.tasks.Task.WEBSERVEREXCELLOCATION;
import com.adamoutler.virtualbox.task.tasks.sentry.UnidentifiedGangsters;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.SendPapaAsynch;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class BattlefieldFullFriendlylScanTask extends Task {

    public BattlefieldFullFriendlylScanTask() {
        System.out.println();
    }

    @Override
    public void initializeTask(VirtualBoxMachine vm) {

        try {
            Thread t = new Thread(() -> {

                try {
                    BattlefieldFactory battleFactory = new BattlefieldFactory(vm.getTaskData().isFriendlyScan(), vm.getTaskData().getBuildingsToScan());
                    Battlefield report = battleFactory.scanBattleField(vm);
                    report.setBattlefield(new CityKingsBattlefieldAnalysis(report.getBoardScreenshot()));
                    if (vm.isKillThread()) {

                        Message m = new Message()
                                .setCommand("deepbuildingscancomplete")
                                .setData(new Message().getData()
                                        .setGang(vm.getGang())
                                        .setUsername(vm.getUsername()));
                        for (Building b : report.getBuildings()) {
                            if (b.isSuspectProblem()) {
                                m.getData().setComment(
                                        b.getSuspsectProblemReason());
                                m.getData().setReason("SCAN ERROR!")
                                        .setResultObject(null);
                            }
                        }
                        if (m.getData().getReason() == null || m.getData().getReason().isEmpty()) {
                            m.getData().setReason("Scan Canceled");
                        }
                        vm.setKillThread(false);
                        SendPapaAsynch.sendToPapa(m);
                        return;

                    }
                    Message m = new Message().setCommand("deepbuildingscancomplete");
                    long time = System.currentTimeMillis();
                    String name = "scan-" + time + "-" + vm.getGang() + "-" + vm.getUsername() + "-" + UUID.randomUUID().toString() + "OCRName-" + report.getOcrName();
                    File fullJson = new File(WEBSERVEREXCELLOCATION + name);
                    File groupPicture = new File(WEBSERVEREXCELLOCATION + name + ".png");
                    CatsGangPicGenerator.saveGangPicToFile(groupPicture, report);
                    File papaJson = new File(WEBSERVEREXCELLOCATION + name + ".json");
                    m.getData().setGang(vm.getGang())
                            .setUsername(vm.getUsername())
                            .setReason("Scan Complete json").setTeamPicture("https://javashark.baby-sharks.com/excel/" + groupPicture.getName())
                            .setResultObject("https://javashark.baby-sharks.com/excel/" + papaJson.getName());
                    boolean errored = false;
                    try {
                        File excelFile = new File(WEBSERVEREXCELLOCATION + name + ".xls");
                        writeFiles(report, fullJson, papaJson, excelFile);
                        UnidentifiedGangsters.createReport(vm, report);
                    } catch (Exception ex) {
                        Logger.getLogger(BattlefieldFullFriendlylScanTask.class.getName()).log(Level.SEVERE, null, ex);
                        SendPapaAsynch.sendToAdam(m);
                        m.getData().setComment(m.getData().getReason());
                        m.getData().setResultObject(null).setReason("Error during scanning");
                        errored = true;

                    }
                    SendPapaAsynch.sendToPapa(m);
                    vm.completeCurrentTask();
                    if (errored) {
                        System.out.println("Error occurred during scanning");
                        for (Building b : report.getBuildings()) {
                            System.out.println(b.getTosb());

                        }
                    }

                } catch (Exception ex) {
                    Logger.getLogger(BattlefieldFullFriendlylScanTask.class.getName()).log(Level.SEVERE, null, ex);

                }
            });
            vm.setKillThread(false);
            t.setName("Deep Building Scan: " + vm.getUsername() + "-" + vm.getGang());
            t.start();
            ready(vm);
        } catch (Exception ex) {
            Logger.getLogger(BattlefieldFullFriendlylScanTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeFiles(Battlefield report, File fullJson, File papaJson, File excelFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fullJson)) {
            fos.write(new GsonBuilder().setLenient().create().toJson(report).getBytes());
        }
                    report.setOcrNamePic(null);

        try {
            new BattlefieldExcelGenerator().createOutputFiles(excelFile, report, true);
        } catch (Exception ex) {
            Logger.getLogger(BattlefieldFullFriendlylScanTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        report.setExcelFile("https://javashark.baby-sharks.com/excel/" + excelFile.getName());
        report.setJsonFile("https://javashark.baby-sharks.com/excel/" + fullJson.getName());
        report.clearScreenshots();
        try (FileOutputStream fos = new FileOutputStream(papaJson)) {
            fos.write(new GsonBuilder().setLenient().setPrettyPrinting().create().toJson(report).getBytes());
        }
    }

    @Override
    synchronized public void runTask(VirtualBoxMachine vm) {

    }

    @Override
    public boolean resume(VirtualBoxMachine vm) {
        return false;
    }
}

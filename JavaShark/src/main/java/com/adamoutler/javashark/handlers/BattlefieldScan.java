/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.javashark.sentry_tools.SentryTools;
import com.adamoutler.virtualbox.task.TaskType;
import static com.babysharks.messaging.ErrorType.USERHASNOSENTRY;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"battlefieldScan","data":{"gang":"development","username":"adamoutler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 *
 * curl -X PATCH -d
 * '{"message":{"command":"battlefieldScan","data":{"gang":"development","username":"adamoutler","scanBuildings":[1,2,3,4,5,6],"friendlyScan":"true"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class BattlefieldScan extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        int[] buildingsToScan = request.getData().getScanBuildings();
        if (buildingsToScan == null || buildingsToScan.length == 0) {
            buildingsToScan = new int[]{1, 2, 3, 4, 5, 6};
        }
        Boolean friendly = request.getData().isFriendlyScan();
        if (friendly == null) {
            friendly = true;
        }
        if (vm == null) {
            this.sendBadReq(he, username + " has no sentry",USERHASNOSENTRY);
        }
        vm.getTaskData().setFriendlyScan(friendly);
        vm.getTaskData().setBuildingsToScan(buildingsToScan);
        vm.setHashes(request.getData().getHashes());
        vm.clearTasks();
        addTasks(TaskType.CLEAR, TaskType.POWERON, TaskType.STARTCATS, TaskType.CORRECTIVEACTIONCK);
        if (request.getCommand().contains("recon")) {
            request.getData().setReason("recon");
            addTasks(TaskType.DEEPBUILDINGRECON);
        } else {
            request.getData().setReason("scanning");
            addTasks(TaskType.BATTLEFIELDSCAN);
        }
        if (new SentryTools(vm).isOnWatch()) {
            addTasks(TaskType.WATCH);
        } else {
        }
        request.getData().addAvailable(vm);
        this.sendSucessMessage(he, request);
    }
}

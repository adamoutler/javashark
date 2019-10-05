/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.ErrorType;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"addsidetask","data":{"gang":"development","username":"adamoutler","tasks":["SKIP","SENTRY"]}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class TemporaryTaskHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            vm.getAdbName();
        } catch (Exception ex) {
            this.sendBadReq(he, username, ErrorType.VMERROR);
            return;
        }
        try {
            vm.setTemporaryTask(TaskType.MYCARS);
        } catch (Exception ex) {
            this.sendBadReq(he, username, ErrorType.NOSUCHTASK);
        }

        request.getData()
                .setResultObject(vm).setReason("updated task list");

        this.sendSucessMessage(he, request);
    }

}

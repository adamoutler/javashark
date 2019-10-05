/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.babysharks.messaging.ErrorType;
import static com.babysharks.messaging.ErrorType.NOSUCHTASK;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 *  curl -X PATCH -d '{"message":{"command":"addtask","data":{"gang":"development","username":"adamoutler","tasks":["SKIP","SENTRY"]}}}' https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class TaskHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
        addTasks(this.request.getData().getTasks());
        request.getData().setResultObject(vm).setReason("updated task list");
        this.sendSucessMessage(he, request);
        } catch (Exception ex){
            this.sendBadReq(he, username, NOSUCHTASK);
        }
    }
    
}

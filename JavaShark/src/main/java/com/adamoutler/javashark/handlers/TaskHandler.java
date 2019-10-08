/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import static com.babysharks.messaging.ErrorType.NOSUCHTASK;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"addtask","data":{"gang":"development","username":"adamoutler","tasks":["SKIP","SENTRY"]}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class TaskHandler extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            if (this.request.getData().getTasks().length == 0|| this.request.getData().getTasks()[0]==null) {
                request.getData().setErrorType(NOSUCHTASK);
                
                this.sendArbitrary( he, request);
                return;
            }

            addTasks(this.request.getData().getTasks());

            request.getData().setResultObject(vm).setReason("updated task list");

            this.sendSucessMessage(he, request);
        } catch (Exception ex) {
            request.getData().setErrorType(NOSUCHTASK);

            this.sendBadReq(he, request);
        }
    }

}

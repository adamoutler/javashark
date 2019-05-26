/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.javashark.sentry_tools.SentryTools;
import com.adamoutler.virtualbox.VirtualBoxControl;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * curl -X PATCH -d
 * '{"message":{"command":"sentrystart","data":{"gang":"development","username":"adamoutler"}}}'
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class StopSentry extends SentryAbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        VirtualBoxControl vmControl = new VirtualBoxControl(vm);
        VirtualBoxMachine killvm=vm;
        SentryTools st=new SentryTools(vm);
        if (!st.isBusy()){
            VirtualBoxMachine otherVM=st.getCurrent(TaskType.WATCH);
            if (otherVM==null){
               request.getData().addAvailable(vm).setReason("stopped already");
            } else {
                otherVM.addTask(TaskType.CLEAR);
                vmControl=new VirtualBoxControl(otherVM);
                request.getData().addAvailable(otherVM).setReason("stopping");
            }
        } else {
            addTasks(TaskType.CLEAR);
            request.getData().addAvailable(vm).setReason("stopping");
        }
        vmControl.stopVM();
        this.sendSucessMessage(he, request);

    }

}

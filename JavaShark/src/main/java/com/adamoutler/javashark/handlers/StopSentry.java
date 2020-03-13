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
import com.babysharks.messaging.ErrorType;
import static com.babysharks.messaging.ErrorType.WHATEVER;
import com.babysharks.messaging.PapaAsync;
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
        try {
            VirtualBoxControl vmControl = new VirtualBoxControl(vm);
            TaskType tt=null;
            SentryTools st = new SentryTools(vm);
            if (vm == null || !vm.isPoweredOn()) {
                VirtualBoxMachine otherVM = st.getCurrent(TaskType.WATCH);
                if (otherVM == null) {
                    request.getData().setErrorType(ErrorType.NOSENTRIESACTIVE);
                    request.getData().addAvailable(vm).setReason("stopped already");
                } else {
                    tt = otherVM.getCurrentTask();
                    otherVM.addTask(TaskType.CLEAR);
                    vmControl = new VirtualBoxControl(otherVM);
                    request.getData().addAvailable(otherVM).setReason("stopping");
                }
            } else {
                tt = vm.getCurrentTask();
                addTasks(TaskType.CLEAR);
                request.getData().addAvailable(vm).setReason("stopping");
            }
            if (tt != null && tt.equals(TaskType.WATCH)) {
                PapaAsync.sentryReasonWarning(vm, "blind");
            }
            vm.setKillThread(true);
            vmControl.stopVM();
            this.sendSucessMessage(he, request);
        } catch (Exception ex) {
            request.getData().setErrorType(WHATEVER);

            this.sendBadReq(he, request);
        }
    }

}

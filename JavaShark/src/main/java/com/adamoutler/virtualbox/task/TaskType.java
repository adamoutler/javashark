/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.virtualbox.task.tasks.CorrectiveActionCKTask;
import com.adamoutler.virtualbox.task.tasks.ForceRemoveTask;
import com.adamoutler.virtualbox.task.tasks.IdleTask;
import com.adamoutler.virtualbox.task.tasks.PowerOnTask;
import com.adamoutler.virtualbox.task.tasks.PoweroffTask;
import com.adamoutler.virtualbox.task.tasks.DeepBuildingScanTask;
import com.adamoutler.virtualbox.task.tasks.SentryTask;
import com.adamoutler.virtualbox.task.tasks.StartCatsTask;

/**
 *
 * @author adamo
 */
public enum TaskType {

    
    IDLE(new IdleTask(), "Machine is idle"),
    SENTRY(new SentryTask(), "Machine is on Sentry Duty"),
    DEEPBUILDINGSCAN(new DeepBuildingScanTask(), "Deep Building Scanning, Friendly Machines"),
    POWERON(new PowerOnTask(),"Turn the machine on"),
    STARTCATS(new StartCatsTask(),"Start CATS"),
    POWEROFF(new PoweroffTask(),"Machine is powered off"),
    SKIP(new ForceRemoveTask(),"Skip the current task"),
    CLEAR(new IdleTask(), "Clearing all tasks"),
    CORRECTIVEACTIONCK(new CorrectiveActionCKTask(),"Performing steps to get to CK");
    
    
    
    private IfaceTask task;
    private String taskDescription;

    TaskType(IfaceTask clazz, String s) {
        this.task = clazz;
        this.taskDescription = s;
    }

    /**
     * @return the task
     */
    public IfaceTask getTask() {
        return task;
    }

   
    /**
     * @return the taskName
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.tasks.BattlefieldFullFriendlylScanTask;
import com.adamoutler.virtualbox.task.tasks.CorrectiveActionCKTask;
import com.adamoutler.virtualbox.task.tasks.CorrectiveActionFacebook;
import com.adamoutler.virtualbox.task.tasks.DeepBuildingFoeScanTask;
import com.adamoutler.virtualbox.task.tasks.DeepBuildingFriendlyScanTask;
import com.adamoutler.virtualbox.task.tasks.ForceRemoveTask;
import com.adamoutler.virtualbox.task.tasks.IdleMinuteTask;
import com.adamoutler.virtualbox.task.tasks.IdleTask;
import com.adamoutler.virtualbox.task.tasks.MyCarsTask;
import com.adamoutler.virtualbox.task.tasks.PowerOnTask;
import com.adamoutler.virtualbox.task.tasks.PoweroffTask;
import com.adamoutler.virtualbox.task.tasks.WatchTask;
import com.adamoutler.virtualbox.task.tasks.StartCatsTask;
import com.adamoutler.virtualbox.task.tasks.Task;
import com.adamoutler.virtualbox.task.tasks.UpdateCatsTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public enum TaskType {
    IDLE(new IdleTask(), "Machine is idle"),
    WATCH(new WatchTask(), "Machine is on Sentry Duty"),
    BATTLEFIELDSCAN(new BattlefieldFullFriendlylScanTask(), "Performing Deep Building Scanning, Friendly Machines"),
    DEEPBUILDINGSCAN(new DeepBuildingFriendlyScanTask(), "Deep Building Scanning, Friendly Machines"),
    DEEPBUILDINGRECON(new DeepBuildingFoeScanTask(), "Deep Building Scanning, Foe Machines"),
    POWERON(new PowerOnTask(),"Turn the machine on"),
    STARTCATS(new StartCatsTask(),"Start CATS"),
    POWEROFF(new PoweroffTask(),"Machine is powered off"),
    SKIP(new ForceRemoveTask(),"Skip the current task"),
    CLEAR(new IdleTask(), "Clearing all tasks"),
    FACEBOOK(new CorrectiveActionFacebook(), "Opening Facebook Screen"),
    CORRECTIVEACTIONCK(new CorrectiveActionCKTask(),"Performing steps to get to CK"),
    IDLE1MINUTE(new IdleMinuteTask(),"Waiting for one minute"),
    UPDATECATS(new UpdateCatsTask(),"Updating CATS."),
    MYCARS(new MyCarsTask(),"Examining my cars");
    
    
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

      private static String getTaskFile(VirtualBoxMachine vm) {
        String fileName = System.getProperty("java.io.tmpdir") + "/" + vm.getVMName();
        return fileName;
    }

    public static TaskType getTasks(VirtualBoxMachine vm) {
        String fileName = getTaskFile(vm);
        File taskFile = new File(fileName);
        TaskType currentTask = TaskType.IDLE;
        if (taskFile.exists()) {
            try {
                String contents = new String(Files.readAllBytes(Paths.get(fileName)));
                currentTask = TaskType.valueOf(contents);
                System.out.println(vm.getVMName()+" found resumable task "+currentTask);
            } catch (IOException ex) {
                currentTask = TaskType.IDLE;
                System.out.println("resumable task not found");
            }
        }
        return currentTask;
    }

    public static void writeTaskToFile(VirtualBoxMachine vm) {
        String fileName = getTaskFile(vm);
        try (PrintStream out = new PrintStream(new FileOutputStream(fileName))) {
            out.print(vm.getCurrentTask().name());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}

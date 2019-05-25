/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.IfaceTask;
import static com.adamoutler.virtualbox.task.TaskManager.getTaskMachine;
import com.adamoutler.virtualbox.task.TaskType;
import java.io.File;
import java.io.FileInputStream;
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
public abstract class Task implements IfaceTask {

    final static public String WEBSERVEREXCELLOCATION="/var/www/baby-sharks/excel/";
    final static public String WEBSERVERLOCATION="/var/www/baby-sharks/";
    final static public String WEBSERVERSCREENSHOTLOCATION="/var/www/baby-sharks/screenshots/";
    abstract void initializeTask(VirtualBoxMachine vm);

    abstract void runTask(VirtualBoxMachine vm);

    @Override
    public void init(VirtualBoxMachine vm) {
        vm = getTaskMachine(vm);
        vm.setTaskInitialized(true);
        initializeTask(vm);
    }

    @Override
    public void exec(VirtualBoxMachine vm) {
        vm = getTaskMachine(vm);
        runTask(vm);
    }

    void ready(VirtualBoxMachine vm) {
        vm.setTaskReady(true);
    }

    void complete(VirtualBoxMachine vm) {
        vm.setTaskComplete(true);
    }

    void initialized(VirtualBoxMachine vm) {
        vm.setTaskInitialized(true);
    }

  

}

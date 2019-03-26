/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class TaskManager {

    /**
     * true if the Task Manager is running
     */
    private static AtomicBoolean running = new AtomicBoolean(false);
    /**
     * List of all machines available
     */
    private static ArrayList<VirtualBoxMachine> machines = new ArrayList<>();
    /**
     * List of last task so we can report task changes
     */
    private static HashMap<VirtualBoxMachine, TaskType> lastList = new HashMap<>();

    private static AtomicBoolean testmode = new AtomicBoolean(false);

    
    /**
     * loops through machines. Finds machines which are not idle, and executes
     * tasks as long as the machine is ready.
     */
    private static void performTasks() {
        getMachines().stream().forEachOrdered((vm) -> {
            if (vm.isTaskComplete()){
                vm.removeCurrentTask();
            }
            initializeTask(vm);
        });
        getMachines().stream().filter((vm) -> (
                vm.isTaskInitialized() && 
                vm.isTaskReady() && !vm.isTaskComplete())
        ).forEachOrdered((vm) -> {
            runTemporaryTask(vm);
            executeCurrentTask(vm);
        });
    }
    
    
    
    
    /**
     * Starts the task manager
     */
    static public void start() {
        if (running.get()) {
            return;
        }
        ArrayList<VirtualBoxMachine> al = new VirtualBox().getMachinesList();
        al.forEach((mac) -> {
            machines.add((VirtualBoxMachine) mac);
        });
        Thread t = new Thread(() -> {
            while (running.get()) {
                try {
                    getStates();
                    sleep(500);
                    performTasks();
                } catch (Exception ex) {
                    Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.setName("Machine Monitor");
        t.setDaemon(true);
        t.start();
        running.set(true);
    }

    /**
     * executes a thread.sleep
     *
     * @param ms miliseconds to sleep
     */
    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stops the task manager;
     */
    synchronized static public void stop() {
        running.set(false);
    }

    

    /**
     * Runs the assigned task for the VM
     *
     * @param vm vm to run tasks upon
     */
    private static void executeCurrentTask(VirtualBoxMachine vm) {
        vm.getCurrentTask().getTask().exec(vm);
    }

    static void getStates() {
        HashMap<VirtualBoxMachine, TaskType> newList = new HashMap<>();
        getMachines().forEach((vm) -> {
            newList.put(vm, vm.getCurrentTask());
        });
    

        lastList.clear();
        lastList = newList;
    }

    /**
     * @return the machines
     */
    public static ArrayList<VirtualBoxMachine> getMachines() {
        return machines;
    }

    /**
     * @param aMachines the machines to set
     */
    public static void setMachines(ArrayList<VirtualBoxMachine> aMachines) {
        machines = aMachines;
    }

    private static void sendPapaUpdate(VirtualBoxMachine vm ){
        

    }

    private static void runTemporaryTask(VirtualBoxMachine vm) {
        if (vm.getTemporaryTask() != null) {
            TemporaryTaskHandler.doTemporaryTask(vm);
        }
    }

    public static VirtualBoxMachine getTaskMachine(VirtualBoxMachine vm) {
        for (VirtualBoxMachine myVm : getMachines()) {
            if (myVm.equals(vm)) {
                return vm;
            }
        }
        return null;
    }

    private static void initializeTask(VirtualBoxMachine vm) {
        if (!vm.isTaskInitialized()) {
            System.out.println("Performing "+vm.getCurrentTask().name()+ " on "+ vm.getUsername()+" "+vm.getGang());
            sendPapaUpdate(vm);
            vm.getCurrentTask().getTask().init(vm);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.babysharks.messaging.SendPapaAsynch;
import java.util.ArrayList;
import java.util.HashMap;
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
            if (vm.isTaskComplete()) {
                vm.performTaskListMaintenance();
            }
            if (vm.isTemporaryTaskAvailable() && !vm.isTemporaryTaskInitialized()) {
                vm.getTemporaryTask().getTask().init(vm);
            } else {
                initializeTask(vm);
            }
        });

        getMachines().stream().filter((vm) -> ((vm.isTaskInitialized() && vm.isTaskReady() && !vm.isTaskComplete())||
                vm.isTemporaryTaskAvailable()&& vm.isTemporaryTaskInitialized())
        ).forEachOrdered((vm) -> {
            if (vm.isTemporaryTaskAvailable()){
                vm.getTemporaryTask().getTask().exec(vm);
            } else {
                executeCurrentTask(vm);
            }
        });
    }

    /**
     * Starts the task manager
     */
    static public void start() {
        ArrayList<VirtualBoxMachine> al = new VirtualBox().getMachinesList();
        al.forEach((mac) -> {
            machines.add((VirtualBoxMachine) mac);
            TaskType resumeTask = TaskType.getTasks((VirtualBoxMachine) mac);
            mac.clearTasks();
            boolean supported = resumeTask.getTask().resume((VirtualBoxMachine) mac);
            if (supported) {
                System.out.println("Resuming " + resumeTask);
            } else if (!mac.getGang().equals("development")) {
                SendPapaAsynch.resumeWarning((VirtualBoxMachine) mac, resumeTask);
                mac.addTask(TaskType.POWEROFF);
            }
        });
        if (running.get()) {
            return;
        }
        Thread t = new Thread(() -> {

            while (running.get()) {
                try {
                    getStates();
                    sleep(1000);
                    performTasks();
                } catch (Exception ex) {
                    Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.setName("Machine Monitor");
        t.setDaemon(true);
        running.set(true);
        t.start();
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

    public static ArrayList<VirtualBoxMachine> getGangMachines(String gang) {
        ArrayList<VirtualBoxMachine> al = new ArrayList<>();
        for (VirtualBoxMachine m : getMachines()) {
            if (m.getGang().equals(gang)) {

                al.add(m);
                try {
                    m.updateStatuses();
                } catch (Exception ex) {
                }

            }
        }
        return al;
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
            System.out.println("Performing " + vm.getCurrentTask().name() + " on " + vm.getUsername() + " " + vm.getGang());
            vm.getCurrentTaskTime().setTimeStarted();
            vm.getCurrentTask().getTask().init(vm);
            TaskType.writeTaskToFile(vm);

        }
    }

}

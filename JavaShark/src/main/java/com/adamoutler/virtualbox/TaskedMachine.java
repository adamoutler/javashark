/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import com.adamoutler.virtualbox.task.TaskType;
import com.adamoutler.virtualbox.task.tasks.sentry.VMTaskData;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.SendPapaAsynch;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author adamo
 */
public class TaskedMachine {
    private LinkedList<TaskType> taskList = new LinkedList<>();
    private HashMap<TaskType, TaskTime> completedTasks = new HashMap<>();
    transient private TaskType temporaryTask;
    transient private boolean taskInitialized = false;
    transient private boolean taskReady = false;
    transient private boolean taskComplete = false;
    transient private VMTaskData vmTaskData = new VMTaskData();
    private String taskDescription = "";
    private TaskTime currentTaskTimes=new TaskTime();
    
    public TaskedMachine(){
    }
    
    
    synchronized public void performTaskListMaintenance() {
        //this is an idle task and nothing has been commanded
        if ((taskList.get(0).equals(TaskType.IDLE) || taskList.get(0).equals(TaskType.POWEROFF) && this.isTaskComplete()) && taskList.size() == 1){
            return;
        //this is a task to be removed and there are more tasks to be donet. 
        } else if ((this.taskList.get(0).equals(TaskType.IDLE)||this.taskList.get(0).equals(TaskType.POWEROFF)||this.isTaskComplete()) && taskList.size() > 1) {
            System.out.println("Done with task " + taskList.get(0).name());
            currentTaskTimes.setTimeComplete();
            this.completedTasks.put(taskList.get(0), getCurrentTaskTime());
            taskList.remove(0);
            this.currentTaskTimes=new TaskTime();
        //something happened and a task was not added but the last one was removed.
        } else if (taskList.size()==0){
            taskList.add(TaskType.IDLE);
        //the task to be removed is the last task in the list
        } else if (taskList.size()==1){
            taskList.add(TaskType.IDLE);
            System.out.println("Done with task " + taskList.get(0).name());
            taskList.remove(0);
        }
        
        //we will restart the task if this maintenace was called but the task was not complete.
        this.taskReady = false;
        this.taskComplete = false;
        this.taskInitialized = false;
        this.taskDescription = taskList.get(0).getTaskDescription();
    }

    synchronized public void forceRemoveCurrentTask() {
        if (taskList.size()<2){
            taskList.add(TaskType.IDLE);
        }
        System.out.println("Force-Stop task " + taskList.get(0).name());
        taskList.remove(0);
        this.taskReady = false;
        this.taskComplete = false;
        this.taskInitialized = false;
        this.taskDescription = taskList.get(0).getTaskDescription();
    }

    
    public List<TaskType> getTaskList(){
        return Collections.unmodifiableList(taskList);
    }
    
    /**
     * @return the currentTask
     */
    synchronized public TaskType getCurrentTask() {
        return taskList.get(0);
    }

    synchronized public void completeCurrentTask() {
        this.taskComplete=true;
        this.performTaskListMaintenance();
    }

    
    /**
     * @return the taskDescription
     */
    synchronized public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * @param taskDescription the taskDescription to set
     */
    synchronized public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

   
    /**
     * @return the taskComplete
     */
    synchronized public boolean isTaskComplete() {
        return taskComplete;
    }

    /**
     * @param taskComplete the taskComplete to set
     */
    synchronized public void setTaskComplete(boolean taskComplete) {
        this.taskComplete = taskComplete;
        if (taskList.size() > 1) {
            this.performTaskListMaintenance();
        }
    }

    /**
     * @return the data
     */
    synchronized public VMTaskData getTaskData() {
        return vmTaskData;
    }

    /**
     * @param data the data to set
     */
    synchronized public void setData(VMTaskData data) {
        this.vmTaskData = data;
    }

    /**
     * @return the initalized
     */
    synchronized public boolean isTaskInitialized() {
        return taskInitialized;
    }

    /**
     * @param initalized the initalized to set
     */
    synchronized public void setTaskInitialized(boolean initalized) {
        this.taskInitialized = initalized;
    }

    /**
     * @return the nextTask
     */
    synchronized public TaskType getNextTask() {
        if (taskList.size() > 1) {
            return taskList.get(1);
        } else {
            return TaskType.IDLE;
        }
    }

    synchronized public void addTask(TaskType task) {
        if (task.equals(TaskType.SKIP)) {
            this.forceRemoveCurrentTask();
            this.performTaskListMaintenance();
        } else if (task.equals(TaskType.CLEAR)){
            while (taskList.size()>1){
                this.forceRemoveCurrentTask();
            }
            this.forceRemoveCurrentTask();
        } else {
            this.taskList.add(task);
            if (taskList.get(0).equals(TaskType.IDLE) || taskList.get(0).equals(TaskType.POWEROFF) || this.taskComplete) {
                this.performTaskListMaintenance();
            }
        }
    }

    /**
     * @return the temporaryTask
     */
    synchronized public TaskType getTemporaryTask() {
        return temporaryTask;
    }

    /**
     * @param temporaryTask the temporaryTask to set
     */
    synchronized public void setTemporaryTask(TaskType temporaryTask) {
        this.temporaryTask = temporaryTask;
    }
    /**
     * @return the taskReady
     */
    public boolean isTaskReady() {
        return taskReady;
    }

    /**
     * @param taskReady the taskReady to set
     */
    public void setTaskReady(boolean taskReady) {
        this.taskReady = taskReady;
    }

    /**
     * @return the taskTime
     */
    public TaskTime getCurrentTaskTime() {
        return currentTaskTimes;
    }
    
    
    public void clearTasks(){
        this.addTask(TaskType.CLEAR);
    }
    
    public void writeBabySharksDebug(VirtualBoxMachine vm,String comment){
        String name=VirtualBoxScreenShot.writeScreenshot(vm, "/var/www/baby-sharks/screenshots");
        SendPapaAsynch.sendToPapa(new Message().setCommand("screenshot").setData(new MessageData().setGang(vm.getGang()).setUsername(vm.getUsername()).setUrl("https://javashark.baby-sharks.com/screenshots/"+name).setReason(comment+" Here's a screenshot "+"https://javashark.baby-sharks.com/screenshots/"+comment)));
    }
}

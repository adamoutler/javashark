/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import com.adamoutler.virtualbox.task.tasks.sentry.VMTaskData;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.virtualbox.task.TaskType;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public class VirtualBoxMachine {

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

    private String machineName;
    private String gang;
    private String user;
    private String portNumber;
    private String adbName;
    private String rdpPort;
    private ScreenData screenID = ScreenData.BLACKSCREEN;
    private String screenDescription;
    private LinkedList<TaskType> taskList = new LinkedList<>();
    HashMap<TaskType, Long> completedTasks = new HashMap<>();
    private TaskType temporaryTask = TaskType.IDLE;
    private boolean taskInitialized = false;
    private boolean taskReady = false;
    private boolean taskComplete = false;
    private String taskDescription = "";
    transient private byte[] screenshot = new byte[]{};
    private VMTaskData vmTaskData = new VMTaskData();

    public VirtualBoxMachine(String machineName) {
        taskList.add(TaskType.IDLE);
        String[] machineNameAr = machineName.split("_");
        gang = machineNameAr[0];
        user = machineNameAr[1];
        portNumber = machineNameAr[2];
        adbName = "127.0.0.1:" + portNumber;
        rdpPort = machineNameAr[3];
        getVMName();
    }

    public VirtualBoxMachine(String gang, String user, String port, String rdp) {
        taskList.add(TaskType.IDLE);
        this.gang = gang;
        this.user = user;
        this.portNumber = port;
        adbName = "127.0.0.1:" + portNumber;
        rdpPort = rdp;
        this.getVMName();
    }

    public BufferedImage screenshot() {
        try {
            return VirtualBoxScreenShot.getScreenshot(this);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    final public String getVMName() {
        machineName = getGang() + "_" + getUsername() + "_" + getPortNumber() + "_" + getRdpPort();
        return machineName;
    }

    /**
     * @return the gang
     */
    public String getGang() {
        return gang;
    }

    /**
     * @param gang the gang to set
     */
    public void setGang(String gang) {
        this.gang = gang;
        getVMName();
    }

    /**
     * @return the user
     */
    public String getUsername() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
        getVMName();
    }

    /**
     * @return the portNumber
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * @return the portNumber
     */
    public int getPortNumberInt() {
        return Integer.parseInt(portNumber);
    }

    /**
     * @param portNumber the portNumber to set
     */
    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
        getVMName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = "\n";
        sb.append("VM Name: ").append(this.getVMName()).append(n)
                .append("User: ").append(this.user).append(n)
                .append("Gang: ").append(gang).append(n)
                .append("ADB address: ").append(this.getAdbName()).append(n)
                .append("ADB Port: ").append(portNumber).append(n)
                .append("RDP port: ").append(this.getRdpPort());
        return sb.toString();
    }

    /**
     * @return the adbName
     */
    public String getAdbName() {
        return adbName;
    }

    /**
     * @param adbName the adbName to set
     */
    public void setAdbName(String adbName) {
        this.adbName = adbName;
        getVMName();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof VirtualBoxMachine) {
            VirtualBoxMachine vm = (VirtualBoxMachine) o;
            if (hashCode() == vm.hashCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.gang);
        hash = 23 * hash + Objects.hashCode(this.user);
        hash = 23 * hash + Objects.hashCode(this.portNumber);
        hash = 23 * hash + Objects.hashCode(this.adbName);
        return hash;
    }

    /**
     * @return the rdpPort
     */
    public String getRdpPort() {
        return rdpPort;
    }

    /**
     * @param rdpPort the rdpPort to set
     */
    public void setRdpPort(String rdpPort) {
        this.rdpPort = rdpPort;
        getVMName();
    }


    /**
     * @param screenID the screenID to set
     */
    public void setScreenID(ScreenData screenID) {
        this.screenID = screenID;
    }

    /**
     * @return the screenDescription
     */
    public String getScreenDescription() {
        return screenDescription;
    }

    /**
     * @param screenDescription the screenDescription to set
     */
    public void setScreenDescription(String screenDescription) {
        this.screenDescription = screenDescription;
    }

    synchronized public void removeCurrentTask() {
        if ((taskList.get(0).equals(TaskType.IDLE) || taskList.get(0).equals(TaskType.POWEROFF) && this.isTaskComplete()) && taskList.size() == 1){
            return;
        } else if (this.isTaskComplete() && taskList.size() > 1) {
            System.out.println("Done with task " + taskList.get(0).name());
            this.completedTasks.put(taskList.get(0), System.currentTimeMillis());
            taskList.remove(0);
        } else if (taskList.size()==0){
            System.out.println("Attempted to remove non-existant task");
        } else if (taskList.size()==1){
            taskList.add(TaskType.IDLE);
            System.out.println("Done with task " + taskList.get(0).name());
            taskList.remove(0);
        }
        if (taskList.isEmpty()) {
            taskList.add(TaskType.IDLE);
        }
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

    /**
     * @return the currentTask
     */
    synchronized public TaskType getCurrentTask() {
        return taskList.get(0);
    }

    synchronized public void completeCurrentTask() {
        this.removeCurrentTask();
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
     * @return the screenshot
     */
    synchronized public byte[] getLastScreenshot() {
        return screenshot;
    }

    /**
     * @param screenshot the screenshot to set
     */
    synchronized public void setScreenshot(byte[] screenshot) {
        this.screenshot = screenshot;
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
            this.removeCurrentTask();
        }
    }

    /**
     * @return the data
     */
    synchronized public VMTaskData getData() {
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
            this.removeCurrentTask();
        } else {
            this.taskList.add(task);
            if (taskList.get(0).equals(TaskType.IDLE) || taskList.get(0).equals(TaskType.POWEROFF) || this.taskComplete) {
                this.removeCurrentTask();
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

    synchronized public String adbShell(String[] commands) {
        StringBuilder sb = new StringBuilder();
        String cmd = "";
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < commands.length; i++) {
            switch (i) {
                case 0:
                    cmd = commands[i];
                    break;
                default:
                    al.add(commands[i]);
            }
        }
        try {
            BufferedInputStream is = new BufferedInputStream(getJadb().execute(cmd, al.toArray(new String[al.size()])));
            Thread.sleep(50);
            while (is.available() > 0) {
                sb.append((char) is.read());
            }

        } catch (IOException | JadbException ex) {
            Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, "Couldn't send adb command", ex);
        } catch (NullPointerException | InterruptedException ex) {
        }
        return sb.toString();
    }

    public ScreenData getScreen() {
        this.screenID= ScreenData.identifyScreen(this);
        screenDescription=screenID.getDescription();
        return screenID;
    }

    public JadbDevice getJadb() {
        JadbDevice returnValue = null;
        try {
            JadbConnection jadbc = new JadbConnection();
            returnValue = getJadbDevice(jadbc);
            if (returnValue == null) {
                new JadbConnection().connectToTcpDevice(new InetSocketAddress("127.0.0.1", this.getPortNumberInt()));
                returnValue = getJadbDevice(jadbc);
            }

        } catch (IOException | JadbException | ConnectionToRemoteDeviceException ex) {
            Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, "port is not available. Did you start ADB? Machine down?", ex);
        }
        return returnValue;
    }

    private JadbDevice getJadbDevice(JadbConnection jadbc) throws IOException, JadbException {
        List<JadbDevice> list = jadbc.getDevices();
        for (JadbDevice jadb : list) {
            if (jadb != null && jadb.getSerial().equals(this.adbName)) {
                return jadb;
            }
        }
        return null;
    }

    public void updateStatuses(){
        this.getScreen();
        
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.task.TaskType;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

/**
 *
 * @author adamo
 */
public class VirtualBoxMachine extends TaskedMachine {

    private String machineName;
    final private String gang;
    final private String user;
    private boolean poweredOn;
    transient private String portNumber;
    transient private String adbName;
    transient private String rdpPort;
    private ScreenData screenID = ScreenData.OFF;
    private String screenDescription;
    private String adamoutlerchat = "";
    transient private byte[] screenshot = new byte[]{};
    private transient Map<String, String> hashes;
    transient private boolean killThread;
    private boolean temporaryTaskInitialized;

    public VirtualBoxMachine(String machineName) {

        String[] machineNameAr = machineName.split("_");
        gang = machineNameAr[0];
        user = machineNameAr[1].replaceAll("[^a-zA-Z0-9]+", "");

        portNumber = machineNameAr[2];
        adbName = "127.0.0.1:" + portNumber;
        rdpPort = machineNameAr[3];
        getVMName();
        if (this.isPoweredOn()) {
            this.addTask(TaskType.IDLE);
        } else {
            this.addTask(TaskType.POWEROFF);
        }
    }

    public VirtualBoxMachine(String gang, String user, String port, String rdp) {
        this.gang = gang;
        this.user = user.replaceAll("[^a-zA-Z0-9]+", "");
        ;
        this.portNumber = port;
        adbName = "127.0.0.1:" + portNumber;
        rdpPort = rdp;
        this.getVMName();
    }

    public File logUnidentifiedScreen(String name) {
        try {
            String filename = System.getProperty("java.io.tmpdir") + "/" + name + System.currentTimeMillis() + ".png";
            System.out.println("Could not identify screen!  Wrote \"" + filename + "\" screen to logs");
            File f=new File(filename);
            FileOutputStream fos = new FileOutputStream(f);
            ImageIO.write(screenshot(), "PNG", fos);
            return f;
            
        } catch (IOException ex) {
            Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
    
     public String writeScreenToWebServer(String name) {
        try {
            String filename =  name + System.currentTimeMillis() + ".png";
            System.out.println("Could not identify screen!  Wrote \"/var/www/baby-sharks/screenshots/" + filename + "\" screen to logs");
            File f=new File("/var/www/baby-sharks/screenshots/"+filename);
            FileOutputStream fos = new FileOutputStream(f);
            ImageIO.write(screenshot(), "PNG", fos);
            return "https://javashark.baby-sharks.com/screenshots/"+filename;
            
        } catch (IOException ex) {
            Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
    
    transient List<BufferedImage> images = null;

    public void DEBUGaddImages(BufferedImage... image) {
        if (images==null){
            images=new LinkedList<>();
        }
        Collections.addAll(images, image);
    }

    public void DEBUGclearImages() {
        images = null;
    }

    public BufferedImage screenshot() {
        if (images == null) {
            try {
                return VirtualBoxScreenShot.getScreenshot(this);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            BufferedImage bi = images.get(0);
            Collections.rotate(images, -1);
            return bi;

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
     * @return the user
     */
    public String getUsername() {
        return user;
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
        this.screenDescription = screenID.getDescription();
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

    synchronized public String adbShell(String... commands) {
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
            sleep(50);

            while (is.available() > 0) {
                sb.append((char) is.read());
            }

        } catch (IOException | JadbException ex) {
            System.out.println("Error Couldn't send adb command... " + ex.getMessage());
        } catch (NullPointerException ex) {
        }
        return sb.toString();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(VirtualBoxMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ScreenData getScreen() {
        try {
            if (this.isPoweredOn()) {
                screenID = ScreenData.identifyScreen(this);
            } else {
                screenID = ScreenData.OFF;
            }
        } catch (Exception ex) {
            return ScreenData.UNRESPONSIVE;
        }
        screenDescription = screenID.getDescription();
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
            System.out.println("Attempted to connect to " + this.getPortNumber() + " but machine was down or ADB was not started. Sleeping for 1 second");
            this.sleep(1000);

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

    public void updateStatuses() {
        this.getScreen();

    }

    /**
     * @return the poweredOn
     */
    public boolean isPoweredOn() {
        poweredOn = new VirtualBox().isMachineOn(this);
        return poweredOn;
    }

    /**
     * @param poweredOn the poweredOn to set
     */
    public void setPoweredOn(boolean poweredOn) {
        this.poweredOn = poweredOn;
    }

    public void tap(XYCoords coords) {
        this.adbShell("input", "tap", coords.getXStr(), coords.getYStr());
    }

    public void longTap(XYCoords coords) {
        this.adbShell("input", "swipe", coords.getXStr(), coords.getYStr(), coords.getXStr(), coords.getYStr());
    }

    /**
     * @return the hashes
     */
    public Map<String, String> getHashes() {
        return hashes;
    }

    /**
     * @param hashes the hashes to set
     */
    public void setHashes(Map<String, String> hashes) {
        if (hashes != null) {
            this.hashes = hashes;
        } else {
            this.hashes = new HashMap<>();
        }
    }

    /**
     * @return the killThread
     */
    public boolean isKillThread() {
        return killThread;
    }

    /**
     * @param killThread the killThread to set
     */
    public void setKillThread(boolean killThread) {
        this.killThread = killThread;
    }

    public void resumeSentry() {

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import com.adamoutler.ADB;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
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
public class VirtualBoxControl {

    JadbDevice dev;

    final static String BADAZZAUTOMATIONJAR = new File("/opt/jbot/BadazzAutomation-1.0-SNAPSHOT-jar-with-dependencies.jar").getAbsolutePath();
    Process sentryProcess;
    private BufferedReader error;
    private BufferedReader in;
    private final VirtualBoxMachine vm;

    public VirtualBoxControl(VirtualBoxMachine vm) {

        this.vm = vm;
    }

    public  void main(String[] args) {

//        VirtualBoxMachine vm = new VirtualBox().getUserMachine("development", "adamoutler");
//        VirtualBoxControl vbox = new VirtualBoxControl(vm);
//        //vbox.stopVM();
//        vbox.startVirtualMachine();
//        vbox.startCats();
//        vbox.startSentry(9999999);

    }

    /**
     * Starts the machine. Waits for a specified period of time. verifies the
     * machine is operational. exits if any error is detected.
     *
     * @return true if machine is up
     */
    public boolean startVirtualMachine() {
        try {
            System.out.println("Starting VM " + vm);
            final LinkedList<String> args = new LinkedList<>();
            args.add(VirtualBox.VBOX);
            args.add("startvm");
            args.add(vm.getVMName());
            args.add("--type");
            args.add("headless");
            String executed = executeProcess(args, 4000);
            if (!executed.contains("started")) {
                return false;
            }
            this.sleepMillis(4000); //sleep for 45 seconds

            args.add("adb");
            args.add("connect");
            args.add(vm.getVMName());
            this.executeProcess(args, 5);

            new JadbConnection().connectToTcpDevice(new InetSocketAddress("127.0.0.1",vm.getPortNumberInt()));
            List l=new JadbConnection().getDevices();
            //todo verify device
            return true;
        } catch (IOException | JadbException | ConnectionToRemoteDeviceException ex) {
            System.err.println("Problem while connecting to VM during startup");
            return false;
        }

    }
    // vboxmanage startvm "${vm}" --type headless
    // sleep 45
    // adb connect
    // verify connection

    /**
     * Starts CATS. waits for a specified period of time. Verifies CATS is
     * operational.
     *
     * @return true if CATS is operational
     */
    public boolean startCats() {
        try {
            System.out.println("Starting CATS on " + vm.toString());
            System.out.println(ADB.runAdbShellCommand(1, getDevice(), "am", new String[]{"force-stop", "com.zeptolab.cats.google"}));
            System.out.println(ADB.runAdbShellCommand(1, getDevice(), "am", new String[]{"start", "com.zeptolab.cats.google/com.zeptolab.cats.CATSActivity"}));
            String s = ADB.runAdbShellCommand(1, getDevice(), "ps", new String[]{"com.zeptolab.cats.google"});
            System.out.println(s);
            if (!s.contains("zeptolab.cats")) {
                return false;
            }
            this.sleepMillis(40000);
            this.performCorrectiveActionOnVM();
            return true;
        } catch (IOException | JadbException ex) {
            Logger.getLogger(VirtualBoxControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    // adb shell am force-stop  com.zeptolab.cats.google;
    // adb connect $adbdevice;
    // adb shell am start  com.zeptolab.cats.google/com.zeptolab.cats.CATSActivity;    
    // adb -s $adbdevice shell ps com.zeptolab.cats.google   check if this contains *zeptolab.cats*

    /**
     * Terminates the machine immediately. Shutdown and kill process are issued
     * simultaniously.
     *
     *
     * @return true if stopped
     */
    public boolean stopVM() {
        System.out.println("Stopping VM " + vm);
        LinkedList<String> args = new LinkedList<>();
        vm.setKillThread(false);
        args.add(VirtualBox.VBOX);
        args.add("controlvm");
        args.add(vm.getVMName());
        args.add("poweroff");
        String executed = executeProcess(args, 4000);
        if (System.getProperty("os.name").contains("nux")) {
            args = new LinkedList<>();
            args.add("kill");
            args.add("-9");
            args.add("$(top -b -n 1|grep VBoxHeadless|cut -d' ' -f2)");
            executeProcess(args, 4000);
            return true;
        }

        return !(!executed.contains("0%") && !executed.contains("not currently"));
    }

    // adb kill-server
    // kill -9 $(top -b -n 1|grep VBoxHeadless|cut -d' ' -f2) || echo vboxheadless shutdown
    // kill -9 $(top -b -n 1|grep VirtualBox |cut -d' ' -f2) || echo virtualbox shutdown
    // vboxmanage controlvm "${vm}" poweroff|| echo machine is already down;
    /**
     * Verifies CATS is operational.
     *
     * @return true if CATS is runing in foreground.
     * @throws java.io.IOException
     * @throws se.vidstige.jadb.JadbException
     */
    public boolean verifyCatsOperational() throws IOException, JadbException {
        BufferedInputStream bis = (BufferedInputStream) getDevice().execute("shell", "ps com.zeptolab.cats.google");
        sleepMillis(100);
        return streamToString(bis).contains("zeptolab.cats");

    }
    // adb -s $adbdevice shell ps com.zeptolab.cats.google   check if this contains *zeptolab.cats*

    private void sleepMillis(int sleepTime) {
        try {
            //todo check timing
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(VirtualBoxControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String streamToString(BufferedInputStream bis) {
        Scanner s = new Scanner(bis).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

    /**
     * General corrective action to be taken before scanning or other event
     * occurs.
     *
     * @throws java.io.IOException
     * @throws se.vidstige.jadb.JadbException
     */
    public void performCorrectiveActionOnVM() throws IOException, JadbException {

        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"keyevent", "KEYCODE_BACK"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"keyevent", "KEYCODE_BACK"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"keyevent", "KEYCODE_BACK"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"keyevent", "KEYCODE_BACK"});
        this.sleepMillis(1000);
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"tap", "535", "651"});
        this.sleepMillis(1000);
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"tap", "899", "87"});
        this.sleepMillis(4000);
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"tap", "526", "480"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"tap", "526", "480"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"tap", "526", "480"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"keyevent", "KEYCODE_BACK"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"input", "keyevent", "KEYCODE_BACK"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"input", "keyevent", "KEYCODE_BAC"});
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"input", "keyevent", "KEYCODE_BACK"});
        this.sleepMillis(1000);
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"tap", "535", "651"});
        this.sleepMillis(1000);
        ADB.runAdbShellCommand(1, this.getDevice(), "input", new String[]{"input", "tap", "899", "87"});

    }
    // adb -s "$adbdevice" shell "input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; sleep 1; input tap 535 651; sleep 1; input tap 899 87; sleep 4; input tap 526 480; input tap 526 480; input tap 526 480; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; input keyevent KEYCODE_BACK; sleep 1; input tap 535 651; sleep 1; input tap 899 87;"

    private JadbDevice getDevice() throws IOException, JadbException {
        doConnection();

        if (dev != null) {
            return dev;
        }
        List<JadbDevice> devs = new JadbConnection().getDevices();
        for (JadbDevice d : devs) {
            if (d.getSerial().trim().equals(vm.getAdbName().trim())) {
                dev = d;
                break;
            }
        }
        return dev;
    }

    /**
     * starts the sentry
     *
     * @param time time to run sentry
     * @return true if sentry was started and continues to run after initial
     * startup.
     */
    public boolean startSentry(long time) {
        final LinkedList<String> args = new LinkedList<>();
        args.add(System.getProperty("java.home") + "/bin/java");
        args.add("-jar");
        args.add(BADAZZAUTOMATIONJAR);
        args.add("--vm=" + vm.getVMName());
        args.add("--serial=" + vm.getAdbName());
        args.add("--gang=" + vm.getGang());
        args.add("--duration=" + time);
        args.add("--username=" + vm.getUsername());
        return executeSentryProcess(args);
    }

    String executeProcess(LinkedList<String> args, int timeToWait) {
        try {
            final Runtime re = Runtime.getRuntime();
            Process process = re.exec(args.toArray(new String[args.size()]));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(process.getInputStream()));
            //todo test timing
            process.waitFor(timeToWait, TimeUnit.SECONDS);
            StringBuilder sb = new StringBuilder();
            readBufferedReader(stdin, sb);
            readBufferedReader(stderr, sb);
            return sb.toString();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(VirtualBoxControl.class.getName()).log(Level.SEVERE, null, ex);

        }
        return "";
    }

    private void readBufferedReader(BufferedReader in1, StringBuilder sb) throws IOException {
        while (in1.ready()) {
            sb.append(in1.readLine());
        }
    }

    boolean executeSentryProcess(LinkedList<String> args) {

        try {
            final Runtime re = Runtime.getRuntime();
            sentryProcess = re.exec(args.toArray(new String[args.size()]));
            this.error = new BufferedReader(new InputStreamReader(sentryProcess.getErrorStream()));
            this.in = new BufferedReader(new InputStreamReader(sentryProcess.getInputStream()));

            //todo test timing
            this.sleepMillis(1000);

            if (sentryProcess.isAlive()) {
                return true;
            }

        } catch (IOException ex) {
            Logger.getLogger(VirtualBoxControl.class.getName()).log(Level.SEVERE, null, ex);

        }
        return false;
    }

    // bash -c "/usr/bin/java -jar /opt/jbot/BadazzAutomation-1.0-SNAPSHOT-jar-with-dependencies.jar fast-ownership --vm=\"$vm\" --serial=\"$adbdevice\" --gang=\"$gang\" --duration=\"$duration\" --username=\"$username\""
    /**
     * Monitors the automation process. When complete a timer is triggered to
     * clean up the machine when/if required. This must provide the ability to
     * resume operations if a sentry is reactivated.
     *
     * If 10 minutes elapses and the sentry has not been reactivated, the VM
     * will be killed.
     *
     */
    public void monitorAutomationProcess() {
        Thread t = new Thread(() -> {
            while (this.sentryProcess.isAlive()) {
                this.sleepMillis(60000);
            }
            this.sleepMillis(60000);
            if (!sentryProcess.isAlive()) {
                this.stopVM();
            }

        });
    }

    /**
     * gets the vm name format from a gang and username
     *
     * @param gang user's gang
     * @param user user's telegram name
     * @return vm name for user machine.
     */
    final public String getVMName(String gang, String user) {
        return (gang + "_" + user).replaceAll("[^a-zA-Z-_]", "");
    }

    private void doConnection() {
        try {
            final LinkedList<String> args = new LinkedList<>();
            new JadbConnection().connectToTcpDevice(new InetSocketAddress("127.0.0.1", vm.getPortNumberInt()));
        } catch (IOException | JadbException | ConnectionToRemoteDeviceException ex) {
            Logger.getLogger(VirtualBoxControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

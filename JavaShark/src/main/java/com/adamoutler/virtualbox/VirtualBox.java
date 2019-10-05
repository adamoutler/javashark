/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author adamo
 */
public class VirtualBox {

    final public static String VBOX = "VBoxManage";
    private static ArrayList<VirtualBoxMachine> machines = new ArrayList<>();

    public void main(String[] args) {
        List<VirtualBoxMachine> allMachines = new VirtualBox().getMachinesList();
        System.out.println("All machines : " + allMachines);
        List<VirtualBoxMachine> gang = new VirtualBox().getGangMachines("Android");
        System.out.println("Searching for gang 'Android' : " + gang);
        VirtualBoxMachine usermachine = new VirtualBox().getUserMachine("Debian", "esting");
        System.out.println("Searching for gang 'Debian', user 'esting : " + usermachine);
        List<VirtualBoxMachine> activeMachines = new VirtualBox().getActiveMachines();
        System.out.println("Active Machines : " + activeMachines);
        List<VirtualBoxMachine> activeGangMachines = new VirtualBox().getGangMachines("development");
        System.out.println("Active gang 'adam' Machines : " + activeGangMachines);

    }

    public VirtualBoxMachine getUserMachine(String gang, String user) {
        String alphaOnly = user.replaceAll("[^a-zA-Z0-9]+", "");
        for (VirtualBoxMachine machine : getMachinesList()) {
            if (machine.getGang().toLowerCase().startsWith(gang.toLowerCase())
                    && machine.getUsername().toLowerCase().contains(alphaOnly.toLowerCase())) {
                return machine;

            }
        }
        System.out.println("User machine not found!");
        return null;
    }

    public ArrayList<VirtualBoxMachine> getGangMachines(String gang) {
        ArrayList<VirtualBoxMachine> returnValue = new ArrayList<>();
        for (VirtualBoxMachine m : getMachinesList()) {
            if (m.getGang().toLowerCase().startsWith(gang.toLowerCase())) {
                m.updateStatuses();
                returnValue.add(m);
            }
        }
        return returnValue;
    }

    public ArrayList<VirtualBoxMachine> getActiveMachines() {
        return getMachinesList();
    }

    public ArrayList<VirtualBoxMachine> getMachinesList() {
        if (machines.size() < 1) {
            ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "list", "vms"});
            ArrayList<VirtualBoxMachine> al = getFormattedMachines(pb);
            machines = al;
        }
        return machines;
    }

    public boolean isMachineOn(VirtualBoxMachine vm) {

        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "list", "runningvms"});
        pb.redirectError();
        try {
            Process p = pb.start();
            try (BufferedInputStream is = new BufferedInputStream(p.getInputStream())) {
                p.waitFor(1, TimeUnit.SECONDS);
                StringBuilder sb = new StringBuilder();

                while (is.available() > 0) {
                    sb.append((char) is.read());
                }
                if (sb.toString().contains(vm.getVMName())) {
                    return true;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(VirtualBox.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(VirtualBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private ArrayList<VirtualBoxMachine> getFormattedMachines(ProcessBuilder pb) {
        ArrayList<VirtualBoxMachine> al = new ArrayList<>();
        String value = getStringValueFromProcess(pb);
        processVMList(value, al);
        return al;
    }

    private String getStringValueFromProcess(ProcessBuilder pb) {
        try {
            pb.redirectError();
            Process p = pb.start();
            InputStream is = p.getInputStream();
            p.waitFor(5, TimeUnit.SECONDS);
            byte[] bytes = IOUtils.toByteArray(is);
            return new String(bytes);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(VirtualBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    void processVMList(String value, ArrayList<VirtualBoxMachine> recognizedMachines) {
        String[] vmList = value.split("\n");
        if (vmList.length == 0) {
            return;
        }
        for (String vmListLine : vmList) {
            if (!vmListLine.contains("\"") || !vmListLine.contains("_") || vmListLine.split("_").length != 4) {
                continue;
            }
            String[] vmColumns = vmListLine.split("\"");
            recognizedMachines.add(new VirtualBoxMachine(vmColumns[1]));
        }

    }
}

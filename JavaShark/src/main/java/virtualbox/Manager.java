/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualbox;

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
public class Manager {

    final static String VBOX = "VBoxManage";

//    public static void main(String[] args) {
//        List<String> allMachines = new Manager().getMachinesList();
//        System.out.println("All machines : " + allMachines);
//        List<String> gang = new Manager().getGangMachines("Android");
//        System.out.println("Searching for gang 'Android' : " + gang);
//        String usermachine = new Manager().getUserMachine("Debian", "esting");
//        System.out.println("Searching for gang 'Debian', user 'esting : " + usermachine);
//        List<String> activeMachines=new Manager().getActiveMachines();
//        System.out.println("Active Machines : "+activeMachines);
//        List<String> activeGangMachines=new Manager().getActiveGangMachines("adam");
//         System.out.println("Active gang 'adam' Machines : "+activeGangMachines);
//
//
//    }
    public String getUserMachine(String gang, String user) {
        for (String s : getMachinesList()) {
            if (s.toLowerCase().startsWith(gang.toLowerCase()) && s.contains(user.toLowerCase())) {
                return s.replaceFirst(gang.toLowerCase(), "");

            }
        }
        return "null";
    }

    public List<String> getGangMachines(String gang) {
        ArrayList returnValue = new ArrayList<>();
        for (String s : getMachinesList()) {
            if (s.toLowerCase().startsWith(gang.toLowerCase())) {
                returnValue.add(s.toLowerCase());
            }
        }
        return returnValue;
    }

    public List<String> getActiveGangMachines(String gang) {
        ArrayList returnValue = new ArrayList<>();
        for (String s : getActiveMachines()) {
            if (s.toLowerCase().startsWith(gang.toLowerCase())) {
                returnValue.add(s.replaceFirst(gang, "").toLowerCase());
            }
        }
        return returnValue;
    }

    public List<String> getActiveMachines() {
        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "list", "runningvms"});
        ArrayList<String> al = getFormattedMachines(pb);
        return al;
    }

    public List<String> getMachinesList() {
        ProcessBuilder pb = new ProcessBuilder(new String[]{VBOX, "list", "vms"});
        ArrayList<String> al = getFormattedMachines(pb);
        return al;
    }

    private ArrayList<String> getFormattedMachines(ProcessBuilder pb) {
        ArrayList<String> al = new ArrayList<>();
        String value = getStringValueFromProcess(pb);
        processVMList(value, al);
        return al;
    }

    private String getStringValueFromProcess(ProcessBuilder pb) {
        try {
            Process p = pb.start();
            InputStream is = p.getInputStream();
            p.waitFor(5, TimeUnit.SECONDS);
            byte[] bytes = IOUtils.toByteArray(is);
            return new String(bytes);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private void processVMList(String value, ArrayList<String> al) {
        String[] vms = value.split("\n");
        if (vms.length == 0) {
            return;
        }
        for (String vm : vms) {
            if (!vm.contains("\"")) {
                return;
            }
            String[] row = vm.split("\"");
            al.add(row[1].toLowerCase());
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.Prop;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class PapaAsync {

    public PapaAsync() {

    }

    private static long time = 0;

    static {
        if (time == 0) {
            time = System.currentTimeMillis();
        }
    }
    final private static String UNCLEURL = Prop.getProp("devURL");
    final private static String PPAPAURL = Prop.getProp("productionURL");

//    public static void main(String[] args){
//        Message m=new Message().setCommand("test");
//        m.getData().setReason("testing");
//        new VirtualBox().getMachinesList().get(0);
//        m.setData(new MessageData().setActive(new VirtualBox().getMachinesList()));
//        SendPapaAsynch.sendToPapa(m);
//        //onlineWarning(new VirtualBox().getMachinesList().get(0));
//    }
    public static void onlineWarning(VirtualBoxMachine vm, boolean unitTesting) {
        vm.updateStatuses();
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        Message message = getM(vm).setData(
                getData(vm).setReason("online"))
                .setCommand("sentry");
        if (unitTesting) {
            message.getData().setReason("UNIT TESTING");
        }
        HashedMessage hm = new HashedMessage(message);

        System.out.println(send(hm));

    }

    public static void enlistWarning(VirtualBoxMachine vm, String reason) {
        HashedMessage hm = null;
        
            hm = new HashedMessage(getM(vm).setCommand("enlist")
                    .setData(getData(vm).setReason(reason)));
        

        String s = send(hm);
        System.out.println(s);

    }
    
    public static void sentryReasonWarning(VirtualBoxMachine vm, String reason) {
        HashedMessage hm = null;
        if (System.currentTimeMillis() > time + 10000) {
            hm = new HashedMessage(getM(vm).setCommand("sentry")
                    .setData(getData(vm).setReason(reason)));
        } else {
            hm = new HashedMessage(getM(vm).setCommand("resumesentry")
                    .setData(getData(vm).setReason(reason)));
        }

        String s = send(hm);
        System.out.println(s);

    }

    public static void resumeWarning(VirtualBoxMachine vm, TaskType resumeTask) {
        if (!resumeTask.equals(TaskType.POWEROFF) && !resumeTask.equals(TaskType.IDLE)) {
            HashedMessage hm = new HashedMessage(getM(vm).setCommand("resume")
                    .setData(getData(vm).setReason("Could not resume " + resumeTask)));
            String s = send(hm);
            System.out.println(s);
        }
    }

    public static void sendToAdam(Message m) {
        m.setCommand("adam").setData(m.getData());
        HashedMessage hm = new HashedMessage(m);
        String s = send(hm);
        System.out.println(s);

    }

    private static Message getM(VirtualBoxMachine vm) {
        Message m = new Message();
        return m;
    }

    private static MessageData getData(VirtualBoxMachine vm) {
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        return new MessageData().setAvailable(vms).setGang(vm.getGang()).setUsername(vm.getUsername());
    }

    public static String send(Message message) {
        HashedMessage hm = new HashedMessage(message);
        return send(hm);
    }

    public static String send(HashedMessage hm) throws RestClientException, HttpServerErrorException {
        String json = hm.toJson();
        System.err.println(json);
        String gang = getGangFromMessage(hm);
        String s;
        if (gang == null || gang.equals("staging") || gang.equals("development")) {
            System.out.println("Sending message to Uncle Shark for gang:" + gang);
            s = getRestTemplate().postForObject(UNCLEURL, json, String.class);
        } else {
            System.out.println("Sending message to Papa Shark for gang:" + gang);
            s = getRestTemplate().postForObject(PPAPAURL, json, String.class);
        }
        System.out.println("Received: " + s);
        return s;
    }

    private static String getGangFromMessage(HashedMessage hm) {
        String gang = null;
        try {
            gang = hm.getMessage().getData().getGang();
            if (gang == null) {
                throw new Exception();
            }
        } catch (Exception ex) {
            try {
                gang = hm.getMessage().getData().getAvailable().get(0).getGang();
            } catch (Exception exx) {
                gang = "none";
            //Logger.getLogger(PapaAsync.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

        return gang;
    }

    public static RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return template;
    }

}

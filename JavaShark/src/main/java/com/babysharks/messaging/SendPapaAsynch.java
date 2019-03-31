/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.javashark.MainApp;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.util.ArrayList;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class SendPapaAsynch {

    final private static String PAPAURL = "https://catsbot-201602.appspot.com/adam_api/secret_shark_path_shhhh/async";
    
//    public static void main(String[] args){
//        Message m=new Message().setCommand("test");
//        m.getData().setReason("testing");
//        new VirtualBox().getMachinesList().get(0);
//        m.setData(new MessageData().setActive(new VirtualBox().getMachinesList()));
//        SendPapaAsynch.sendToPapa(m);
//        //onlineWarning(new VirtualBox().getMachinesList().get(0));
//    }
    
    
    public static void onlineWarning(VirtualBoxMachine vm,boolean unitTesting) {
        vm.updateStatuses();
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        Message message=getM(vm).setData(
                getData(vm).setReason("online"))
                .setCommand("sentry");
        if (unitTesting) message.getData().setReason("UNIT TESTING");
        HashedMessage hm=new HashedMessage(message);
        
        MainApp.chat.clear();
        MainApp.chat.sendMessage("I'm online!");
        MainApp.chat.sendMessage("Please be quiet.  Use the 3-dots at the top right to enable notifications.  Leave this window open and I will notify when a building is taken, as long as I'm online.");
        System.out.println(sendToPapa(hm));
        
    }

    
    public static void reasonWarning(VirtualBoxMachine vm,String reason) {
        HashedMessage hm =new HashedMessage(getM(vm).setCommand("sentry")
                .setData(getData(vm).setReason(reason)));
        MainApp.chat.sendMessage("Announcement:"+reason);
        Object s = new RestTemplate().postForObject("https://catsbot-201602.appspot.com/adam_api/secret_shark_path_shhhh/async", hm.toJson(), String.class);
        System.out.println(s);
        
    }
    
    private static Message getM(VirtualBoxMachine vm) {
        Message m = new Message();
        return m;
    }
    
    private static MessageData getData(VirtualBoxMachine vm){
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        return new MessageData().setAvailable(vms).setGang(vm.getGang()).setUsername(vm.getUsername());
    }
    
    public static String sendToPapa(Message message){
        HashedMessage hm=new HashedMessage(message);
        return sendToPapa(hm);
    }
    
    public static String sendToPapa(HashedMessage hm) throws RestClientException, HttpServerErrorException {
        String json = hm.toJson();
        System.err.println(json);
        String s = new RestTemplate().postForObject(PAPAURL, json, String.class);
        return s;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
    
    public static void onlineWarning(VirtualBoxMachine vm) {
        vm.updateStatuses();
        ArrayList<VirtualBoxMachine> vms = new ArrayList<>();
        vms.add(vm);
        HashedMessage hm=new HashedMessage(getM(vm).setData(
                getData(vm).setReason("online"))
                .setCommand("sentry"));
        System.out.println(sendToPapa(hm));
        
    }

    
    public static void reasonWarning(VirtualBoxMachine vm,String reason) {
        HashedMessage hm =new HashedMessage(getM(vm).setCommand("sentry")
                .setData(getData(vm).setReason(reason)));
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
        return new MessageData().setActive(vms).setGang(vm.getGang()).setUsername(vm.getUsername());
    }
    
    public static String sendToPapa(HashedMessage hm) throws RestClientException, HttpServerErrorException {
        String json = hm.toJson();
        System.err.println(json);
        String s = new RestTemplate().postForObject(PAPAURL, json, String.class);
        return s;
    }
}

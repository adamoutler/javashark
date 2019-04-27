/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.javashark.MainApp;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class SendPapaAsynch {

    final private static String UNCLEURL = "https://catsbot-201602.appspot.com/adam_api/secret_shark_path_shhhh/async";
    final private static String PAPAURL = "https://catsbot-201602.appspot.com/adam_api/secret_shark_path_shhhhh/async";

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

        MainApp.chat.clear();
        MainApp.chat.sendMessage("I'm online!");
        MainApp.chat.sendMessage("Please be quiet.  Use the 3-dots at the top right to enable notifications.  Leave this window open and I will notify when a building is taken, as long as I'm online.");
        System.out.println(sendToPapa(hm));

    }

    public static void sentryReasonWarning(VirtualBoxMachine vm, String reason) {
        HashedMessage hm = new HashedMessage(getM(vm).setCommand("sentry")
                .setData(getData(vm).setReason(reason)));
        MainApp.chat.sendMessage("Announcement:" + reason);
        Object s = getRestTemplate().postForObject("https://catsbot-201602.appspot.com/adam_api/secret_shark_path_shhhh/async", hm.toJson(), String.class);
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

    public static String sendToPapa(Message message) {
        HashedMessage hm = new HashedMessage(message);
        return sendToPapa(hm);
    }

    public static String sendToPapa(HashedMessage hm) throws RestClientException, HttpServerErrorException {
        String json = hm.toJson();
        System.err.println(json);
        String gang = getGangFromMessage( hm);
        String s;
        if (gang == null || gang.equals("staging") || gang.equals("development")) {
            System.out.println("Sending message to Uncle Shark for gang:"+gang);
            s = getRestTemplate().postForObject(UNCLEURL, json, String.class);
        } else {
            System.out.println("Sending message to Papa Shark for gang:"+gang);
            s = getRestTemplate().postForObject(PAPAURL, json, String.class);
        }
        System.out.println("Received: "+s);
        return s;
    }

    private static String getGangFromMessage( HashedMessage hm) {
        String gang=null;
        try {
            gang = hm.getMessage().getData().getGang();
            if (gang==null){
                throw new Exception();
            }
        } catch (Exception ex) {
            try {
                gang = hm.getMessage().getData().getAvailable().get(0).getGang();
            } catch (Exception exx) {

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

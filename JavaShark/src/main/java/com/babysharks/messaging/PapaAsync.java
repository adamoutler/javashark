/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.Prop;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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
        reason = reason.replaceAll(" ", "_");
        HashedMessage hm = null;
        if (System.currentTimeMillis() > time + 10000) {
            hm = new HashedMessage(getM(vm).setCommand("sentry")
                    .setData(getData(vm).setReason(reason).setUrl(vm.writeScreenToWebServer(reason))));
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

    public static void sendURLToAdam(String url) {
        Message m = new Message();
        m.setCommand("adam").setData(m.getData());
        m.getData().setUrl(url);
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

    public static String send(String comand, VirtualBoxMachine vm, Object resultObject) {
        Message message = new Message().setCommand(comand).setData(getData(vm).setResultObject(resultObject));
        return send(message);
    }

    public static String send(HashedMessage hm) throws RestClientException, HttpServerErrorException {
        String json = hm.toJson();
        System.err.println(json);
        String gang = getGangFromMessage(hm);
        String s;
        s = sendRequest(gang, json);
        System.out.println("Received: " + s);
        return s;
    }

    private static String sendRequest(String gang, String json) throws RestClientException {
        String s;

        try {
            if (gang == "none") {
                gang = "development";
            }
            if (gang == null || gang.equals("none") || gang.equals("staging") || gang.equals("development")) {
                System.out.println("Sending message to Uncle Shark for gang:" + gang);
                s = getRestTemplate().postForObject(UNCLEURL, json, String.class);
            } else {
                System.out.println("Sending message to Papa Shark for gang:" + gang);
                s = getRestTemplate().postForObject(PPAPAURL, json, String.class);
            }
            processCache();
        } catch (Exception ex) {

            addMessageToCache(gang, json);
            throw new RestClientException("Could not send message, trying again later");
        }
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

    static File cacheFile = new File(System.getProperty("user.home") + "/papaCache.json");

    static String[] recognizedAddableStrings = new String[]{
        "\"reason\": \"blind\",",};

    private static void addMessageToCache(String gang, String json) {
        boolean addable = false;
        for (String string : recognizedAddableStrings) {
            if (json.contains("string")) {
                addable = true;
            }
        }
        if (!addable) {
            return;
        }
        try {
            ArrayList al = null;
            Gson gson = new GsonBuilder().create();
            try (FileReader reader = new FileReader(cacheFile)) {
                al = gson.fromJson(reader, ArrayList.class);
            } catch (IOException ex) {
                Logger.getLogger(PapaAsync.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (al == null) {
                al = new ArrayList();
            }
            al.add(new MessageObject(gang, json));
            String outputjson = gson.toJson(al);
            cacheFile.delete();
            cacheFile.createNewFile();
            try (FileWriter writer = new FileWriter(cacheFile)) {
                writer.write(outputjson);
            } catch (IOException ex) {
                Logger.getLogger(PapaAsync.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(PapaAsync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processCache() {
        try {
            if (cacheFile.length() < 5) {
                return;
            }
            ArrayList<MessageObject> al = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            try (FileReader reader = new FileReader(cacheFile)) {
                Type collectionType = new TypeToken<ArrayList<MessageObject>>() {
                }.getType();
                al = gson.fromJson(reader, collectionType);
            } catch (Throwable ex) {
                Logger.getLogger(PapaAsync.class.getName()).log(Level.SEVERE, null, ex);
            }
            String outputjson = gson.toJson(al);
            cacheFile.delete();
            cacheFile.createNewFile();
            for (MessageObject o : al) {

                try {
                    sendRequest(o.gang, o.json);
                } catch (Throwable ex) {
                    System.out.println("error while processing;");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(PapaAsync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class MessageObject {

    String gang;
    String json;

    MessageObject(String gang, String json) {
        this.gang = gang;
        this.json = json;
    }
}

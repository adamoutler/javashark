package com.babysharks.messaging;

import com.adamoutler.Prop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author adamo
 */
public class HashedMessage {

    final public String service = "JavaShark";
    private Message message;
    public static String instance;
    private String machine = instance;
    private String hash;

    public HashedMessage(Message message) {
        this.message = message;
        this.hash = getSha512(message.toJson());

    }

    public static HashedMessage hash(Message m) {
        return new HashedMessage(m);
    }

    private static String getSha512(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException ex) {
            return "null could not find algorithm requested";
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setLenient().setPrettyPrinting().create();
        String retval = gson.toJson(this);
        return retval;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    public static HashedMessage decode(String json) throws Exception {
        Gson gson = new GsonBuilder().create();
        HashedMessage hm = gson.fromJson(json, HashedMessage.class);
        verifyHash(json, hm);
        return hm;
    }

    /**
     * @return the instance
     */
    public static String getInstance() {
        return instance;
    }

    /**
     * @param aInstance the instance to set
     */
    public static void setInstance(String aInstance) {
        instance = aInstance;
    }

    public String toString() {
        return this.getHash();
    }

    private static void verifyHash(String json, HashedMessage hm) throws HashedMessageException {
        //{"service":"papa","message":{"command":"time","data":{"location":"berlin","username":"fezzik_king"}},"hash":"06ea821b9d5f662da8ee9c66ca3d5cbcf70a0db52cac2994cd30505d2dec4d6ff5670bb7b412db2bafba47a21a8f3c9eb78519db30b6e446212f65b5972c955e"}
        int endOfMessage = json.indexOf('}' + ",\"hash\":\"") + 1;
        String messagePortion = json.substring(0, endOfMessage).replaceFirst("\\{\"service\":\"papa\",\"message\":", "");
        verifyHashOrDie(messagePortion, hm);
        verifyTimeOrDie(hm);
    }

    private static void verifyTimeOrDie(HashedMessage hm) throws NumberFormatException, HashedMessageException {
        long time = System.currentTimeMillis();
        long messageTime = Long.parseLong(hm.getMessage().getTimestamp()) * 1000;
        System.out.println("me: " + time + "\n" + "fez:" + messageTime);
        if (time + 30000 < messageTime || time - 30000 > messageTime) {
            throw new HashedMessageException("Message time is wrong.  We are at " + time + " and message is " + messageTime);
        }
    }

    private static void verifyHashOrDie(String messagePortion, HashedMessage hm) throws HashedMessageException {
        String hash = getSha512(messagePortion + Prop.getProp("my_secret_password"));
        if (!(hash).equals(hm.getHash())) {
            System.out.println ("message: "+messagePortion);
            System.out.println ("Got hash: "+hash);
            System.out.println ("Expected: "+hm.getHash());
            throw new HashedMessageException("Hash did not match");
        }
    }
}

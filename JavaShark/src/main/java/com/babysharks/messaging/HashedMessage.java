package com.babysharks.messaging;

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
    public String service="JavaShark";
    private Message message;
    private String hash;

    public HashedMessage(Message message) {
        this.message = message;
        this.hash = getSha512(message.toJson());

    }
    
    public static HashedMessage hash(Message m){
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
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        return gson.toJson(this);
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

    public static HashedMessage decode(String json){
        Gson gson=new GsonBuilder().create();
        return gson.fromJson(json, HashedMessage.class);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author adamo
 */
public class Message {

    private String command;
    private String building;
    private String response;
    private String yearday;

    
    /**
     * @return the message
     */
    public String getMessage() {
        return response;
    }

    /**
     * @param message the message to set
     * @return reference to this object
     */
    public Message setMessage(String message) {
        this.response = message;
        return this;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set1
      * @return reference to this object
     */
    public Message setCommand(String command) {
        this.command = command;
        return this;
    }
    
    public String getYearDate(){
        Date date=new Date();
        SimpleDateFormat d = new SimpleDateFormat("YYYY D");
        return d.format(date);
    }
    
    

    /**
     * @return the building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * @param building the building to set
     * @return reference to this object
     */
    public Message setBuilding(String building) {
        this.building = building;
        return this;
    }
    
    
    public String toJson(){
        Gson gson=new GsonBuilder().setLenient().setPrettyPrinting().create();
        this.yearday=getYearDate();
        return gson.toJson(this);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author adamo
 */
public class Message {

    private String command;
    private MessageData data = new MessageData();
    private String timestamp;

    public Message() {
        generateTimeStamp();

    }

    /**
     * @return the message
     */
    public MessageData getData() {
        return data;
    }

    /**
     * @param data the data to be set
     * @return reference to this object
     */
    public Message setData(MessageData data) {
        this.data = data;
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

    final void generateTimeStamp() {
        this.timestamp = Long.toString(System.currentTimeMillis() / 1000L);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setLenient().setPrettyPrinting().create();
        this.timestamp = getTimestamp();
        return gson.toJson(this);
    }

}

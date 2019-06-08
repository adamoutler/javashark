/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.datastructures.Gang;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author adamo
 */
public class MessageData {

    private int[] weOwn;
    private int[] theyOwn;
    private int[] nobodyOwns;
    private int[] alertBuildings;
    private String username;
    private String url;
    private String gang;
    private String text;
    private String reason;
    private String ScreenName;
    private String[] buildings;
    private int[] scanBuildings;
    private Boolean friendlyScan;
    private Object resultObject;
    private ArrayList<VirtualBoxMachine> available;
    private String location;
    private TaskType[] tasks;
    private String comment;
    private Gang gangObject;
    private String reportName;
    private Map<String, String> hashes;
    private boolean debug = false;

    /**
     * @return the weOwn
     */
    public int[] getWeOwn() {
        return weOwn;
    }

    /**
     * @param weOwn the weOwn to set
     * @return representation of this MessageData object
     */
    public MessageData setWeOwn(int[] weOwn) {
        this.weOwn = weOwn;
        return this;
    }

    /**
     * @return the theyOwn
     */
    public int[] getTheyOwn() {
        return theyOwn;
    }

    /**
     * @param theyOwn the theyOwn to set
     * @return representation of this MessageData object
     */
    public MessageData setTheyOwn(int[] theyOwn) {
        this.theyOwn = theyOwn;
        return this;
    }

    /**
     * @return the nobodyOwns
     */
    public int[] getNobodyOwns() {
        return nobodyOwns;
    }

    /**
     * @param nobodyOwns the nobodyOwns to set
     * @return representation of this MessageData object
     */
    public MessageData setNobodyOwns(int[] nobodyOwns) {
        this.nobodyOwns = nobodyOwns;
        return this;
    }

    /**
     * @return the alertBuilding
     */
    public int[] getAlertBuilding() {
        return alertBuildings;
    }

    /**
     * @param alertBuilding the alertBuilding to set
     * @return representation of this MessageData object
     */
    public MessageData setAlertBuilding(int[] alertBuilding) {
        this.alertBuildings = alertBuilding;
        return this;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     * @return representation of this MessageData object
     */
    public MessageData setText(String text) {
        this.text = text;
        return this;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setLenient().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     * @return representation of this MessageData object
     */
    public MessageData setReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * @return the buildings
     */
    public String[] getBuildings() {
        return buildings;
    }

    /**
     * @param buildings the buildings to set
     * @return representation of this MessageData object
     */
    public MessageData setBuildings(String[] buildings) {
        this.buildings = buildings;
        return this;
    }

    /**
     * @return the resultObject
     */
    public Object getResultObject() {
        return resultObject;
    }

    /**
     * @param resultObject the resultObject to set
     * @return representation of this MessageData object
     */
    public MessageData setResultObject(Object resultObject) {
        this.resultObject = resultObject;
        return this;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username.replaceAll("_", "");
    }

    /**
     * @param username the username to set
     * @return reference to this object
     */
    public MessageData setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     * @return reference to this object
     */
    public MessageData setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return the gang
     */
    public String getGang() {
        return gang;
    }

    /**
     * @param gang the gang to set
     * @return reference to this object
     */
    public MessageData setGang(String gang) {
        this.gang = gang;
        return this;
    }

    /**
     * @return the available
     */
    public ArrayList<VirtualBoxMachine> getAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     * @return reference to this object
     */
    public MessageData setAvailable(ArrayList<VirtualBoxMachine> available) {
        this.available = available;
        return this;
    }

    /**
     * @param vm the available machine to set
     * @return reference to this object
     */
    public MessageData addAvailable(VirtualBoxMachine vm) {
        if (this.available == null) {
            this.available = new ArrayList<>();
        }
        this.available.add(vm);
        return this;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     * @return reference to this object
     */
    public MessageData setLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * @return the ScreenName
     */
    public String getScreenName() {
        return ScreenName;
    }

    /**
     * @param ScreenName the ScreenName to set
     * @return reference to this object.
     */
    public MessageData setScreenName(String ScreenName) {
        this.ScreenName = ScreenName;
        return this;
    }

    public TaskType[] getTasks() {
        return tasks;
    }

    /**
     * @param tasks the tasks to set
     * @return reference to this object
     */
    public MessageData setTasks(TaskType[] tasks) {
        this.tasks = tasks;
        return this;
    }

    /**
     * sets the comment
     *
     * @param comment
     * @return reference to this object
     */
    public MessageData setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * @return the userHashes
     */
    public Gang getGangsters() {
        return gangObject;
    }

    /**
     * @param gangObject the userHashes to set
     * @return reference to this object
     */
    public MessageData setGangObject(Gang gangObject) {
        this.gangObject = gangObject;
        return this;

    }

    /**
     * @return the reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportName the reportName to set
     * @return reference to this object
     */
    public MessageData setReportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    /**
     * @return the hashes
     */
    public Map<String, String> getHashes() {
        return hashes;
    }

    /**
     * @param hashes the hashes to set
     * @return reference to this object
     */
    public MessageData setHashes(Map<String, String> hashes) {
        this.hashes = hashes;
        return this;
    }

    /**
     * @return the scanBuildings
     */
    public int[] getScanBuildings() {
        return scanBuildings;
    }

    /**
     * @param scanBuildings the scanBuildings to set
     * @return reference to this object
     */
    public MessageData setScanBuildings(int[] scanBuildings) {
        this.scanBuildings = scanBuildings;
        return this;
    }

    /**
     * @return the friendlyScan
     */
    public Boolean isFriendlyScan() {
        return friendlyScan;
    }

    /**
     * @param friendlyScan the friendlyScan to set
     * @return reference to this object
     */
    public MessageData setFriendlyScan(boolean friendlyScan) {
        this.friendlyScan = friendlyScan;
        return this;
    }
}

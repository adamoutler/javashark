/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.datastructures;

import com.babysharks.datastructures.google.AddressComponent;
import com.babysharks.datastructures.google.GooglePlaceDetail;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author adamo
 */
public class DateTimeResult {
    
    private GooglePlaceDetail details;
    private int utcOffset;
    private String formattedName;
    private long milisSinceEpoch;
    private long javaSystemTime=System.currentTimeMillis();
    private String friendlyTime;
    private String javaDate;
    private String iso8601Time;
    private String utcHrOffset;
    public DateTimeResult (GooglePlaceDetail result){
        details=result;
        utcOffset=result.getUtcOffset();
        List<AddressComponent> ac=result.getAddressComponents();
        StringBuilder sb=new StringBuilder().append(ac.get(0).getLongName());
        if (ac.size()>1){
            sb.append(" (");
            for (int i=1; i<ac.size(); i++){
               sb.append(", ").append(ac.get(i).getShortName()); 
            }
        }
        formattedName=sb.append(")").toString().replaceFirst(", ", "");
        milisSinceEpoch=(javaSystemTime/1000);
        Date date=new Date(javaSystemTime+(utcOffset*60));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        iso8601Time=formatter.format(date);
        float utctime=utcOffset/60;
        utcHrOffset= "UTC"+(utctime<0 ? "":"+")+Float.toString(utctime);
        
        javaDate=date.toString().replace("EST", utcHrOffset);
        
        
    }

    /**
     * @return the details
     */
    public GooglePlaceDetail getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(GooglePlaceDetail details) {
        this.details = details;
    }

    /**
     * @return the utcOffset
     */
    public int getUtcOffset() {
        return utcOffset;
    }

    /**
     * @param utcOffset the utcOffset to set
     */
    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    /**
     * @return the formattedName
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * @param formattedName the formattedName to set
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    /**
     * @return the epochTime
     */
    public long getEpochTime() {
        return getMilisSinceEpoch();
    }

    /**
     * @param epochTime the epochTime to set
     */
    public void setEpochTime(long epochTime) {
        this.setMilisSinceEpoch(epochTime);
    }

    /**
     * @return the friendlyTime
     */
    public String getFriendlyTime() {
        return friendlyTime;
    }

    /**
     * @param friendlyTime the friendlyTime to set
     */
    public void setFriendlyTime(String friendlyTime) {
        this.friendlyTime = friendlyTime;
    }

    /**
     * @return the formattedTime
     */
    public String getFormattedTime() {
        return getIso8601Time();
    }

    /**
     * @param formattedTime the formattedTime to set
     */
    public void setFormattedTime(String formattedTime) {
        this.setIso8601Time(formattedTime);
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return getJavaSystemTime();
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.setJavaSystemTime(timestamp);
    }

    /**
     * @return the javaDate
     */
    public String getJavaDate() {
        return javaDate;
    }

    /**
     * @param javaDate the javaDate to set
     */
    public void setJavaDate(String javaDate) {
        this.javaDate = javaDate;
    }

    /**
     * @return the milisSinceEpoch
     */
    public long getMilisSinceEpoch() {
        return milisSinceEpoch;
    }

    /**
     * @param milisSinceEpoch the milisSinceEpoch to set
     */
    public void setMilisSinceEpoch(long milisSinceEpoch) {
        this.milisSinceEpoch = milisSinceEpoch;
    }

    /**
     * @return the javaSystemTime
     */
    public long getJavaSystemTime() {
        return javaSystemTime;
    }

    /**
     * @param javaSystemTime the javaSystemTime to set
     */
    public void setJavaSystemTime(long javaSystemTime) {
        this.javaSystemTime = javaSystemTime;
    }

    /**
     * @return the iso8601Time
     */
    public String getIso8601Time() {
        return iso8601Time;
    }

    /**
     * @param iso8601Time the iso8601Time to set
     */
    public void setIso8601Time(String iso8601Time) {
        this.iso8601Time = iso8601Time;
    }

    /**
     * @return the utcHrOffset
     */
    public String getUtcHrOffset() {
        return utcHrOffset;
    }

    /**
     * @param utcHrOffset the utcHrOffset to set
     */
    public void setUtcHrOffset(String utcHrOffset) {
        this.utcHrOffset = utcHrOffset;
    }
}

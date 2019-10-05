/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class Translate {

    public void main(String[] args) {
        try {
            GoogleAuth.authExplicit("C:\\Users\\adamo\\OneDrive\\Desktop\\svn\\Baby Sharks Projects\\JavaShark\\src\\main\\resources\\JavaShark-a966035cedbc.json");
        } catch (IOException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Translate().translate("hola");
    }

    String translate(String text) {

        HashMap<String, String> map = new HashMap();
        String s = new RestTemplate().postForObject("https://translation.googleapis.com/language/translate/v2/", text, String.class, map);
        System.out.println(s);
        Data data = new RestTemplate().postForObject("https://translation.googleapis.com/language/translate/v2", text, Data.class);

        return "";

    }

    static String translate(String text, String fromLanguage) {
        return "";
    }

    class Request {

        String target = "en";
        String[] q = new String[1];
        String model = "nmt";
        String key = "REPLACE_ME";

        Request(String text) {
            q[0] = text;
        }
    }

    String getRequest(String text) {
        Request request = new Request(text);
        String requestJson = new GsonBuilder().create().toJson(request);
        return requestJson;
    }

}

class Data {

    @SerializedName("detections")
    @Expose
    private List<List<Detection>> detections = null;

    public List<List<Detection>> getDetections() {
        return detections;
    }

    public void setDetections(List<List<Detection>> detections) {
        this.detections = detections;
    }

}

class Detection {

    @SerializedName("confidence")
    @Expose
    private Integer confidence;
    @SerializedName("isReliable")
    @Expose
    private Boolean isReliable;
    @SerializedName("language")
    @Expose
    private String language;

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public Boolean getIsReliable() {
        return isReliable;
    }

    public void setIsReliable(Boolean isReliable) {
        this.isReliable = isReliable;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}

class GooglePlaceDetailObject {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}

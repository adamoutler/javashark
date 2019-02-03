/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class LocationTime {

    public static DetailsResult getLocation(String location, String apiKey) {
        String locationRequest = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
                + "?key=" + apiKey
                + "&inputtype=textquery&input=" + location;
        JsonElement jes = new JsonParser().parse(new RestTemplate()
                .getForEntity(locationRequest, String.class).getBody());
        System.out.println(jes.toString());
        String placeID = jes.getAsJsonObject().get("candidates")
                .getAsJsonArray().get(0).getAsJsonObject().get("place_id")
                .toString();
        String detailsRequest = "https://maps.googleapis.com/maps/api/place/details/json"
                + "?placeid=" + placeID.replace("\"", "").replace("\\\"", "")
                + "&fields=name,address_components,utc_offset"
                + "&inputtype=textquery"
                + "&key=" + apiKey;
        String dsr = new RestTemplate().getForEntity(detailsRequest, String.class)
                .getBody();

        DetailsResult dr = new RestTemplate().getForEntity(detailsRequest, DetailsResult.class)
                .getBody();

        return dr;

    }

    static String check = "/time ";

    public String getDetails(String apiKey, String place) {

        try {
            DetailsResult dr = getLocation(place, apiKey);
            return "It is " + dr.getCurrentTime() + " in " + dr.getLongName() + "(" + dr.getShortName() + ").";

        } catch (Exception ex) {
            return "I don't know that place.";

        }

    }

    public static boolean isTimeExtractionCommand(String value) {
        return value.toLowerCase().contains(check);
    }

}

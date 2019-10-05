/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import com.adamoutler.time.UserTimeCalculator;
import com.babysharks.datastructures.DateTimeResult;
import com.babysharks.datastructures.google.GooglePlaceDetail;
import com.babysharks.datastructures.google.GooglePlaceDetailObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class LocationTime {

    public static GooglePlaceDetail getLocation(String location, String apiKey) {
        String locationRequest = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
                + "?key=" + apiKey
                + "&inputtype=textquery&input=" + location;
        JsonElement jes = getLocation(locationRequest);
        System.out.println(jes.toString());
        if (jes.toString().contains("ZERO_RESULTS")) {
            GooglePlaceDetail gpd = new GooglePlaceDetail();
            gpd.setName("I don't know that place");
            return gpd;
        }
        String placeID = jes.getAsJsonObject().get("candidates")
                .getAsJsonArray().get(0).getAsJsonObject().get("place_id")
                .toString();
        String detailsRequest = "https://maps.googleapis.com/maps/api/place/details/json"
                + "?placeid=" + placeID.replace("\"", "").replace("\\\"", "")
                + "&fields=name,address_components,utc_offset"
                + "&inputtype=textquery"
                + "&key=" + apiKey;
        getDetails(detailsRequest);

        GooglePlaceDetail dr = new RestTemplate().getForEntity(detailsRequest, GooglePlaceDetailObject.class).getBody().getResult();

        return dr;

    }

    private static void getDetails(String detailsRequest) throws RestClientException {
        String dsr = new RestTemplate().getForEntity(detailsRequest, String.class)
                .getBody();
    }

    private static JsonElement getLocation(String locationRequest) throws JsonSyntaxException, RestClientException {
        JsonElement jes = new JsonParser().parse(new RestTemplate()
                .getForEntity(locationRequest, String.class).getBody());
        return jes;
    }

    static String check = "/time ";

    public String getDetails(String apiKey, String place) {

        try {
            GooglePlaceDetail dr = getLocation(place, apiKey);
            return getFriendlyTime(dr);

        } catch (Exception ex) {
            return "I don't know that place.";

        }

    }

    private static String getFriendlyTime(GooglePlaceDetail dr) {
        return "It is " + getCurrentTime(dr) + " in " + dr.getAddressComponents().get(0).getLongName() + "(" + dr.getAddressComponents().get(0).getShortName() + ").";
    }

    public static DateTimeResult getDateTimeObject(String place, String apiKey) {
        DateTimeResult dtr = new DateTimeResult(getLocation(place, apiKey));
        return dtr;
    }

    public static boolean isTimeExtractionCommand(String value) {
        return value.toLowerCase().contains(check);
    }

    public static String getCurrentTime(GooglePlaceDetail result) {

        long offset = getUtcOffset(result);
        String curDate = UserTimeCalculator.calculateUTCTimeWithOffset(offset);
        return curDate;

    }

    public static int getUtcOffset(GooglePlaceDetail result) {

        return result.getUtcOffset();
    }
}

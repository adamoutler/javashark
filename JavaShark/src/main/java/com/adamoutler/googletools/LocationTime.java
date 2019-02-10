/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import com.adamoutler.time.UserTimeCalculator;
import com.babysharks.datastructures.DateTimeResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.web.client.RestTemplate;
import com.babysharks.datastructures.google.GooglePlaceDetail;
import com.babysharks.datastructures.google.GooglePlaceDetailObject;

/**
 *
 * @author adamo
 */
public class LocationTime {

    public static GooglePlaceDetail getLocation(String location, String apiKey) {
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
                + "&fields=name,address_components,utc_offset,url"
                + "&inputtype=textquery"
                + "&key=" + apiKey;
        String dsr = new RestTemplate().getForEntity(detailsRequest, String.class)
                .getBody();

        GooglePlaceDetail dr = new RestTemplate().getForEntity(detailsRequest, GooglePlaceDetailObject.class).getBody().getResult();

        return dr;

    }

    static String check = "/time ";

    public String  getDetails(String apiKey, String place) {
 
     

        try {
            GooglePlaceDetail dr = getLocation(place, apiKey);
            return getFriendlyTime(dr);

        } catch (Exception ex) {
          return  "I don't know that place.";

        }

        

    }

    private static String getFriendlyTime(GooglePlaceDetail dr) {
        return "It is "+getCurrentTime(dr) + " in " + dr.getAddressComponents().get(0).getLongName()+ "(" +dr.getAddressComponents().get(0).getShortName() + ").";
    }
    
    public static DateTimeResult getDateTimeObject(String place,String apiKey){
        DateTimeResult dtr =new DateTimeResult(getLocation(place, apiKey));
        dtr.setFriendlyTime(getFriendlyTime(dtr.getDetails()));
        return dtr;
    }

    public static boolean isTimeExtractionCommand(String value) {
        return value.toLowerCase().contains(check);
    }
public static String getCurrentTime(GooglePlaceDetail result){
        
        long offset=getUtcOffset( result);
        String curDate=UserTimeCalculator.calculateUTCTimeWithOffset(offset);
        return curDate;
        
    }
 public static int getUtcOffset(GooglePlaceDetail result) {
        
       return result.getUtcOffset();
    }
}

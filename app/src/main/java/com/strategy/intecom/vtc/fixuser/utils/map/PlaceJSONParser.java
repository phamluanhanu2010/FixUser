package com.strategy.intecom.vtc.fixuser.utils.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Mr. Ha on 6/3/16.
 */

public class PlaceJSONParser {

    /** Receives a JSONObject and returns a list */
    public List<HashMap<String,String>> parse(JSONObject jObject){

            /** Retrieves all the elements in the 'places' array */
        JSONArray jPlaces = jObject.optJSONArray("results");
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        if(jPlaces != null) {
            int placesCount = jPlaces.length();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return placesList;
    }

    /** Parsing the Place JSON object */
    private HashMap<String, String> getPlace(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();

        String description = jPlace.optString("description");
        String id = jPlace.optString("id");
        String reference = jPlace.optString("reference");

        place.put("description", description);
        place.put("_id", id);
        place.put("reference", reference);

        return place;
    }
}
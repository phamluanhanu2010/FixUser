package com.strategy.intecom.vtc.fixuser.utils.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.strategy.intecom.vtc.fixuser.view.base.AppCore;


/**
 * Created by Mr. Ha on 6/3/16.
 */

public class PlaceProvider extends ContentProvider {

    public static final String AUTHORITY = "com.strategy.intecom.vtc.fixuser.utils.map.PlaceProvider";

    public static final Uri SEARCH_URI = Uri.parse("content://"+AUTHORITY+"/search");

    public static final Uri DETAILS_URI = Uri.parse("content://"+AUTHORITY+"/details");

    private static final int SEARCH = 1;
    private static final int SUGGESTIONS = 2;
    private static final int DETAILS = 3;

    // Obtain browser key from https://code.google.com/apis/console
    String mKey = "key=AIzaSyB_EUVaNsPRefPpGNQ7MYOxXFmTG4bv2Zc";

    // Defines a set of uris allowed with this content provider
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // URI for "Go" button
        uriMatcher.addURI(AUTHORITY, "search", SEARCH );

        // URI for suggestions in Search Dialog
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SUGGESTIONS);

        // URI for Details
        uriMatcher.addURI(AUTHORITY, "details", DETAILS);

        return uriMatcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;

        PlaceJSONParser parser = new PlaceJSONParser();
        PlaceDetailsJSONParser detailsParser = new PlaceDetailsJSONParser();

        String jsonString = "";
        String jsonPlaceDetails = "";

        List<HashMap<String, String>> list = null;
        List<HashMap<String, String>> detailsList = null;

        MatrixCursor mCursor = null;

        switch(mUriMatcher.match(uri)){
            case SEARCH:
                // Defining a cursor object with columns description, lat and lng
                mCursor = new MatrixCursor(new String[] { "formatted_address","lat","lng", "icon" });

                // Get Places from Google Places API
                jsonString = getPlaces(selectionArgs);
                try {

                   JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray jsonArray = jsonObject.optJSONArray("results");
                        if(jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject map = jsonArray.optJSONObject(i);

                                if (map != null) {

                                    String formatted_address = map.optString("formatted_address");
                                    String icon = map.optString("icon");
                                    String lat = "0";
                                    String lng = "0";

                                    JSONObject geometry = map.optJSONObject("geometry");
                                    if (geometry != null) {
                                        JSONObject location = geometry.optJSONObject("location");

                                        if (location != null) {

                                            lat = location.optString("lat");
                                            lng = location.optString("lng");
                                        }

                                    }
                                    mCursor.addRow(new String[]{ formatted_address , lat,  lng, icon });
                                }
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                c = mCursor;
                break;

            case SUGGESTIONS :

                // Defining a cursor object with columns id, SUGGEST_COLUMN_TEXT_1, SUGGEST_COLUMN_INTENT_EXTRA_DATA
                mCursor = new MatrixCursor(new String[] { "_id", SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA } );

                // Creating a parser object to parse places in JSON format
                parser = new PlaceJSONParser();

                // Get Places from Google Places API
                jsonString = getPlaces(selectionArgs);

                try {
                    // Parse the places ( JSON => List )
                    list = parser.parse(new JSONObject(jsonString));

                    // Creating cursor object with places
                    for(int i=0;i<list.size();i++){
                        HashMap<String, String> hMap = (HashMap<String, String>) list.get(i);

                        // Adding place details to cursor
                        mCursor.addRow(new String[] { Integer.toString(i), hMap.get("description"), hMap.get("reference") });
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                c = mCursor;
                break;

            case DETAILS :
                // Defining a cursor object with columns description, lat and lng
                mCursor = new MatrixCursor(new String[] { "description","lat","lng" });

                detailsParser = new PlaceDetailsJSONParser();
                jsonPlaceDetails = getPlaceDetails(selectionArgs[0]);
                try {
                    detailsList = detailsParser.parse(new JSONObject(jsonPlaceDetails));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                for(int j=0;j<detailsList.size();j++){
                    HashMap<String, String> hMapDetails = detailsList.get(j);
                    mCursor.addRow(new String[]{ hMapDetails.get("formatted_address") , hMapDetails.get("lat") , hMapDetails.get("lng") });
                }
                c = mCursor;
                break;
        }
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            AppCore.showLog("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getPlaceDetailsUrl(String ref){

        // reference of place
        String reference = "reference="+ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference+"&"+sensor+"&"+mKey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;

        AppCore.showLog("getPlaceDetailsUrl ----- : " + url);
        return url;
    }

    private String getPlacesUrl(String qry){

        try {
            qry = "query=" + URLEncoder.encode(qry, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

//        // Sensor enabled
//        String sensor = "sensor=false";
//
//        // place type to be searched
//        String types = "types=geocode";

        // Building the parameters to the web service
//        String parameters = qry+"&"+types+"&"+sensor+"&"+mKey;
        String parameters = qry+"&"+mKey;

        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/"+output+"?"+parameters;
        AppCore.showLog("getPlacesUrl --- : " + url);
//        https://maps.googleapis.com/maps/api/place/textsearch/json?key=AIzaSyAeCwlbxzxhonGvVBnydYJsVNg5jDmHxZU&query=L%E1%BA%A1c+trung%2C+Hai+b%C3%A0+tr%C6%B0ng%2C+H%C3%A0+n%E1%BB%99i
        return url;
    }

    private String getPlaces(String[] params){
        // For storing data from web service
        String data = "";
        String url = getPlacesUrl(params[0]);
        try{
            // Fetching the data from web service in background
            data = downloadUrl(url);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    private String getPlaceDetails(String reference){
        String data = "";
        String url = getPlaceDetailsUrl(reference);
        try {
            data = downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

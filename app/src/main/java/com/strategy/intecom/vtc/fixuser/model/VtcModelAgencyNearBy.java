package com.strategy.intecom.vtc.fixuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixuser.utils.ParserJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 7/11/16.
 */
public class VtcModelAgencyNearBy implements Parcelable{

    private String id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String skills;
    private String image;

    public VtcModelAgencyNearBy(){

    }

    public static List<VtcModelAgencyNearBy> getDataJson(String response){

        List<VtcModelAgencyNearBy> lst = new ArrayList<>();

        if (!response.equals("")) {

            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int a = 0; a < jsonArray.length(); a++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                    if (jsonObject != null) {

                        VtcModelAgencyNearBy model = new VtcModelAgencyNearBy();

                        model.setId(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                        model.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                        model.setImage(jsonObject.optString(ParserJson.API_PARAMETER_AVATAR));

                        JSONObject addresss = jsonObject.optJSONObject(ParserJson.API_PARAMETER_ADDRESS);
                        if (addresss != null) {

                            model.setAddress(addresss.optString(ParserJson.API_PARAMETER_NAME));
                            model.setLongitude(addresss.optString(ParserJson.API_PARAMETER_LONGITUDE));
                            model.setLatitude(addresss.optString(ParserJson.API_PARAMETER_LATITUDE));
                        }

                        JSONArray skills = jsonObject.optJSONArray(ParserJson.API_PARAMETER_SKILLS);

                        String skill = "";

                        if (skills != null && skills.length() > 0) {
                            for (int i = 0; i < skills.length(); i++) {
                                JSONObject object = skills.optJSONObject(i);
                                skill += object.optString(ParserJson.API_PARAMETER_NAME);
                                if (i < 1) {
                                    skill += ", ";
                                }
                            }
                        }
                        model.setSkills(skill);

                        lst.add(model);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lst;
    }


    protected VtcModelAgencyNearBy(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        skills = in.readString();
        image = in.readString();
    }

    public static final Creator<VtcModelAgencyNearBy> CREATOR = new Creator<VtcModelAgencyNearBy>() {
        @Override
        public VtcModelAgencyNearBy createFromParcel(Parcel in) {
            return new VtcModelAgencyNearBy(in);
        }

        @Override
        public VtcModelAgencyNearBy[] newArray(int size) {
            return new VtcModelAgencyNearBy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(skills);
        dest.writeString(image);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.strategy.intecom.vtc.fixuser.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixuser.config.VtcDBBase;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelAddress implements Parcelable{

    private static final String TAG = VtcModelAddress.class.getName();

    private int id = 0;
    private String distance = "";
    private String address = "";
    private String description = "";
    private double longitude = 0.0f;
    private double latitude = 0.0f;
    private boolean isSave = Boolean.FALSE;

    protected VtcModelAddress(Parcel in) {
        id = in.readInt();
        distance = in.readString();
        address = in.readString();
        description = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        isSave = in.readByte() != 0;
    }

    public VtcModelAddress(){

    }

    public static final Creator<VtcModelAddress> CREATOR = new Creator<VtcModelAddress>() {
        @Override
        public VtcModelAddress createFromParcel(Parcel in) {
            return new VtcModelAddress(in);
        }

        @Override
        public VtcModelAddress[] newArray(int size) {
            return new VtcModelAddress[size];
        }
    };

    public static List<VtcModelAddress> setDataListAddress(Cursor cursor) {
        List<VtcModelAddress> lst_address = new ArrayList<>();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    VtcModelAddress modelAddress = new VtcModelAddress();
                    modelAddress.setId(cursor.getInt(cursor.getColumnIndex(VtcDBBase.id)));
                    modelAddress.setDistance(cursor.getString(cursor.getColumnIndex(VtcDBBase.Search_distance)));
                    modelAddress.setAddress(cursor.getString(cursor.getColumnIndex(VtcDBBase.Search_address)));
                    modelAddress.setDescription(cursor.getString(cursor.getColumnIndex(VtcDBBase.Search_address)));

                    double longitude = 0.0f;
                    double latitude = 0.0f;

                    try {

                        longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(VtcDBBase.Search_longitude)));
                        latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(VtcDBBase.Search_latitude)));
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                    modelAddress.setLongitude(longitude);
                    modelAddress.setLatitude(latitude);


                    int isSave = cursor.getInt(cursor.getColumnIndex(VtcDBBase.Search_isSave));
                    if(isSave == 0){
                        modelAddress.setSave(false);
                    }else {
                        modelAddress.setSave(true);
                    }

                    lst_address.add(modelAddress);
                }
            }
        } catch (Exception e) {
            AppCore.showLog(TAG + "  " + cursor.getPosition() + " setDataListAddress Exception : " + e.getMessage());
        }finally {
            cursor.close();
        }
        return lst_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(distance);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeByte((byte) (isSave ? 1 : 0));
    }
}

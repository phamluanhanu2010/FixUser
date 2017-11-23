package com.strategy.intecom.vtc.fixuser.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelNoti implements Parcelable {

    private static final String TAG = VtcModelNoti.class.getName();

    private int type = 0;
    private String message = "";
    private String title = "";
    private String id_order = "";
    private String responseData = "";
    private int typeGoto = 1;               // 1 Là trạng thái đến từ notifi in app, 0 là trạng thái đến từ notifi out app

    protected VtcModelNoti(Parcel in) {
        type = in.readInt();
        message = in.readString();
        title = in.readString();
        id_order = in.readString();
        responseData = in.readString();
        typeGoto = in.readInt();
    }

    public VtcModelNoti(){

    }

    public static final Creator<VtcModelNoti> CREATOR = new Creator<VtcModelNoti>() {
        @Override
        public VtcModelNoti createFromParcel(Parcel in) {
            return new VtcModelNoti(in);
        }

        @Override
        public VtcModelNoti[] newArray(int size) {
            return new VtcModelNoti[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public int getTypeGoto() {
        return typeGoto;
    }

    public void setTypeGoto(int typeGoto) {
        this.typeGoto = typeGoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(message);
        dest.writeString(title);
        dest.writeString(id_order);
        dest.writeString(responseData);
        dest.writeInt(typeGoto);
    }
}

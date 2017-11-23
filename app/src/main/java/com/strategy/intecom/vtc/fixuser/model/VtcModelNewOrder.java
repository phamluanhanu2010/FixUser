package com.strategy.intecom.vtc.fixuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixuser.utils.ParserJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelNewOrder implements Parcelable {

    private static final String TAG = VtcModelNewOrder.class.getName();

    public final static String TYPE_BOOKING_NORMAL = "normal";
    public final static String TYPE_BOOKING_FAST = "fast";
    public final static String TYPE_BOOKING_PAY_TIENMAT = "Tiền mặt";
    public final static String TYPE_BOOKING_PAY_VTC = "VTC Pay";

    private String type;

    private String paytype = VtcModelNewOrder.TYPE_BOOKING_PAY_TIENMAT;

    private String id = "";
    private String name = "";
    private String email = "";
    private String phone = "";

    private String addname = "";
    private String addlong = "";
    private String addlat = "";

    private String fieldId = "";
    private String description = "";
    private String order_time = "";
    private String orderTimeString = "";
    private String coupon_code = "";
    private String price = "";
    private String fieldChildName = "";

    private List<String> lstImageUri = new ArrayList<>();
    private List<String> lstImageBase64 = new ArrayList<>();

    protected VtcModelNewOrder(Parcel in) {
        type = in.readString();
        paytype = in.readString();
        id = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        addname = in.readString();
        addlong = in.readString();
        addlat = in.readString();
        fieldId = in.readString();
        description = in.readString();
        order_time = in.readString();
        orderTimeString = in.readString();
        coupon_code = in.readString();
        price = in.readString();
        fieldChildName = in.readString();
        lstImageUri = in.createStringArrayList();
        lstImageBase64 = in.createStringArrayList();
    }

    public static final Creator<VtcModelNewOrder> CREATOR = new Creator<VtcModelNewOrder>() {
        @Override
        public VtcModelNewOrder createFromParcel(Parcel in) {
            return new VtcModelNewOrder(in);
        }

        @Override
        public VtcModelNewOrder[] newArray(int size) {
            return new VtcModelNewOrder[size];
        }
    };

    public JSONObject initInputObjNewOrder(){
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(ParserJson.API_PARAMETER_TYPE, getType());

            JSONObject jUser = new JSONObject();
            jUser.put(ParserJson.API_PARAMETER_ID, getId());
            jUser.put(ParserJson.API_PARAMETER_NAME, getName());
            jUser.put(ParserJson.API_PARAMETER_EMAIL, getEmail());
            jUser.put(ParserJson.API_PARAMETER_PHONE, getPhone());

            jsonObject.put(ParserJson.API_PARAMETER_USER, jUser);

            JSONObject jAddress = new JSONObject();
            jAddress.put(ParserJson.API_PARAMETER_NAME, getName());
            jAddress.put(ParserJson.API_PARAMETER_LONGITUDE, getAddlong());
            jAddress.put(ParserJson.API_PARAMETER_LATITUDE, getAddlat());

            jsonObject.put(ParserJson.API_PARAMETER_ADDRESS, jAddress);

            jsonObject.put(ParserJson.API_PARAMETER_FIELD_ID, getFieldId());
            jsonObject.put(ParserJson.API_PARAMETER_DESCRIPTION, getDescription());
            jsonObject.put(ParserJson.API_PARAMETER_ORDER_TIME, getOrder_time());
            jsonObject.put(ParserJson.API_PARAMETER_COUPON_CODE, getCoupon_code());


        } catch (JSONException e) {
        }

        return jsonObject;
    }

    public JSONObject initInputObjNewOrder(VtcModelNewOrder modelNewOrder){
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(ParserJson.API_PARAMETER_TYPE, modelNewOrder.getType());

            JSONObject jUser = new JSONObject();
            jUser.put(ParserJson.API_PARAMETER_ID, modelNewOrder.getId());
            jUser.put(ParserJson.API_PARAMETER_NAME, modelNewOrder.getName());
            jUser.put(ParserJson.API_PARAMETER_EMAIL, modelNewOrder.getEmail());
            jUser.put(ParserJson.API_PARAMETER_PHONE, modelNewOrder.getPhone());

            jsonObject.put(ParserJson.API_PARAMETER_USER, jUser);

            JSONObject jAddress = new JSONObject();
            jAddress.put(ParserJson.API_PARAMETER_NAME, modelNewOrder.getName());
            jAddress.put(ParserJson.API_PARAMETER_LONGITUDE, modelNewOrder.getAddlong());
            jAddress.put(ParserJson.API_PARAMETER_LATITUDE, modelNewOrder.getAddlat());

            jsonObject.put(ParserJson.API_PARAMETER_ADDRESS, jAddress);

            jsonObject.put(ParserJson.API_PARAMETER_FIELD_ID, modelNewOrder.getFieldId());
            jsonObject.put(ParserJson.API_PARAMETER_DESCRIPTION, modelNewOrder.getDescription());
            jsonObject.put(ParserJson.API_PARAMETER_ORDER_TIME, modelNewOrder.getOrder_time());
            jsonObject.put(ParserJson.API_PARAMETER_COUPON_CODE, modelNewOrder.getCoupon_code());


        } catch (JSONException e) {
        }


        return jsonObject;
    }

    public VtcModelNewOrder(){

    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddname() {
        return addname;
    }

    public void setAddname(String addname) {
        this.addname = addname;
    }

    public String getAddlong() {
        return addlong;
    }

    public void setAddlong(String addlong) {
        this.addlong = addlong;
    }

    public String getAddlat() {
        return addlat;
    }

    public void setAddlat(String addlat) {
        this.addlat = addlat;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFieldChildName() {
        return fieldChildName;
    }

    public void setFieldChildName(String fieldChildName) {
        this.fieldChildName = fieldChildName;
    }

    public List<String> getLstImageUri() {
        return lstImageUri;
    }

    public void setLstImageUri(List<String> lstImageUri) {
        this.lstImageUri = lstImageUri;
    }
    public void setLstImageUri(String image) {
        this.lstImageUri.add(image);
    }


    public List<String> getLstImageBase64() {
        return lstImageBase64;
    }

    public void setLstImageBase64(List<String> lstImageBase64) {
        this.lstImageBase64 = lstImageBase64;
    }
    public void setLstImageBase64(String image) {
        this.lstImageBase64.add(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(paytype);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(addname);
        dest.writeString(addlong);
        dest.writeString(addlat);
        dest.writeString(fieldId);
        dest.writeString(description);
        dest.writeString(order_time);
        dest.writeString(orderTimeString);
        dest.writeString(coupon_code);
        dest.writeString(price);
        dest.writeString(fieldChildName);
        dest.writeStringList(lstImageUri);
        dest.writeStringList(lstImageBase64);
    }

    public String getOrderTimeString() {
        return orderTimeString;
    }

    public void setOrderTimeString(String orderTimeString) {
        this.orderTimeString = orderTimeString;
    }
}

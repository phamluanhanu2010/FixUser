package com.strategy.intecom.vtc.fixuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelUser implements Parcelable {

    private static final String TAG = VtcModelUser.class.getName();

    private int version = 0;
    private String name = "";
    private String phone = "";
    private String email = "";
    private String device_id = "";
    private String sms_verify = "";
    private String system_id = "";
    private String created_at = "";
    private String _id = "";
    private String avatar = "";

    private static List<Coupons> lstCoupons = new ArrayList<>();

    private String address = "";

    protected VtcModelUser(Parcel in) {
        version = in.readInt();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        device_id = in.readString();
        sms_verify = in.readString();
        system_id = in.readString();
        created_at = in.readString();
        _id = in.readString();
        address = in.readString();
        avatar = in.readString();
    }

    public static final Creator<VtcModelUser> CREATOR = new Creator<VtcModelUser>() {
        @Override
        public VtcModelUser createFromParcel(Parcel in) {
            return new VtcModelUser(in);
        }

        @Override
        public VtcModelUser[] newArray(int size) {
            return new VtcModelUser[size];
        }
    };

    public static VtcModelUser getDataJson(String strData) {

        VtcModelUser vtcModelUser = new VtcModelUser();

        try {
            JSONObject jsonObject = new JSONObject(strData);

            JSONObject userData = jsonObject.optJSONObject(ParserJson.API_PARAMETER_INFO);

            if(userData == null){
                userData = jsonObject;
            }

            if(userData != null) {

                vtcModelUser.setVersion(userData.optInt(ParserJson.API_PARAMETER_VERSION));
                vtcModelUser.setName(userData.optString(ParserJson.API_PARAMETER_NAME));
                vtcModelUser.setPhone(userData.optString(ParserJson.API_PARAMETER_PHONE));
                vtcModelUser.setEmail(userData.optString(ParserJson.API_PARAMETER_EMAIL));
                vtcModelUser.setDevice_id(userData.optString(ParserJson.API_PARAMETER_DEVICE_ID));
                vtcModelUser.setSms_verify(userData.optString(ParserJson.API_PARAMETER_SMS_VERIFY));
                vtcModelUser.setSystem_id(userData.optString(ParserJson.API_PARAMETER_SYSTEM_ID));
                vtcModelUser.setCreated_at(userData.optString(ParserJson.API_PARAMETER_CREATE_AT));
                vtcModelUser.set_id(userData.optString(ParserJson.API_PARAMETER_ID_));
                vtcModelUser.setAvatar(userData.optString(ParserJson.API_PARAMETER_AVATAR));

                // Paser Json address
                vtcModelUser.setAddress(userData.optString(ParserJson.API_PARAMETER_ADDRESSES));

                JSONArray ojbCoupon = userData.optJSONArray(ParserJson.API_PARAMETER_COUPONS);

                if (ojbCoupon != null) {
                    for (int i = 0; i < ojbCoupon.length(); i++) {

                        JSONObject couponObj = ojbCoupon.getJSONObject(i);
                        if(couponObj != null) {

                            Coupons coupons = new Coupons();

                            coupons.setId(couponObj.optString(ParserJson.API_PARAMETER_ID_));
                            coupons.setUsed(couponObj.optString(ParserJson.API_PARAMETER_USED));
                            coupons.setValue(couponObj.optString(ParserJson.API_PARAMETER_VALUE));
                            coupons.setCode(couponObj.optString(ParserJson.API_PARAMETER_CODE));

                            setLstCoupons(coupons);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vtcModelUser;
    }

    public VtcModelUser(){

    }



    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getSms_verify() {
        return sms_verify;
    }

    public void setSms_verify(String sms_verify) {
        this.sms_verify = sms_verify;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Coupons> getLstCoupons() {
        return lstCoupons;
    }

    public void setLstCoupons(List<Coupons> lstCoupons) {
        this.lstCoupons = lstCoupons;
    }

    public static void setLstCoupons(Coupons coupons) {
        VtcModelUser.lstCoupons.add(coupons);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(version);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(device_id);
        dest.writeString(sms_verify);
        dest.writeString(system_id);
        dest.writeString(created_at);
        dest.writeString(_id);
        dest.writeString(address);
        dest.writeString(avatar);
    }

    private static class Coupons implements Parcelable {
        private String code = "";
        private String value = "";
        private String used = "false";
        private String id = "";

        public Coupons(){

        }

        protected Coupons(Parcel in) {
            code = in.readString();
            value = in.readString();
            used = in.readString();
            id = in.readString();
        }

        public static final Creator<Coupons> CREATOR = new Creator<Coupons>() {
            @Override
            public Coupons createFromParcel(Parcel in) {
                return new Coupons(in);
            }

            @Override
            public Coupons[] newArray(int size) {
                return new Coupons[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(code);
            dest.writeString(value);
            dest.writeString(used);
            dest.writeString(id);
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

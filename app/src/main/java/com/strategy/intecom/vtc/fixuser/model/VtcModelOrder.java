package com.strategy.intecom.vtc.fixuser.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixuser.config.VtcDBBase;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 6/30/16.
 */
public class VtcModelOrder implements Parcelable {
    private static final String TAG = VtcModelOrder.class.getSimpleName();

    private String id;
    private String type;
    private String status;
    private String payment_method;
    private String order_time;
    private String description;
    private Field field;
    private User user;
    private String created_at;
    private Agency agency;
    private String comment;
    private int rate;
    private List<String> images;
    private Address address;
    private Coupon_code coupon_code;
    private boolean isSave;


    protected VtcModelOrder(Parcel in) {
        id = in.readString();
        type = in.readString();
        status = in.readString();
        payment_method = in.readString();
        order_time = in.readString();
        description = in.readString();
        field = in.readParcelable(Field.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        created_at = in.readString();
        agency = in.readParcelable(Agency.class.getClassLoader());
        comment = in.readString();
        rate = in.readInt();
        address = in.readParcelable(Address.class.getClassLoader());
        coupon_code = in.readParcelable(Coupon_code.class.getClassLoader());
        isSave = in.readByte() != 0;
    }

    public static final Creator<VtcModelOrder> CREATOR = new Creator<VtcModelOrder>() {
        @Override
        public VtcModelOrder createFromParcel(Parcel in) {
            return new VtcModelOrder(in);
        }

        @Override
        public VtcModelOrder[] newArray(int size) {
            return new VtcModelOrder[size];
        }
    };


    public static List<VtcModelOrder> setDataListOrder(Cursor cursor) {
        List<VtcModelOrder> lst_order = new ArrayList<VtcModelOrder>();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    VtcModelOrder order = getOrderData(new JSONObject(cursor.getString(cursor.getColumnIndex(VtcDBBase.Fav_row_order_detail))));
                    lst_order.add(order);
                }
            }
        } catch (Exception e) {
            AppCore.showLog(TAG + "  " + cursor.getPosition() + " setDataListAddress Exception : " + e.getMessage());
        }finally {
            cursor.close();
        }
        return lst_order;
    }

    public static List<VtcModelOrder> getOrderData(JSONArray jsonArray) {

        if (jsonArray != null && jsonArray.length() > 0) {
            List<VtcModelOrder> orderList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);

                VtcModelOrder vtcModelOrder = getOrderData(jsonObject);

                if (vtcModelOrder != null) {
                    orderList.add(vtcModelOrder);
                }
            }
            return orderList;
        }

        return null;
    }

    public static VtcModelOrder getOrderData(JSONObject jsonObject) {

        if (jsonObject != null) {
            VtcModelOrder vtcModelOrder = new VtcModelOrder();

            vtcModelOrder.setId(jsonObject.optString(ParserJson.API_PARAMETER_ID_));
            vtcModelOrder.setType(jsonObject.optString(ParserJson.API_PARAMETER_TYPE));
            vtcModelOrder.setStatus(jsonObject.optString(ParserJson.API_PARAMETER_STATUS));
            vtcModelOrder.setPayment_method(jsonObject.optString(ParserJson.API_PARAMETER_PAYMENT_METHOD));
            vtcModelOrder.setOrder_time(jsonObject.optString(ParserJson.API_PARAMETER_ORDER_TIME));
            vtcModelOrder.setDescription(jsonObject.optString(ParserJson.API_PARAMETER_DESCRIPTION));
            vtcModelOrder.setField(Field.getField(jsonObject.optJSONObject(ParserJson.API_PARAMETER_FIELD)));
            vtcModelOrder.setUser(User.getUser(jsonObject.optJSONObject(ParserJson.API_PARAMETER_USER)));
            vtcModelOrder.setCreated_at(jsonObject.optString(ParserJson.API_PARAMETER_CREATE_AT));
            vtcModelOrder.setAgency(Agency.getAgency(jsonObject.optJSONObject(ParserJson.API_PARAMETER_AGENCY)));
            vtcModelOrder.setComment(jsonObject.optString(ParserJson.API_PARAMETER_COMMENT));
            vtcModelOrder.setRate(jsonObject.optInt(ParserJson.API_PARAMETER_RATE));
            vtcModelOrder.setImages(jsonObject.optJSONArray(ParserJson.API_PARAMETER_IMAGES));
            vtcModelOrder.setAddress(Address.getAddress(jsonObject.optJSONObject(ParserJson.API_PARAMETER_ADDRESS)));
            vtcModelOrder.setCoupon_code(Coupon_code.getCoupon_code(jsonObject.optJSONObject(ParserJson.API_PARAMETER_COUPON_CODE)));

            AppCore.showLog("---------Status--------------- : " + vtcModelOrder.getStatus());
            return vtcModelOrder;
        }

        return null;
    }

    public VtcModelOrder() {

    }

    public JSONObject exportToJsonObject() {
        JSONObject order = new JSONObject();

        try {
            order.putOpt(ParserJson.API_PARAMETER_ID_, id);
            order.putOpt(ParserJson.API_PARAMETER_TYPE, type);
            order.putOpt(ParserJson.API_PARAMETER_DESCRIPTION, description);
            order.putOpt(ParserJson.API_PARAMETER_ORDER_TIME, order_time);
            order.putOpt(ParserJson.API_PARAMETER_STATUS, status);
            order.putOpt(ParserJson.API_PARAMETER_CREATE_AT, created_at);
            order.putOpt(ParserJson.API_PARAMETER_PAYMENT_METHOD, payment_method);
            order.putOpt(ParserJson.API_PARAMETER_COMMENT, comment);
            order.putOpt(ParserJson.API_PARAMETER_RATE, rate);

            JSONArray imageArray = new JSONArray();
            if (images != null) {
                for (String imageLink : images) {
                    imageArray.put(imageLink);
                }
            }
            order.putOpt(ParserJson.API_PARAMETER_IMAGES, imageArray);

            if (field != null) {
                order.putOpt(ParserJson.API_PARAMETER_FIELD, field.toJson());
            }
            if (user != null) {
                order.putOpt(ParserJson.API_PARAMETER_USER, user.toJson());
            }
            if (agency != null) {
                order.putOpt(ParserJson.API_PARAMETER_AGENCY, agency.toJson());
            }

            if (address != null) {
                order.putOpt(ParserJson.API_PARAMETER_ADDRESS, address.toJson());
            }

            if (coupon_code != null) {
                order.putOpt(ParserJson.API_PARAMETER_COUPON_CODE, coupon_code.toJson());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setImages(JSONArray jsonArray) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject != null) {
                        setImage(jsonObject.optString(ParserJson.API_PARAMETER_LINK));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setImage(String image) {
        if(images == null){
            images = new ArrayList<>();
        }
        this.images.add(image);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Coupon_code getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(Coupon_code coupon_code) {
        this.coupon_code = coupon_code;
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
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(payment_method);
        dest.writeString(order_time);
        dest.writeString(description);
        dest.writeParcelable(field, flags);
        dest.writeParcelable(user, flags);
        dest.writeString(created_at);
        dest.writeParcelable(agency, flags);
        dest.writeString(comment);
        dest.writeInt(rate);
        dest.writeParcelable(address, flags);
        dest.writeParcelable(coupon_code, flags);
        dest.writeByte((byte) (isSave ? 1 : 0));
    }

    public static class Field implements Parcelable {

        private String id;
        private String name;
        private String category_name;
        private String price;

        public static Field getField(JSONObject jsonObject) {
            if (jsonObject != null) {
                Field field = new Field();

                field.setId(jsonObject.optString(ParserJson.API_PARAMETER_ID));
                field.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                field.setCategory_name(jsonObject.optString(ParserJson.API_PARAMETER_CATEGORY_NAME));
                field.setPrice(jsonObject.optString(ParserJson.API_PARAMETER_PRICE));

                return field;
            }

            return null;
        }

        public JSONObject toJson() {
            JSONObject object = new JSONObject();

            try {
                object.putOpt(ParserJson.API_PARAMETER_ID, id);
                object.putOpt(ParserJson.API_PARAMETER_NAME, name);
                object.putOpt(ParserJson.API_PARAMETER_CATEGORY_NAME, category_name);
                object.putOpt(ParserJson.API_PARAMETER_PRICE, price);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return object;
        }

        public Field() {

        }

        protected Field(Parcel in) {
            id = in.readString();
            name = in.readString();
            category_name = in.readString();
            price = in.readString();
        }

        public final Creator<Field> CREATOR = new Creator<Field>() {
            @Override
            public Field createFromParcel(Parcel in) {
                return new Field(in);
            }

            @Override
            public Field[] newArray(int size) {
                return new Field[size];
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
            dest.writeString(category_name);
            dest.writeString(price);
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

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public static class User implements Parcelable {

        private String id;
        private String name;
        private String email;
        private String phone;

        public static User getUser(JSONObject jsonObject) {
            if (jsonObject != null) {
                User user = new User();

                user.setId(jsonObject.optString(ParserJson.API_PARAMETER_ID));
                user.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                user.setEmail(jsonObject.optString(ParserJson.API_PARAMETER_EMAIL));
                user.setPhone(jsonObject.optString(ParserJson.API_PARAMETER_PHONE));
                return user;
            }

            return null;
        }

        public JSONObject toJson() {
            JSONObject object = new JSONObject();

            try {
                object.putOpt(ParserJson.API_PARAMETER_ID, id);
                object.putOpt(ParserJson.API_PARAMETER_NAME, name);
                object.putOpt(ParserJson.API_PARAMETER_EMAIL, email);
                object.putOpt(ParserJson.API_PARAMETER_PHONE, phone);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return object;
        }

        public User() {

        }

        protected User(Parcel in) {
            id = in.readString();
            name = in.readString();
            email = in.readString();
            phone = in.readString();
        }

        public final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
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
            dest.writeString(email);
            dest.writeString(phone);
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
    }

    public static class Agency implements Parcelable {
        private String id;
        private int avg_rate;
        private String level;
        private String phone;
        private String name;
        private String avatar;

        public static Agency getAgency(JSONObject jsonObject) {
            if (jsonObject != null) {
                Agency agency = new Agency();

                agency.setId(jsonObject.optString(ParserJson.API_PARAMETER_ID_));
                agency.setAvg_rate(jsonObject.optInt(ParserJson.API_PARAMETER_RATE_AVG));
                agency.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                agency.setLevel(jsonObject.optString(ParserJson.API_PARAMETER_LEVEL));
                agency.setPhone(jsonObject.optString(ParserJson.API_PARAMETER_PHONE));
                agency.setAvatar(jsonObject.optString(ParserJson.API_PARAMETER_AVATAR));
                return agency;
            }

            return null;
        }

        private Agency() {

        }

        public JSONObject toJson() {
            JSONObject object = new JSONObject();

            try {
                object.putOpt(ParserJson.API_PARAMETER_ID_, id);
                object.putOpt(ParserJson.API_PARAMETER_RATE_AVG, avg_rate);
                object.putOpt(ParserJson.API_PARAMETER_NAME, name);
                object.putOpt(ParserJson.API_PARAMETER_LEVEL, level);
                object.putOpt(ParserJson.API_PARAMETER_PHONE, phone);
                object.putOpt(ParserJson.API_PARAMETER_AVATAR, avatar);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return object;
        }
        protected Agency(Parcel in) {
            id = in.readString();
            avg_rate = in.readInt();
            level = in.readString();
            phone = in.readString();
            name = in.readString();
            avatar = in.readString();
        }

        public final Creator<Agency> CREATOR = new Creator<Agency>() {
            @Override
            public Agency createFromParcel(Parcel in) {
                return new Agency(in);
            }

            @Override
            public Agency[] newArray(int size) {
                return new Agency[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeInt(avg_rate);
            dest.writeString(level);
            dest.writeString(phone);
            dest.writeString(name);
            dest.writeString(avatar);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getAvg_rate() {
            return avg_rate;
        }

        public void setAvg_rate(int avg_rate) {
            this.avg_rate = avg_rate;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class Address implements Parcelable {
        private String name;
        private String slong;
        private String slat;

        public static Address getAddress(JSONObject jsonObject) {
            if (jsonObject != null) {
                Address address = new Address();

                address.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                address.setSlong(jsonObject.optString(ParserJson.API_PARAMETER_LONGITUDE));
                address.setSlat(jsonObject.optString(ParserJson.API_PARAMETER_LATITUDE));
                return address;
            }

            return null;
        }

        public Address() {

        }

        public JSONObject toJson() {
            JSONObject object = new JSONObject();

            try {
                object.putOpt(ParserJson.API_PARAMETER_NAME, name);
                object.putOpt(ParserJson.API_PARAMETER_LONGITUDE, slong);
                object.putOpt(ParserJson.API_PARAMETER_LATITUDE, slat);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return object;
        }

        protected Address(Parcel in) {
            name = in.readString();
            slong = in.readString();
            slat = in.readString();
        }

        public final Creator<Address> CREATOR = new Creator<Address>() {
            @Override
            public Address createFromParcel(Parcel in) {
                return new Address(in);
            }

            @Override
            public Address[] newArray(int size) {
                return new Address[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(slong);
            dest.writeString(slat);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlong() {
            return slong;
        }

        public void setSlong(String slong) {
            this.slong = slong;
        }

        public String getSlat() {
            return slat;
        }

        public void setSlat(String slat) {
            this.slat = slat;
        }
    }

    public static class Coupon_code implements Parcelable {

        private String code;
        private String value;

        public static Coupon_code getCoupon_code(JSONObject jsonObject) {
            if (jsonObject != null) {
                Coupon_code couponCode = new Coupon_code();

                couponCode.setCode(jsonObject.optString(ParserJson.API_PARAMETER_CODE));
                couponCode.setValue(jsonObject.optString(ParserJson.API_PARAMETER_VALUE));
                return couponCode;
            }

            return null;
        }

        public JSONObject toJson() {
            JSONObject object = new JSONObject();

            try {
                object.putOpt(ParserJson.API_PARAMETER_CODE, code);
                object.putOpt(ParserJson.API_PARAMETER_VALUE, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return object;
        }
        public Coupon_code() {

        }

        protected Coupon_code(Parcel in) {
            code = in.readString();
            value = in.readString();
        }

        public final Creator<Coupon_code> CREATOR = new Creator<Coupon_code>() {
            @Override
            public Coupon_code createFromParcel(Parcel in) {
                return new Coupon_code(in);
            }

            @Override
            public Coupon_code[] newArray(int size) {
                return new Coupon_code[size];
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
    }
}

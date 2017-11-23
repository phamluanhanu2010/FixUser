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
 * Created by HungNL on 8/3/2016.
 */
public class VtcModelNotification implements Parcelable{

    private String id;
    private String user;
    private String name;
    private String phone;
    private String createAt;
    private List<Message> messages;

    public VtcModelNotification() {}

    protected VtcModelNotification(Parcel in) {
        id = in.readString();
        user = in.readString();
        name = in.readString();
        phone = in.readString();
        createAt = in.readString();
        messages = in.createTypedArrayList(Message.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(createAt);
        dest.writeTypedList(messages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VtcModelNotification> CREATOR = new Creator<VtcModelNotification>() {
        @Override
        public VtcModelNotification createFromParcel(Parcel in) {
            return new VtcModelNotification(in);
        }

        @Override
        public VtcModelNotification[] newArray(int size) {
            return new VtcModelNotification[size];
        }
    };

    public static VtcModelNotification getNotificationData(JSONObject jsonObject){
        VtcModelNotification noti = new VtcModelNotification();

        try {
            noti.setId(jsonObject.optString(ParserJson.API_PARAMETER_ID_));
            noti.setUser(jsonObject.optString(ParserJson.API_PARAMETER_USER));
            noti.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
            noti.setPhone(jsonObject.optString(ParserJson.API_PARAMETER_PHONE));
            noti.setCreateAt(
                    jsonObject.optString(ParserJson.API_PARAMETER_CREATE_AT));
            noti.setMessages(Message.getMessageList(jsonObject.getJSONArray
                    (ParserJson.API_PARAMETER_MESSAGE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noti;
    }

    public static ArrayList<VtcModelNotification> getListNotification(JSONArray
            jsonArray) {
        ArrayList<VtcModelNotification> notiList = new ArrayList<VtcModelNotification>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                notiList.add(getNotificationData(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notiList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(
            List<Message> messages) {
        this.messages = messages;
    }

    public static class Message implements Parcelable {
        private String id;
        private String title;
        private String messageContent;
        private String from;
        private String createAt;
        private boolean isReaded;

        public  Message() {

        }

        protected Message(Parcel in) {
            id = in.readString();
            title = in.readString();
            messageContent = in.readString();
            from = in.readString();
            createAt = in.readString();
            isReaded = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(title);
            dest.writeString(messageContent);
            dest.writeString(from);
            dest.writeString(createAt);
            dest.writeByte((byte) (isReaded ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Message> CREATOR = new Creator<Message>() {
            @Override
            public Message createFromParcel(Parcel in) {
                return new Message(in);
            }

            @Override
            public Message[] newArray(int size) {
                return new Message[size];
            }
        };

        public static Message getMessage(JSONObject jsonObject)  {
            Message msg = new Message();

            msg.setId(jsonObject.optString(ParserJson.API_PARAMETER_ID_));
            msg.setTitle(jsonObject.optString(ParserJson.API_PARAMETER_TITLE));
            msg.setMessageContent(jsonObject.optString(ParserJson
                    .API_PARAMETER_MESSAGE));
            msg.setFrom(jsonObject.optString(ParserJson
                    .API_PARAMETER_FROM));
            msg.setCreateAt(jsonObject.optString(ParserJson
                    .API_PARAMETER_CREATE_AT));
            msg.setReaded(jsonObject.optBoolean(ParserJson
                    .API_PARAMETER_IS_READED));

            return msg;
        }

        public static List<Message> getMessageList(JSONArray msgArray){
            List<Message> msgList = new ArrayList<Message>();
            try {
                for (int i = 0; i < msgArray.length(); i++) {
                    msgList.add(getMessage(msgArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return msgList;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public boolean isReaded() {
            return isReaded;
        }

        public void setReaded(boolean isReaded) {
            this.isReaded = isReaded;
        }

    }
}

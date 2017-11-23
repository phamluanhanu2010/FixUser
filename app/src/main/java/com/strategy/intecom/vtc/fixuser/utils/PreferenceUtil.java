package com.strategy.intecom.vtc.fixuser.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private SharedPreferences IShare = null;

    /**
     * isCheck Login
     * is return boolean
     */
    public static final String IS_ACTIVE = "is_active";

    public static final String USER_INFO_AVATAR = "user_info_avatar";
    public static final String USER_INFO = "user_info";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String DEVICE_ID = "device_id";
    public static final String IS_KEEP_WORKING = "keep_working";


    public PreferenceUtil(Context context) {
        if (context != null)
            IShare = context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
    }


    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Remove Share Preferences
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    public void PreferenceUtilRemove(Context context) {
        if (context != null)
            IShare = context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
        IShare.edit().clear().commit();
    }


    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Remove Data When Logout User
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    public void RemoveDataWhenLogOut() {
        removeValue(IS_ACTIVE);
        removeValue(USER_INFO_AVATAR);
        removeValue(USER_INFO);
        removeValue(AUTH_TOKEN);
        removeValue(DEVICE_ID);
        removeValue(IS_KEEP_WORKING);
    }

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Get Set By Other Key
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    public void setValueLong(String key, long val) {
        IShare.edit().putLong(key, val).commit();
    }

    public void setValueString(String key, String val) {
        IShare.edit().putString(key, val).commit();
    }

    public void setValueBoolean(String key, boolean val) {
        IShare.edit().putBoolean(key, val).commit();
    }

    public void setValueInteger(String key, int val) {
        IShare.edit().putInt(key, val).commit();
    }

    public int getValueInteger(String key) {
        return IShare.getInt(key, -1);
    }

    public long getValueLong(String key) {
        return IShare.getLong(key, -1);
    }

    public boolean getValueBoolean(String key) {
        return IShare.getBoolean(key, false);
    }

    public String getValueString(String key) {
        return IShare.getString(key, "").trim().toString();
    }

    public void removeValue(String key) {
        IShare.edit().remove(key).commit();
    }

}

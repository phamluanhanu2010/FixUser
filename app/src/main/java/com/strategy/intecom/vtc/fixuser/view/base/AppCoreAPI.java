package com.strategy.intecom.vtc.fixuser.view.base;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelUser;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Socket;

/**
 * Created by Mr. Ha on 6/29/16.
 */
public class AppCoreAPI extends AppCoreBase implements RequestListener {

    protected void initGetLogin(RequestListener requestListener, String phoneNum, String password) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_PASSWORD, password);
            jsonObject.put(ParserJson.API_PARAMETER_DEVICE_ID, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.DEVICE_ID));

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_LOGIN, RequestListener.API_CONNECTION_LOGIN, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void initLogOut(RequestListener requestListener) {
        // TODO init logout
        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString
                (PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_LOGOUT, RequestListener
                .API_CONNECTION_LOGOUT + VtcNWConnection.urlEncodeUTF8(map), null, false, true);
    }

    protected void initGetRegister(RequestListener requestListener, String sName, String phoneNum, String password, String email) {
        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_NAME, sName);
            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_PASSWORD, password);
            jsonObject.put(ParserJson.API_PARAMETER_EMAIL, email);
            jsonObject.put(ParserJson.API_PARAMETER_OS_TYPE, Const.OS_TYPE);
            jsonObject.put(ParserJson.API_PARAMETER_DEVICE_ID, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.DEVICE_ID));

            AppCore.showLog("---------------- : " + sName + " ---- : " + phoneNum + " -------- : " + password + " ------ : " + email  );
            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_REGISTER, RequestListener.API_CONNECTION_REGISTER, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initFogotPassword(RequestListener requestListener, String phoneNum) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_FORGOT_PASSWORD, RequestListener.API_CONNECTION_FORGOT_PASSWORD, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initFogotChangePassword(RequestListener requestListener, String phoneNum, String confirmCode, String password) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_PASSCODE, confirmCode);
            jsonObject.put(ParserJson.API_PARAMETER_PASSWORD, password);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CHANGE_PASSWORD, RequestListener.API_CONNECTION_CHANGE_PASSWORD, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initChangePassword(RequestListener requestListener, String userID, String oldPassword, String newPassword) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_OLD_PASSWORD, oldPassword);
            jsonObject.put(ParserJson.API_PARAMETER_NEW_PASSWORD, newPassword);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CHANGE_PASSWORD, RequestListener.API_CONNECTION_USER + userID + RequestListener.API_CONNECTION_CHANGE_PASSWORD + VtcNWConnection.urlEncodeUTF8(map), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initConfirmPassCode(RequestListener requestListener, String phoneNum, String passCode) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_PASSCODE, passCode);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CONFIRM_PASSCODE, RequestListener.API_CONNECTION_PASSCODE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initGetPassCode(RequestListener requestListener, String phoneNum, String strtyle) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);
            jsonObject.put(ParserJson.API_PARAMETER_TYPE, strtyle);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_PASSCODE, RequestListener.API_CONNECTION_GET_PASSCODE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initGetFieldLst(RequestListener requestListener, boolean isRefresh) {
        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_FIELD, RequestListener.API_CONNECTION_GET_FIELD_LIST + VtcNWConnection.urlEncodeUTF8(map), null, false, isRefresh);
    }

    protected static void initGetNearbyLst(RequestListener requestListener, String lat, String log, String fieldId) {
        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));
        if(!fieldId.equals("")) {
            map.put(ParserJson.API_PARAMETER_FIELD_ID, fieldId);
        }

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_NEARBY, RequestListener.API_CONNECTION_GET_AGENCY_NEARBY + "/" + lat + "," + log + VtcNWConnection.urlEncodeUTF8(map), null, false, false);
    }

    protected void initGetOrderLst(RequestListener requestListener, String status, boolean isShowProcess) {

            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));
            map.put(ParserJson.API_PARAMETER_LIMIT, String.valueOf(30));
            map.put(ParserJson.API_PARAMETER_STATUS, status);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_NEARBY, RequestListener.API_CONNECTION_GET_ORDER_LIST + VtcNWConnection.urlEncodeUTF8(map), null, false, isShowProcess);
    }


    protected void initCreateOrder(RequestListener requestListener, VtcModelNewOrder modelNewOrder, VtcModelUser vtcModelUser, boolean isShowProcess) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_TYPE, modelNewOrder.getType());

            if(vtcModelUser != null) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put(ParserJson.API_PARAMETER_ID, vtcModelUser.get_id());
                jsonUser.put(ParserJson.API_PARAMETER_NAME, vtcModelUser.getName());
                jsonUser.put(ParserJson.API_PARAMETER_EMAIL, vtcModelUser.getEmail());
                jsonUser.put(ParserJson.API_PARAMETER_PHONE, vtcModelUser.getPhone());

                jsonObject.put(ParserJson.API_PARAMETER_USER, jsonUser);
            }

            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put(ParserJson.API_PARAMETER_NAME, modelNewOrder.getAddname());
            jsonAddress.put(ParserJson.API_PARAMETER_LONGITUDE, modelNewOrder.getAddlong());
            jsonAddress.put(ParserJson.API_PARAMETER_LATITUDE, modelNewOrder.getAddlat());

            jsonObject.put(ParserJson.API_PARAMETER_ADDRESS, jsonAddress);

            jsonObject.put(ParserJson.API_PARAMETER_FIELD_ID, modelNewOrder.getFieldId());
            jsonObject.put(ParserJson.API_PARAMETER_DESCRIPTION, modelNewOrder.getDescription());
            jsonObject.put(ParserJson.API_PARAMETER_ORDER_TIME, modelNewOrder.getOrder_time());
            jsonObject.put(ParserJson.API_PARAMETER_COUPON_CODE, modelNewOrder.getCoupon_code());

            JSONArray jsonArray = new JSONArray();

            if(modelNewOrder.getLstImageBase64() != null && modelNewOrder.getLstImageBase64().size() > 0) {
                for (String image : modelNewOrder.getLstImageBase64()){
                    jsonArray.put(image);
                }
            }

            jsonObject.put(ParserJson.API_PARAMETER_IMAGES, jsonArray);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_CREATE_ORDER, RequestListener.API_CONNECTION_CREATE_ORDER + VtcNWConnection.urlEncodeUTF8(map), jsonObject, true, isShowProcess);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initUpdateStatusOrder(RequestListener requestListener, StatusBookingJob status, String idOrder, String lydo) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ParserJson.API_PARAMETER_STATUS, status.getValuesStatus());

            if(!lydo.equals("")) {
                jsonObject.put(ParserJson.API_PARAMETER_CANCEL_REASON, lydo);
            }

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_UPDATE_STATUS_ORDER, RequestListener.API_CONNECTION_ORDER + idOrder + RequestListener.API_CONNECTION_ORDER_AGENCY_UPDATE + VtcNWConnection.urlEncodeUTF8(map) , jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initUpLoadAvatar(RequestListener requestListener, String avatarLink) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_UPLOAD_AVATAR, RequestListener.API_CONNECTION_USER + getVtcModelUser().get_id() + RequestListener.API_CONNECTION_UPLOAD_AVATAR + VtcNWConnection.urlEncodeUTF8(map), avatarLink, false);
    }
    public static void initUpdateProfile(RequestListener requestListener, String idUser, String name, String email) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            if(!name.isEmpty()){
                jsonObject.put(ParserJson.API_PARAMETER_NAME, name);
            }if(!email.isEmpty()){
                jsonObject.put(ParserJson.API_PARAMETER_EMAIL, email);
            }
            jsonObject.put(ParserJson.API_PARAMETER_PHONE, getVtcModelUser().getPhone());

            jsonObject.put(ParserJson.API_PARAMETER_OS_TYPE, Const.OS_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_UPDATE_PROFILE, RequestListener.API_CONNECTION_USER + idUser + RequestListener.API_CONNECTION_USER_UPDATE + VtcNWConnection.urlEncodeUTF8(map) , jsonObject, true, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initCheckCounpon(RequestListener requestListener, String counpon) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CHECK_COUPON, RequestListener.API_CONNECTION_CHECK_COUNPON + counpon + VtcNWConnection.urlEncodeUTF8(map), null, false, true);
    }

    public static void initReSearchOrder(RequestListener requestListener, String orderID) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_RESEARCH, RequestListener.API_CONNECTION_ORDER + orderID + RequestListener.API_CONNECTION_RESEARCH + VtcNWConnection.urlEncodeUTF8(map));
    }

    public static void initCancelOrderMark(RequestListener requestListener, String userID, String orderID, String cancel_reason) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_CANCEL_REASON, cancel_reason);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CANCEL_ORDER_MARK, RequestListener.API_CONNECTION_USER + userID + RequestListener.API_CONNECTION_MASRK_AS_CANCEL + orderID + VtcNWConnection.urlEncodeUTF8(map), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initRatePepairer(RequestListener requestListener, String userID, int rate, String customer_name, String comment) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_RATE, rate);
            jsonObject.put(ParserJson.API_PARAMETER_CUSTOMER_NAME, customer_name);
            jsonObject.put(ParserJson.API_PARAMETER_COMMENT, comment);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CANCEL_ORDER_MARK, RequestListener.API_CONNECTION_ORDER + userID + RequestListener.API_CONNECTION_RATE + VtcNWConnection.urlEncodeUTF8(map), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initGetNotificationList(RequestListener
            requestListener, String userID) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                AppCore.getPreferenceUtil(getCurrentActivity())
                        .getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess
                (TypeActionConnection.TYPE_ACTION_NOTIFICATION_LIST,
                        RequestListener.API_CONNECTION_NOTIFICATION_LIST
                                + userID + VtcNWConnection.urlEncodeUTF8(map),
                        false);
    }

    protected void initGetCommonInfo(RequestListener requestListener) {
        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_COMMONINFO, RequestListener.API_CONNECTION_GET_COMMONINFO, false);
    }

    protected static void initSocketConnecting(RequestListener requestListener) {

        getConnection(requestListener).onExcuteResultSocket();

    }

    protected void initSocketDisconnect(RequestListener requestListener) {

        getConnection(requestListener).onExcuteDisconnectSocket();

    }

    protected static void initSendRequestGetListagency(){
//        try {
//            JSONObject obj = new JSONObject();
//            obj.put(ParserJson.API_PARAMETER_ORDER_ID, id);
//            if (getConnection(null).getSocket() != null) {
//                getConnection(null).getSocket().emit(RequestListener.SOCKET_EVENT_ACCEPT_OFFER, obj);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    protected void initRequestLocation(Socket socket, String agency_id){
        try {
            JSONObject obj = new JSONObject();
            obj.put(ParserJson.API_PARAMETER_AGENCY_ID, agency_id);
            if (socket != null) {
                socket.emit(RequestListener.SOCKET_EVENT_RECEIVE_LOCATION, obj);
            }else {

                if(timerRequestLocation != null) {
                    timerRequestLocation.cancel();
                    timerRequestLocation = null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        switch (keyType) {

            case TYPE_CONNECTION_NOT_CONNECT_SERVER:
                initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                        getCurrentActivity().getResources().getString(R.string.confirm_server_not_found));
                break;
            case TYPE_CONNECTION_NO_INTERNET:
                initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_ENABLE_NETWORK);
                break;
            case TYPE_CONNECTION_TIMEOUT:
                initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                        getCurrentActivity().getResources().getString(R.string.confirm_server_timeout));
                break;
            case TYPE_CONNECTION_ERROR:
                initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                        getCurrentActivity().getResources().getString(R.string.confirm_server_error_connect));
                break;
            case TYPE_CONNECTION_ERROR_FROM_SERVER:
                if (!msg.equals("")) {
                    initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", msg);
                }
                break;
            default:
                if (!msg.equals("")) {
                    initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", msg);
                }
                return;
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {

    }

    @Override
    public void onResultSocket(TypeActionConnection keyType, Socket socket, final String response) {
        switch (keyType) {
            case TYPE_ACTION_SOCKET_ACCEPT_RESULT:

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    VtcModelOrder.Agency agency = VtcModelOrder.Agency.getAgency(jsonObject);

                    setTimeGetLocation(socket, agency);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initReceived(Const.UI_MY_CONFIRM_ACCEPT_JOB, response);
                break;

            case TYPE_ACTION_SOCKET_LIST_AGENCY:
                initReceived(Const.UI_REPAIRER_SEARCH, response);
                break;

            case TYPE_ACTION_SOCKET_RECEIVE_LOCATION:
                initReceived(Const.UI_MY_CONFIRM_GET_LOCATION, response);
                break;

            default:
                break;

        }
    }

    private static Timer timerRequestLocation;

    public Timer getTimerRequestLocation() {
        return timerRequestLocation;
    }

    public void setTimerRequestLocation(Timer timerRequestLocation) {
        AppCoreAPI.timerRequestLocation = timerRequestLocation;
    }

    public void setTimeGetLocation(final Socket socket, final VtcModelOrder.Agency timeGetLocation) {

        timerRequestLocation = new Timer();

        timerRequestLocation.schedule(new TimerTask() {
            @Override
            public void run() {

                if(timeGetLocation != null) {

                    initRequestLocation(socket, timeGetLocation.getId());
                }
            }
        }, 0, 1000 * 3);

    }
}

package com.strategy.intecom.vtc.fixuser.interfaces;

import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;

import io.socket.client.Socket;

/**
 * Created by Mr. Ha on 5/19/16.
 */
public interface RequestListener {

//    void onPostExecuteStart(TypeActionConnection keyType, String sApi);

    void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg);

    void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message);

    void onResultSocket(TypeActionConnection keyType, Socket socket, String response);

    String SOCKET_EVENT_ACCEPT_RESULT = "server:accepted_offer";
    String SOCKET_EVENT_RECEIVE_LIST_AGENCY = "server:return_agencies";
    String SOCKET_EVENT_REQUEST_LOCATION = "server:send_location";
    String SOCKET_EVENT_RECEIVE_LOCATION = "user:get_location";

    String API_CONNECTION_LOGIN = "/user/login";
    String API_CONNECTION_LOGOUT = "/api/user/logout";
    String API_CONNECTION_REGISTER = "/user/register";
    String API_CONNECTION_GET_FIELD_LIST = "/api/field/getList";
    String API_CONNECTION_PASSCODE = "/verify";
    String API_CONNECTION_GET_PASSCODE = "/getVerifyCode";
    String API_CONNECTION_GET_AGENCY_NEARBY = "/api/user/getAgencies";
    String API_CONNECTION_CREATE_ORDER = "/api/order/create";
    String API_CONNECTION_GET_ORDER_LIST = "/api/order/listByUser";
    String API_CONNECTION_USER = "/api/user/";
    String API_CONNECTION_NOTIFICATION_LIST = "/api/feedback/get/";
    String API_CONNECTION_NOTIFICATION_COUNT = "/api/feedback/countUnread";
    String API_CONNECTION_MESSAGE_READ = "/api/feedback/";
    String API_CONNECTION_SEND_FEEDBACK = "/api/feedback/addFeedback";
    String API_CONNECTION_FORGOT_PASSWORD = "/forgotPassword";
    String API_CONNECTION_CHANGE_PASSWORD = "/changePassword";

    String API_CONNECTION_CHECK_COUNPON = "/api/user/checkCoupon/";

    String API_CONNECTION_RESEARCH = "/findMoreAgencies/";
    String API_CONNECTION_MASRK_AS_CANCEL = "/markAsCancel/";

//    /api/user/:id/markAsCancel/:orderId
//    /api/order/:id/rate


    String API_CONNECTION_ORDER = "/api/order/";
    String API_CONNECTION_ORDER_AGENCY_APPLY = "/apply";
    String API_CONNECTION_ORDER_AGENCY_UPDATE = "/updateStatus";
    String API_CONNECTION_ORDER_AGENCY_DELETE = "/delete";
    String API_CONNECTION_ORDER_UPLOAD_FILE = "/uploadImages";
    String API_CONNECTION_UPLOAD_AVATAR = "/changeAvatar";
    String API_CONNECTION_RATE = "/rate";
    String API_CONNECTION_USER_UPDATE = "/update";
    String API_CONNECTION_GET_COMMONINFO = "/getCommonInfo";


}

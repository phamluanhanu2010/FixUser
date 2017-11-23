package com.strategy.intecom.vtc.fixuser.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/2/16.
 */
public enum TypeActionConnection {

    TYPE_ACTION(0),

    TYPE_ACTION_LOGIN(1),

    TYPE_ACTION_REGISTER(2),

    TYPE_ACTION_GET_PASSCODE(3),

    TYPE_ACTION_CONFIRM_PASSCODE(4),

    TYPE_ACTION_GET_LIST_FIELD(5),

    TYPE_ACTION_GET_LIST_NEARBY(6),

    TYPE_ACTION_GET_CREATE_ORDER(7),

    TYPE_ACTION_UPDATE_STATUS_ORDER(8),

    TYPE_ACTION_GET_LIST_COMMONINFO(9),

    TYPE_ACTION_UPDATE_PROFILE(10),

    TYPE_ACTION_CHECK_COUPON(11),

    TYPE_ACTION_SOCKET_ACCEPT_RESULT(12),

    TYPE_ACTION_SOCKET_LIST_AGENCY(13),

    TYPE_ACTION_SOCKET_RECEIVE_LOCATION(14),

    TYPE_ACTION_RESEARCH(15),

    TYPE_ACTION_CANCEL_ORDER_MARK(16),

    TYPE_ACTION_NOTIFICATION_LIST(17),

    TYPE_ACTION_MESSAGE_READ(18),

    TYPE_ACTION_SEND_FEEDBACK(19),

    TYPE_ACTION_UPLOAD_AVATAR(20),

    TYPE_ACTION_FORGOT_PASSWORD(21),

    TYPE_ACTION_CHANGE_PASSWORD(22),

    TYPE_GET_UNREAD_FEEDBACK_COUNT(23),

    TYPE_LOGOUT(24);


    private static final Map<Integer, TypeActionConnection> typesByValue = new HashMap<>();

    private final int valuesConnectionType;

    TypeActionConnection(int value) {
        this.valuesConnectionType = value;
    }

    public int getValuesTypeDialog() {
        return valuesConnectionType;
    }

    static {
        for (TypeActionConnection type : TypeActionConnection.values()) {
            typesByValue.put(type.valuesConnectionType, type);
        }
    }

    public static TypeActionConnection forValue(int value) {
        TypeActionConnection type = typesByValue.get(value);
        if (type == null)
            return TypeActionConnection.TYPE_ACTION;
        return typesByValue.get(value);
    }
}

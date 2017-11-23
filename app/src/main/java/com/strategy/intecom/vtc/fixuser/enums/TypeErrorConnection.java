package com.strategy.intecom.vtc.fixuser.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/2/16.
 */
public enum TypeErrorConnection {

    TYPE_CONNECTION(0),

    TYPE_CONNECTION_NO_INTERNET(1),

    TYPE_CONNECTION_TIMEOUT(2),

    TYPE_CONNECTION_NOT_CONNECT_SERVER(3),

    TYPE_CONNECTION_ERROR(4),

    TYPE_CONNECTION_ERROR_FROM_SERVER(5),

    TYPE_CONNECTION_ERROR_CODE_JSON(6),

    TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE(1003);


    private static final Map<Integer, TypeErrorConnection> typesByValue = new HashMap<>();

    private final int valuesConnectionType;

    TypeErrorConnection(int value) {
        this.valuesConnectionType = value;
    }

    public int getValuesTypeDialog() {
        return valuesConnectionType;
    }

    static {
        for (TypeErrorConnection type : TypeErrorConnection.values()) {
            typesByValue.put(type.valuesConnectionType, type);
        }
    }

    public static TypeErrorConnection forValue(int value) {
        TypeErrorConnection type = typesByValue.get(value);
        if (type == null)
            return TypeErrorConnection.TYPE_CONNECTION;
        return typesByValue.get(value);
    }
}

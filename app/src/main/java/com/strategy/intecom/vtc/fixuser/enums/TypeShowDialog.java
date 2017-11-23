package com.strategy.intecom.vtc.fixuser.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public enum TypeShowDialog {

    TYPE_SHOW_MESSAGE_INFO(0),

    TYPE_SHOW_MESSAGE_CONFIRM(1),

    TYPE_SHOW_MESSAGE_INFO_REPAIRER(2),

    TYPE_SHOW_MESSAGE_INFO_NOTE(3),

    TYPE_SHOW_MESSAGE_INFO_PAY_TYPE(4),

    TYPE_SHOW_MESSAGE_INFO_TIME(5),

    TYPE_SHOW_MESSAGE_INFO_PROMOTION(6),

    TYPE_SHOW_MESSAGE_CANCEL(7),

    TYPE_SHOW_ENABLE_NETWORK(8),

    TYPE_SHOW_OPTION_CHOICE_MEDIA(9),

    TYPE_SHOW_MESSAGE_CHANGE_ORDER_TIME(10),

    TYPE_SHOW_ENABLE_GPS(11),

    TYPE_SHOW_FILL_ADDRESS(12),

    TYPE_SHOW_FILL_TIME(13);

    private static final Map<Integer, TypeShowDialog> typesByValue = new HashMap<>();

    private final int valuesDialogType;

    TypeShowDialog(int value) {
        this.valuesDialogType = value;
    }

    public int getValuesTypeDialog() {
        return valuesDialogType;
    }

    static {
        for (TypeShowDialog type : TypeShowDialog.values()) {
            typesByValue.put(type.valuesDialogType, type);
        }
    }

    public static TypeShowDialog forValue(int value) {
        TypeShowDialog type = typesByValue.get(value);
        if (type == null)
            return TypeShowDialog.TYPE_SHOW_MESSAGE_INFO;
        return typesByValue.get(value);
    }

}

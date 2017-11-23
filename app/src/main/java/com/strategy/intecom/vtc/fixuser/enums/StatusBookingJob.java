package com.strategy.intecom.vtc.fixuser.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public enum StatusBookingJob {

    STATUS("status"),

    STATUS_FINISH("Finished"),

    STATUS_ACCEPTED("Accepted"),

    STATUS_FINDING("Finding"),

    STATUS_WORKING("Working"),

    STATUS_COMING("Coming"),

    STATUS_USER_CANCEL("User cancel"),

    STATUS_AGENCY_CANCEL("Agency cancel"),

    STATUS_EXPIRED("Expried");


//    private final String STATUS_FINISH = "Finished";
//    private final String STATUS_ACCEPTED = "Accepted";
//    private final String STATUS_FINDING = "Finding";
//    private final String STATUS_WORKING = "Working";
//    private final String STATUS_CANCEL = "User cancel";
//    private final String STATUS_EXPRIED = "Expried";

    private static final Map<String, StatusBookingJob> typesByValue = new HashMap<>();

    private final String valuesStatus;

    StatusBookingJob(String value) {
        this.valuesStatus = value;
    }

    public String getValuesStatus() {
        return valuesStatus;
    }

    static {
        for (StatusBookingJob type : StatusBookingJob.values()) {
            typesByValue.put(type.valuesStatus, type);
        }
    }

    public static StatusBookingJob forValue(int value) {
        StatusBookingJob type = typesByValue.get(value);
        if (type == null)
            return StatusBookingJob.STATUS_FINISH;
        return typesByValue.get(value);
    }

}

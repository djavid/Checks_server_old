package com.djavid.checkserver.util;

import java.util.Base64;

public class Config {

    private static final String FNS_USERNAME = "+79639666964";
    private static final String FNS_PASSWORD = "732278";

    public static final long CHECK_UPDATE_DELAY = 1000;
    public static final int CHECK_EXPIRE_HOURS = 25;
    public static final long CHECK_FNS_POLLING_DELAY_SECONDS = 1;
    public static final int CHECK_FNS_POLLING_COUNT = 6;

    public static final String ERROR_CHECK_NOT_FOUND = "Check not found.";
    public static final String ERROR_CHECK_EXISTS = "Check already exists.";
    public static final String ERROR_CHECK_NOT_LOADED = "Check has not loaded yet.";
    public static final String ERROR_CHECK_DATE_CORRUPTED = "Check date is corrupted.";

    public static final String ERROR_TOKEN_INCORRECT = "Token is incorrect";
    public static final String ERROR_SHIT_HAPPENS = "Something gone wrong";


    public static String getAuthToken() {
        String credentials = FNS_USERNAME + ":" + FNS_PASSWORD;

        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

}

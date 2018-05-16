package com.djavid.checkserver.util;

import java.util.Base64;

public class Config {

    private static final String FNS_USERNAME = "+79639666964";
    private static final String FNS_PASSWORD = "732278";


    public static String getAuthToken() {
        String credentials = FNS_USERNAME + ":" + FNS_PASSWORD;

        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

}

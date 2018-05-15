package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.RegistrationToken;

public class Result {

    RegistrationToken token;


    public Result(RegistrationToken token) {
        this.token = token;
    }


    public RegistrationToken getToken() {
        return token;
    }

    public void setToken(RegistrationToken token) {
        this.token = token;
    }
}

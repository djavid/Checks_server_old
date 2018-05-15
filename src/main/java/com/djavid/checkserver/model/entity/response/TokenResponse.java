package com.djavid.checkserver.model.entity.response;


import com.djavid.checkserver.model.entity.RegistrationToken;

public class TokenResponse {

    String error;
    RegistrationToken result;

    public TokenResponse(RegistrationToken result) {
        this.result = result;
        error = "";
    }

    public TokenResponse(String error) {
        this.error = error;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public RegistrationToken getResult() {
        return result;
    }

    public void setResult(RegistrationToken result) {
        this.result = result;
    }
}

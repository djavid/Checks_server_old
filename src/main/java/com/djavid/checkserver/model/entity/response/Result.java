package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;

import java.util.List;

public class Result {

    RegistrationToken token;
    List<Receipt> receipts;

    public Result(RegistrationToken token) {
        this.token = token;
    }

    public Result(List<Receipt> receipts) {
        this.receipts = receipts;
    }


    public RegistrationToken getToken() {
        return token;
    }

    public void setToken(RegistrationToken token) {
        this.token = token;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }
}

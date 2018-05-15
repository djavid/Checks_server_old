package com.djavid.checkserver.model.entity.response;


public class BaseResponse {

    String status;
    String reason;
    Object result;

    public BaseResponse(Object result) {
        this.result = result;
        status = "ok";
    }

    public BaseResponse(String error) {
        status = "bad";
        this.reason = error;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

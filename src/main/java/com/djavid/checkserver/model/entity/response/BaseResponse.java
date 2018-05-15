package com.djavid.checkserver.model.entity.response;


public class BaseResponse {

    String status;
    String reason;
    Result result;

    public BaseResponse(Result result) {
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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}

package com.djavid.checkserver.model.entity.response;


public class BaseResponse {

    private String error;
    private Object result;

    public BaseResponse(Object result) {
        this.result = result;
        error = "";
    }

    public BaseResponse(String error) {
        this.error = error;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

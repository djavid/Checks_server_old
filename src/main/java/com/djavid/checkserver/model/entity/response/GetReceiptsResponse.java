package com.djavid.checkserver.model.entity.response;


public class GetReceiptsResponse {

    private String error;
    private Object result;

    public GetReceiptsResponse(Object result) {
        this.result = result;
        error = "";
    }

    public GetReceiptsResponse(String error) {
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

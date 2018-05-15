package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Receipt;

import java.util.List;

public class GetReceiptsResponse {

    private String error;
    private List<Receipt> result;

    public GetReceiptsResponse(List<Receipt> result) {
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

    public List<Receipt> getResult() {
        return result;
    }

    public void setResult(List<Receipt> result) {
        this.result = result;
    }
}

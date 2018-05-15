package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Receipt;

import java.util.List;

public class GetReceiptsResponse {

    private boolean hasNext;
    private List<Receipt> receipts;


    public GetReceiptsResponse(List<Receipt> receipts, boolean hasNext) {
        this.hasNext = hasNext;
        this.receipts = receipts;
    }


    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }
}

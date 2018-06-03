package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Item;

import java.util.List;

public class GetItemsResponse {

    private boolean hasNext;
    private List<Item> items;


    public GetItemsResponse(List<Item> items, boolean hasNext) {
        this.hasNext = hasNext;
        this.items = items;
    }


    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

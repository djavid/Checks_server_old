package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Item;

public class ResponseItem {

    private Long itemId;
    private Long receiptId;
    private double quantity;
    private Long price;
    private Long sum;
    private String name;
    private Long nds18;
    private Long nds10;


    public ResponseItem(Item item) {
        this.itemId = item.getItemId();
        this.receiptId = item.getReceipt().getReceiptId();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.sum = item.getSum();
        this.name = item.getName();
        this.nds18 = item.getNds18();
        this.nds10 = item.getNds10();
    }
}

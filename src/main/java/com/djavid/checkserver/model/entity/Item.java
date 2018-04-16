package com.djavid.checkserver.model.entity;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long item_id;
    private Long quantity;
    private Long price;
    private Long sum;
    private String name;
    private Long nds18;
    private Long nds10;

    @ManyToOne
    @JoinColumn(name="receipt_id")
    private Receipt receipt;


    public Item() { }

    @Override
    public String toString() {
        return "Item{" +
                "item_id=" + item_id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", sum=" + sum +
                ", name='" + name + '\'' +
                ", nds18=" + nds18 +
                ", nds10=" + nds10 +
                ", receipt=" + receipt +
                '}';
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNds18() {
        return nds18;
    }

    public void setNds18(Long nds18) {
        this.nds18 = nds18;
    }

    public Long getNds10() {
        return nds10;
    }

    public void setNds10(Long nds10) {
        this.nds10 = nds10;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}

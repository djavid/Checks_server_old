package com.djavid.checkserver.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;
    private double quantity;
    private Long price;
    private Long sum;
    private String name;
    private Long nds18;
    private Long nds10;
    private String category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="receipt_id")
    private Receipt receipt;

    public Item(Long quantity, Long price, Long sum, String name, Long nds18, Long nds10, Receipt receipt) {
        this.quantity = quantity;
        this.price = price;
        this.sum = sum;
        this.name = name;
        this.nds18 = nds18;
        this.nds10 = nds10;
        this.receipt = receipt;
        receipt.addItem(this);
    }

    public Item() { }


    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", sum=" + sum +
                ", name='" + name + '\'' +
                ", nds18=" + nds18 +
                ", nds10=" + nds10 +
                ", category='" + category + '\'' +
                ", receipt=" + receipt +
                '}';
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

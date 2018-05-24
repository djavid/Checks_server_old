package com.djavid.checkserver.model.entity.response;

import java.util.ArrayList;
import java.util.List;

public class StatPercentResponse {

    private List<Percentage> categories = new ArrayList<>();
    private List<Percentage> shops = new ArrayList<>();

    private double totalSum;


    public StatPercentResponse() { }

    public StatPercentResponse(List<Percentage> categories, List<Percentage> shops, double totalSum) {
        this.categories = categories;
        this.shops = shops;
        this.totalSum = totalSum;
    }


    public List<Percentage> getCategories() {
        return categories;
    }

    public void setCategories(List<Percentage> categories) {
        this.categories = categories;
    }

    public List<Percentage> getShops() {
        return shops;
    }

    public void setShops(List<Percentage> shops) {
        this.shops = shops;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }
}

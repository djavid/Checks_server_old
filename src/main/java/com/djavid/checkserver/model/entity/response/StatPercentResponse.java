package com.djavid.checkserver.model.entity.response;

import java.util.ArrayList;
import java.util.List;

public class StatPercentResponse {

    private List<Percentage> percentages = new ArrayList<>();
    private double totalSum;


    public StatPercentResponse() { }

    public StatPercentResponse(List<Percentage> percentages, double totalSum) {
        this.percentages = percentages;
        this.totalSum = totalSum;
    }


    public List<Percentage> getPercentages() {
        return percentages;
    }

    public void setPercentages(List<Percentage> percentages) {
        this.percentages = percentages;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }
}

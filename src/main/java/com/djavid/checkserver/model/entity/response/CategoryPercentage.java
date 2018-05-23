package com.djavid.checkserver.model.entity.response;

public class CategoryPercentage {

    private String category;
    private Double percentage;
    private Double sum;


    public CategoryPercentage() { }

    public CategoryPercentage(String category, Double percentage, Double sum) {
        this.category = category;
        this.percentage = percentage;
        this.sum = sum;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}

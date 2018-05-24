package com.djavid.checkserver.model.entity.response;

public class Percentage {

    private String title;
    private double percentageCount;
    private double percentageSum;
    private double sum;
    private int count;


    public Percentage() { }

    public Percentage(String title, double percentageCount, double percentageSum, double sum, int count) {
        this.title = title;
        this.percentageCount = percentageCount;
        this.percentageSum = percentageSum;
        this.sum = sum;
        this.count = count;
    }


    @Override
    public String toString() {
        return "Percentage{" +
                "title='" + title + '\'' +
                ", percentageCount=" + percentageCount +
                ", percentageSum=" + percentageSum +
                ", sum=" + sum +
                ", count=" + count +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPercentageCount() {
        return percentageCount;
    }

    public void setPercentageCount(Double percentageCount) {
        this.percentageCount = percentageCount;
    }

    public Double getPercentageSum() {
        return percentageSum;
    }

    public void setPercentageSum(Double percentageSum) {
        this.percentageSum = percentageSum;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void setPercentageCount(double percentageCount) {
        this.percentageCount = percentageCount;
    }

    public void setPercentageSum(double percentageSum) {
        this.percentageSum = percentageSum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

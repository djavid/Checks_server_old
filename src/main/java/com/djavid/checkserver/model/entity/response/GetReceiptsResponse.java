package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.util.DateUtil;
import com.djavid.checkserver.util.SumInterval;

import java.util.List;

public class GetReceiptsResponse {

    private boolean hasNext;
    private List<Receipt> receipts;
    private double totalSum;

    private double totalDay;
    private double totalWeek;
    private double totalMonth;

    private double totalLastDay;
    private double totalLastWeek;
    private double totalLastMonth;


    public GetReceiptsResponse(List<Receipt> receipts, boolean hasNext) {
        this.hasNext = hasNext;
        this.receipts = receipts;
        setTotals();
    }

    private void setTotals() {
        totalSum = DateUtil.getTotal(receipts, SumInterval.TOTAL);

        totalDay = DateUtil.getTotal(receipts, SumInterval.DAY);
        totalWeek = DateUtil.getTotal(receipts, SumInterval.WEEK);
        totalMonth = DateUtil.getTotal(receipts, SumInterval.MONTH);

        totalLastDay = DateUtil.getTotal(receipts, SumInterval.LAST_DAY);
        totalLastWeek = DateUtil.getTotal(receipts, SumInterval.LAST_WEEK);
        totalLastMonth = DateUtil.getTotal(receipts, SumInterval.LAST_MONTH);
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

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public double getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(double totalDay) {
        this.totalDay = totalDay;
    }

    public double getTotalWeek() {
        return totalWeek;
    }

    public void setTotalWeek(double totalWeek) {
        this.totalWeek = totalWeek;
    }

    public double getTotalMonth() {
        return totalMonth;
    }

    public void setTotalMonth(double totalMonth) {
        this.totalMonth = totalMonth;
    }

    public double getTotalLastDay() {
        return totalLastDay;
    }

    public void setTotalLastDay(double totalLastDay) {
        this.totalLastDay = totalLastDay;
    }

    public double getTotalLastWeek() {
        return totalLastWeek;
    }

    public void setTotalLastWeek(double totalLastWeek) {
        this.totalLastWeek = totalLastWeek;
    }

    public double getTotalLastMonth() {
        return totalLastMonth;
    }

    public void setTotalLastMonth(double totalLastMonth) {
        this.totalLastMonth = totalLastMonth;
    }
}

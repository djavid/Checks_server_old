package com.djavid.checkserver.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long receiptId;
    //val buyerId: Long,
    private Long fiscalSign;
    private Long fiscalDocumentNumber;
    private Long ecashTotalSum;
    private Long taxationType;
    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();
    private Long shiftNumber;
    @Column(name = "user")
    private String shop;
    private Long receiptCode;
    private Long cashTotalSum;
    private String userInn;
    private Long nds10;
    private Long requestNumber;
    private Long nds18;
    private Long operationType;
    private String fiscalDriveNumber;
    private String dateTime;
    private Long totalSum;
    private String kktRegId;
    private String operator;

    private String retailPlaceAddress; //адрес
    //modifiers[] (items too)
    //stornoItems[]

    public Receipt() { }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId=" + receiptId +
                ", fiscalSign=" + fiscalSign +
                ", fiscalDocumentNumber=" + fiscalDocumentNumber +
                ", ecashTotalSum=" + ecashTotalSum +
                ", taxationType=" + taxationType +
                ", items=" + items +
                ", shiftNumber=" + shiftNumber +
                ", shop='" + shop + '\'' +
                ", receiptCode=" + receiptCode +
                ", cashTotalSum=" + cashTotalSum +
                ", userInn='" + userInn + '\'' +
                ", nds10=" + nds10 +
                ", requestNumber=" + requestNumber +
                ", nds18=" + nds18 +
                ", operationType=" + operationType +
                ", fiscalDriveNumber='" + fiscalDriveNumber + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", totalSum=" + totalSum +
                ", kktRegId='" + kktRegId + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getFiscalSign() {
        return fiscalSign;
    }

    public void setFiscalSign(Long fiscalSign) {
        this.fiscalSign = fiscalSign;
    }

    public Long getFiscalDocumentNumber() {
        return fiscalDocumentNumber;
    }

    public void setFiscalDocumentNumber(Long fiscalDocumentNumber) {
        this.fiscalDocumentNumber = fiscalDocumentNumber;
    }

    public Long getEcashTotalSum() {
        return ecashTotalSum;
    }

    public void setEcashTotalSum(Long ecashTotalSum) {
        this.ecashTotalSum = ecashTotalSum;
    }

    public Long getTaxationType() {
        return taxationType;
    }

    public void setTaxationType(Long taxationType) {
        this.taxationType = taxationType;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Long getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Long shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(Long receiptCode) {
        this.receiptCode = receiptCode;
    }

    public Long getCashTotalSum() {
        return cashTotalSum;
    }

    public void setCashTotalSum(Long cashTotalSum) {
        this.cashTotalSum = cashTotalSum;
    }

    public String getUserInn() {
        return userInn;
    }

    public void setUserInn(String userInn) {
        this.userInn = userInn;
    }

    public Long getNds10() {
        return nds10;
    }

    public void setNds10(Long nds10) {
        this.nds10 = nds10;
    }

    public Long getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Long requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Long getNds18() {
        return nds18;
    }

    public void setNds18(Long nds18) {
        this.nds18 = nds18;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }

    public String getFiscalDriveNumber() {
        return fiscalDriveNumber;
    }

    public void setFiscalDriveNumber(String fiscalDriveNumber) {
        this.fiscalDriveNumber = fiscalDriveNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Long totalSum) {
        this.totalSum = totalSum;
    }

    public String getKktRegId() {
        return kktRegId;
    }

    public void setKktRegId(String kktRegId) {
        this.kktRegId = kktRegId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRetailPlaceAddress() {
        return retailPlaceAddress;
    }

    public void setRetailPlaceAddress(String retailPlaceAddress) {
        this.retailPlaceAddress = retailPlaceAddress;
    }
}

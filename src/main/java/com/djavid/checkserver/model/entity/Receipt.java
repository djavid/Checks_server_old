package com.djavid.checkserver.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //val buyerId: Long,
    private Long fiscalSign;
    private Long fiscalDocumentNumber;
    private Long ecashTotalSum;
    private Long taxationType;
    //@OneToMany(targetEntity = Item.class, fetch = FetchType.EAGER) //TODO hz cho tut
    private List<Item> items;
    private Long shiftNumber;
    private String user;
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


    public Receipt() { }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Long shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
}

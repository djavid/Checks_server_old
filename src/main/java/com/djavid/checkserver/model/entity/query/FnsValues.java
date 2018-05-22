package com.djavid.checkserver.model.entity.query;

public class FnsValues {

    public String date;
    public String sum;
    public String fiscalDriveNumber;
    public String fiscalDocumentNumber;
    public String fiscalSign;


    public FnsValues(String date, String sum, String fiscalDriveNumber, String fiscalDocumentNumber, String fiscalSign) {
        this.date = date;
        this.sum = sum;
        this.fiscalDriveNumber = fiscalDriveNumber;
        this.fiscalDocumentNumber = fiscalDocumentNumber;
        this.fiscalSign = fiscalSign;
    }


    @Override
    public String toString() {
        return "FnsValues{" +
                "date='" + date + '\'' +
                ", sum='" + sum + '\'' +
                ", fiscalDriveNumber='" + fiscalDriveNumber + '\'' +
                ", fiscalDocumentNumber='" + fiscalDocumentNumber + '\'' +
                ", fiscalSign='" + fiscalSign + '\'' +
                '}';
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getFiscalDriveNumber() {
        return fiscalDriveNumber;
    }

    public void setFiscalDriveNumber(String fiscalDriveNumber) {
        this.fiscalDriveNumber = fiscalDriveNumber;
    }

    public String getFiscalDocumentNumber() {
        return fiscalDocumentNumber;
    }

    public void setFiscalDocumentNumber(String fiscalDocumentNumber) {
        this.fiscalDocumentNumber = fiscalDocumentNumber;
    }

    public String getFiscalSign() {
        return fiscalSign;
    }

    public void setFiscalSign(String fiscalSign) {
        this.fiscalSign = fiscalSign;
    }
}

package com.djavid.checkserver.model.entity.response;

import com.djavid.checkserver.model.entity.Receipt;

public class CheckResponseFns {

    private Document document;


    public CheckResponseFns(Document document) {
        this.document = document;
    }


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }


    public class Document {
        private Receipt receipt;

        public Document(Receipt receipt) {
            this.receipt = receipt;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }
    }
}

package com.example.bloodwallet.retrofit;

public class PayerRequest {
    private String senderRawTX;

    public PayerRequest(String senderRawTX) {
        this.senderRawTX = senderRawTX;
    }

    public String getSenderRawTX() {
        return senderRawTX;
    }

    public void setSenderRawTX(String senderRawTX) {
        this.senderRawTX = senderRawTX;
    }
}


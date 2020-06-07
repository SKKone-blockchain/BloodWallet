package com.example.bloodwallet.retrofit;

public class PayerResponse {
    private String txhash;
    private String error;

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "PayerResponse{" +
                "txhash='" + txhash + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}

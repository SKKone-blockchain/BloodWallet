package com.example.bloodwallet.task;


import android.os.AsyncTask;
import android.util.Log;

import com.example.bloodwallet.CaverFactory;
import com.example.bloodwallet.retrofit.PayerRequest;
import com.example.bloodwallet.retrofit.PayerResponse;
import com.example.bloodwallet.retrofit.PayerService;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.tx.type.TxTypeFeeDelegatedSmartContractExecution;

import java.io.IOException;
import java.math.BigInteger;

import retrofit2.Response;

import static com.example.bloodwallet.Constants.GAS_LIMIT;
import static com.example.bloodwallet.Constants.BLOCK_PARAM;
import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.GAS_LIMIT;
import static com.example.bloodwallet.Constants.GAS_PRICE;

public class PayerTask extends AsyncTask<Object, Void, PayerResponse> {
    private static final String TAG = PayerTask.class.getSimpleName();

    private WithProgressView activity;
    private KlayCredentials sender;
    private SimpleCallback<PayerResponse> callback;

    public PayerTask(WithProgressView activity, KlayCredentials sender, SimpleCallback<PayerResponse> callback) {
        this.activity = activity;
        this.sender = sender;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProgress();
    }

    @Override
    protected PayerResponse doInBackground(Object... params) {
            /*
                params[0] -> PayerService
                params[1] -> byte[]
                params[2] -> String (contract address)
             */
        try {
            // Retrieve next nonce (blocking)
            BigInteger nonce = CaverFactory.get().klay().getTransactionCount(
                    sender.getAddress(),
                    BLOCK_PARAM
            ).send().getValue();

            // Create transaction
            TxTypeFeeDelegatedSmartContractExecution tx =
                    TxTypeFeeDelegatedSmartContractExecution.createTransaction(
                            nonce,
                            GAS_PRICE,
                            GAS_LIMIT,
                            ((String) params[2]),
                            BigInteger.ZERO,
                            sender.getAddress(),
                            ((byte[]) params[1]) // data
                    );

            // Sender signs this transaction
            String senderRawTX = tx.sign(sender, CHAIN_ID).getValueAsString();
            Log.i(TAG, "sending raw -> " + senderRawTX);

            // Send signed raw transaction to payer
            Response<PayerResponse> resp =
                    ((PayerService) params[0]).sendRawTX(new PayerRequest(senderRawTX)).execute();

            return resp.isSuccessful() ? resp.body() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(PayerResponse resp) {
        super.onPostExecute(resp);
        activity.hideProgress();
        callback.run(resp);
    }
}

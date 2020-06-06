package com.example.bloodwallet.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * PayerService is an interface abstracting REST interface of an example payer
 */
public interface PayerService {
    @POST("payer")
    Call<PayerResponse> sendRawTX(@Body PayerRequest request);
}
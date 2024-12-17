package com.example.playconsign;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MidtransService {
    @POST("create-snap-token")
    Call<SnapResponse> createSnapToken(@Body SnapRequest request);
}

package com.onaar.retrofit1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("pytania")
    public Call<List<Pytanie>> getPytania();
}

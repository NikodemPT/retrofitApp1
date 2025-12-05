package com.onaar.retrofit1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/NikodemPT/retrofitL1/")
        .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi
                = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Pytanie>> call = jsonPlaceHolderApi.getPytania();
        call.enqueue(
                new Callback<List<Pytanie>>() {
                    @Override
                    public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                        }
                        List<Pytanie> pytania = response.body();
                        Toast.makeText(MainActivity.this,
                                pytania.get(0)getTrescPytania(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
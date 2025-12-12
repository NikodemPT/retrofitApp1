package com.onaar.retrofit1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private List<Pytanie> pytania;
    int aktualnePytanie = 0;
    int punkty = 0;
    TextView textViewPytanie;
    RadioGroup radioGroup;
    int radioButtonid[] = new int[]{
            R.id.radioButton,
            R.id.radioButton2,
            R.id.radioButton3
    };
    RadioButton radioButton_a;
    RadioButton radioButton_b;
    RadioButton radioButton_c;
    Button buttonDalej;
    Button buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/NikodemPT/retrofitL1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        textViewPytanie = findViewById(R.id.textViewTrescPytania);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_a = findViewById(R.id.radioButton);
        radioButton_b = findViewById(R.id.radioButton2);
        radioButton_c = findViewById(R.id.radioButton3);
        buttonDalej = findViewById(R.id.buttonDalej);
        buttonShare = findViewById(R.id.buttonShare);
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Pytanie>> call =jsonPlaceHolderApi.getPytania();
        call.enqueue(
                new Callback<List<Pytanie>>() {
                    @Override
                    public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(MainActivity.this,
                                    ""+response.code(),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pytania = response.body();
                        Toast.makeText(MainActivity.this,
                                pytania.get(0).getTrescPytania()
                                , Toast.LENGTH_SHORT).show();
                        if (pytania.size() > 0) {
                            wyswietlPytanie(aktualnePytanie);
                        }

                    }
                    @Override
                    public void onFailure(Call<List<Pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,
                                        ""+t.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
        );

        buttonDalej.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (aktualnePytanie < pytania.size()-1){
                            if (sprawdzOdpowiedz(aktualnePytanie)){
                                Toast.makeText(MainActivity.this, "Dobrze", Toast.LENGTH_SHORT).show();
                                punkty++;
                            }else{
                                Toast.makeText(MainActivity.this, "Źle", Toast.LENGTH_SHORT).show();
                            }
                            aktualnePytanie++;
                            wyswietlPytanie(aktualnePytanie);
                        }else {
                            if (sprawdzOdpowiedz(aktualnePytanie)){
                                Toast.makeText(MainActivity.this, "Dobrze", Toast.LENGTH_SHORT).show();
                                punkty++;
                            }else{
                                Toast.makeText(MainActivity.this, "Źle", Toast.LENGTH_SHORT).show();
                            }
                            // koniec testu
                            // podliczenie punktow
                            // znika wszystko
                            // wysylamy wynik sms
                            radioGroup.setVisibility(view.INVISIBLE);
                            textViewPytanie.setText("Koniec testu, punkty: " + punkty);
                            buttonShare.setVisibility(View.VISIBLE);
                            buttonDalej.setVisibility(View.INVISIBLE);
                        }

                    }
                }
        );
        buttonShare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Intent intentWysjij = new Intent();
                        intentWysjij.setAction(Intent.ACTION_SEND);
                        intentWysjij.putExtra(Intent.EXTRA_TEXT, "Otrzymano puntków "+punkty);
                        intentWysjij.setType("text/plain");
                        Intent intentUdostepniono = Intent.createChooser(intentWysjij, null);
                        startActivity(intentUdostepniono);
                    }
                });
    }
    private boolean sprawdzOdpowiedz(int aktualnePytanie){
        Pytanie pytanie = pytania.get(aktualnePytanie);
        if(radioGroup.getCheckedRadioButtonId() == radioButtonid[pytanie.getPoprawna()-1]){
            return true;
        }else{
            return false;
        }
    }
    private void wyswietlPytanie(int ktore){
        Pytanie pytanie = pytania.get(ktore);
        textViewPytanie.setText(pytanie.getTrescPytania().toString());
        radioButton_a.setText(pytanie.getOdpa());
        radioButton_b.setText(pytanie.getOdpb());
        radioButton_c.setText(pytanie.getOdpc());
        radioButton_a.setChecked(false);
        radioButton_b.setChecked(false);
        radioButton_c.setChecked(false);
    }
}
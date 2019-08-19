package com.example.rentalcar.MainPathReservation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rentalcar.MainPathReservation.MainActivity;
import com.example.rentalcar.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //chiamiamo un nuovo Handler e il metodo Runnable che ha come conseguenza il fatto che l'oggetto Runnable che gli passiamo
        //viene runnato dopo che è passata una certa quantità di tempo.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //non fa niente ci serve solo per aspettare
            }
        }, 2000);
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}

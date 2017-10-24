package com.example.jamilissokolowicz.tp8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActividadSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.actividad_splash);

        android.os.Handler hadler = new android.os.Handler();
        hadler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent i = new Intent(ActividadSplash.this, ActividadJuego.class);
                Bundle misDatos = new Bundle();
                misDatos.putString("ir","principal");
                misDatos.putString("anterior","splash");
                i.putExtras(misDatos);
                startActivity(i);
            }
        }, 3000);
    }
}
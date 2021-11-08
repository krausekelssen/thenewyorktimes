package com.example.thenewyorktimes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
/**
* Este es el MainActivity, es la actividad principal
* Cuando el proyecto arranca empieza aquí
* El nav_bar.xml ayuda a redireccionar mediante el activity_main.xml
* al home_fragment.xml
* */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
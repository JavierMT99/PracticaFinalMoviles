package com.cdaliaga.socios;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics analytics;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        analytics = FirebaseAnalytics.getInstance(this);

        //Database firebase
        database = FirebaseDatabase.getInstance();

        Bundle bundle = new Bundle();
        bundle.putString("Socios","Aplicacion Iniciada");
        analytics.logEvent("Socios", bundle);
    }
}
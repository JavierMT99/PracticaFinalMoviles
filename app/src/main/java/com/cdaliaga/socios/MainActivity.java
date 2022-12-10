package com.cdaliaga.socios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.cdaliaga.socios.adapters.SocioListAdapter;
import com.cdaliaga.socios.databinding.ActivityMainBinding;
import com.cdaliaga.socios.models.Socio;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics analytics;
    private FirebaseDatabase database;

    static ArrayList<Socio> socios;
    private SocioListAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Creamos la lista de socios
        socios = new ArrayList<Socio>();

        //Creamos el adaptador de la lista
        adaptador = new SocioListAdapter(socios, this);

        // Obtain the FirebaseAnalytics instance.
        analytics = FirebaseAnalytics.getInstance(this);

        //Database firebase
        database = FirebaseDatabase.getInstance();

        Bundle bundle = new Bundle();
        bundle.putString("Socios","Aplicacion Iniciada");
        analytics.logEvent("Socios", bundle);

        //Coger datos de firebase
        DatabaseReference myRef = database.getReference("socios");


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // A new comment has been added, add it to the displayed list
                System.out.println("Socio numero " + previousChildName);

                Socio socio = new Socio();
                socio.setNombre(dataSnapshot.child("nombre").getValue().toString());
                //socio.setNum(Integer.parseInt(dataSnapshot.child("num").toString()));
                //socio.setPagado((boolean)dataSnapshot.child("pagado").getValue());
                socio.setNum(1);
                socio.setPagado(true);

                socios.add(socio);
                System.out.println(String.valueOf(socio.getNum()));

                //Asignamos el adaptador al listView
                binding.lvLista.setAdapter(adaptador);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRef.addChildEventListener(childEventListener);

//        socios.add(new Socio(1, "prueba", true));
//        socios.add(new Socio(1, "prueba", true));
//        socios.add(new Socio(1, "prueba", true));
    }
}
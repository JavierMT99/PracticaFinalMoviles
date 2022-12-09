package com.cdaliaga.socios;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cdaliaga.socios.models.Socio;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        //Coger datos del csv y subirlos a firebase
//        File file = new File(getFilesDir() + File.separator + "socios.csv");
//
//        socios = leerCSV();

        DatabaseReference myRef = database.getReference();

        Socio prueba = new Socio(1, "Prueba", true);

        myRef = myRef.child("socios");
        myRef.setValue(prueba);
        System.out.println("User subido");

//        for (int i = 0; i < socios.size(); i++) {
//
//        }

    }

    public ArrayList<Socio> leerCSV(){

        BufferedReader br = null;
        String line = "";
        String separador = ",";

        ArrayList<Socio> socios = new ArrayList<Socio>();

        try {
            br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "/socios.csv")));

            //saltamos la primera linea
            br.readLine();

            while ((line = br.readLine()) != null) {


                String[] datos = line.split(separador);
                ArrayList<String> datos1 = new ArrayList<String>();

                for (int i = 0; i < datos.length; i++) {
                    if(!datos[i].isEmpty()){
                        datos1.add(datos[i]);
                    }
                }

                //Guarda datos.

                Socio socio = new Socio();

                socio.setNum(Integer.parseInt(datos1.get(0)));
                socio.setNombre(datos1.get(1));

                if(datos1.get(2).equals("Pagado") ){
                    socio.setPagado(true);
                }else{
                    socio.setPagado(false);
                }

                socios.add(socio);
            }

            return socios;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
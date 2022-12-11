package com.cdaliaga.socios;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics analytics;
    private FirebaseDatabase database;

    static ArrayList<Socio> socios;
    private SocioListAdapter adaptador;

    ActivityMainBinding binding;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode == 10)
        {
            SubirSocio(data.getExtras().getParcelable("socio"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        binding.lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AbrirEditarSocio(adaptador.getItem(i));
            }
        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                //Creamos un nuevo objeto socio con todos los datos guardados en FireBase
                //(Esto se ejecuta cada vez que se añada una entrada nueva en la entrada de la base de
                // datos "socios" y cuando se inicie la aplicación)

                Socio socio = new Socio();
                socio.setNombre(dataSnapshot.child("nombre").getValue().toString());
                String numero = dataSnapshot.child("num").getValue().toString();
                socio.setNum(Integer.parseInt(numero));
                socio.setPagado((boolean)dataSnapshot.child("pagado").getValue());

                //El socio nuevo lo añadimos a la lista
                socios.add(socio);
                System.out.println(String.valueOf(socio.getNum()));

                //Asignamos el adaptador al listView
                binding.lvLista.setAdapter(adaptador);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Actualizamos la información en la lista del socio que ha cambiado en la base de datos
                socios.get(Integer.parseInt(snapshot.child("num").getValue().toString()) - 1).setNombre(snapshot.child("nombre").getValue().toString());
                socios.get(Integer.parseInt(snapshot.child("num").getValue().toString()) - 1).setPagado((boolean)snapshot.child("pagado").getValue());

                //Asignamos el adapartador para actualizar la listView
                binding.lvLista.setAdapter(adaptador);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                //Si se elimina un socio en la base de datos, eliminamos el objeto de la lista de la app
                int num = Integer.parseInt(snapshot.child("num").getValue().toString());
                for (int i = 0; i < socios.size(); i++) {
                    if(socios.get(i).getNum() == num){
                        socios.remove(i);
                    }
                }

                //Asignamos el adapartador para actualizar la listView
                binding.lvLista.setAdapter(adaptador);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MostrarToastError();
            }
        };

        myRef.addChildEventListener(childEventListener);

        binding.botonAnadir.setOnClickListener(v -> {
            AbrirNuevoSocio();
        });
    }

    public ArrayList<Socio> BuscarSocioNumero(ArrayList<Socio> sociosFiltrados) {

        ArrayList<Socio> resultados = new ArrayList<Socio>();

        if(!binding.etNumero.getText().toString().isEmpty()){
            for (int i = 0; i < sociosFiltrados.size(); i++) {
                if(sociosFiltrados.get(i).getNum() == Integer.parseInt(binding.etNumero.getText().toString())){
                    resultados.add(sociosFiltrados.get(i));
                }
            }

            return resultados;
        }

        return sociosFiltrados;
    }

    public ArrayList<Socio> BuscarSocioNombre (ArrayList<Socio> sociosFiltrados){

        ArrayList<Socio> resultados = new ArrayList<Socio>();

        //Quitamos tildes del filtro
        String busqueda = binding.eTextNombre.getText().toString();

        //Si hay filtro por texto buscamos coincidencias
        if(!busqueda.isEmpty()){

            busqueda = Normalizer.normalize(busqueda, Normalizer.Form.NFD);
            busqueda = busqueda.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

            for (int i = 0; i < sociosFiltrados.size(); i++) {

                //Quitar tildes del nombre
                String nombre = sociosFiltrados.get(i).getNombre();
                nombre = Normalizer.normalize(nombre, Normalizer.Form.NFD);
                nombre = nombre.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                if(nombre.toLowerCase().contains(busqueda.toLowerCase())){
                    resultados.add(sociosFiltrados.get(i));
                }
            }

            return resultados;
        }

        //Sino hay filtro por texto devolvemos el array que nos llega igual
        return sociosFiltrados;
    }

    public ArrayList<Socio> BuscarSocioPagado (ArrayList<Socio> sociosFiltrados){

        ArrayList<Socio> resultados = new ArrayList<Socio>();

        if(binding.tbPagado.isChecked()){
            for (int i = 0; i < sociosFiltrados.size(); i++) {
                if(sociosFiltrados.get(i).isPagado() == true){
                    resultados.add(sociosFiltrados.get(i));
                }
            }
            return resultados;
        }else{
            for (int i = 0; i < sociosFiltrados.size(); i++) {
                if(sociosFiltrados.get(i).isPagado() == false){
                    resultados.add(sociosFiltrados.get(i));
                }
            }
            return resultados;
        }
    }

    public void FiltrarSocios(View view){
        ArrayList<Socio> sociosFiltrados = BuscarSocioPagado(BuscarSocioNombre(BuscarSocioNumero(socios)));
        SocioListAdapter adaptador = new SocioListAdapter(sociosFiltrados, this);
        binding.lvLista.setAdapter(adaptador);
    }

    private void MostrarToastError(){
        Toast.makeText(this, getString(R.string.error_conexion), Toast.LENGTH_LONG).show();
    }

    private void AbrirNuevoSocio() {
        Intent nuevoSocioIntent = new Intent(this, AddSocioActivity.class);
        startActivityForResult(nuevoSocioIntent, 10);
    }

    private void AbrirEditarSocio(Socio socio){
        Intent editarSocioIntent = new Intent(this, AddSocioActivity.class);
        editarSocioIntent.putExtra("socio", socio);
        startActivity(editarSocioIntent);
    }

    public void SubirSocio(Socio socio){
        //Funcion para subir el socio en la base de datos en una nueva entrada
        if(socio != null){
            try{

                //Miramos los socios existentes en el momento previo a la subida
                socio.setNum(socios.size() + 1);

                //Creamos una nueva entrada en la referencia "socios" con el nuevo socio
                DatabaseReference ref = database.getReference("socios");
                ref.child(String.valueOf(socio.getNum())).setValue(socio);

                //Damos feedback al usuario
                Toast.makeText(this, String.valueOf(socio.getNum()), Toast.LENGTH_LONG).show();
            }catch (Exception e){

                //Damos feedback de que no se ha podido realizar la subida
                Toast.makeText(this, getString(R.string.add_new_socio_error), Toast.LENGTH_LONG).show();
            }
        }
    }
}
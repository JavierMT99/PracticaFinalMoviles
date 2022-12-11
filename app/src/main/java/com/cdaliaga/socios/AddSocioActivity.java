package com.cdaliaga.socios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.cdaliaga.socios.databinding.ActivityAddSocioBinding;
import com.cdaliaga.socios.models.Socio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class AddSocioActivity extends AppCompatActivity {

    ActivityAddSocioBinding binding;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSocioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Database firebase
        database = FirebaseDatabase.getInstance();

        //Coger datos de firebase
        DatabaseReference myRef = database.getReference("socios");

        if(getIntent().hasExtra("socio")){

            Socio socio = (getIntent().getExtras().getParcelable("socio"));

            binding.edNombre.setText(socio.getNombre());
            binding.cbPagado.setChecked(socio.isPagado());
        }

        binding.btnAceptar.setOnClickListener(v -> {
            VolverMain();
        });
    }

    private void VolverMain(){
        //Si el campo nombre esta vacío no podemos dejar que se cree el nuevo socio
        if(binding.edNombre.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.no_name), Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(!getIntent().hasExtra("socio")){
                //Si el campo nombre esta lleno entonces creamos un socio.
                Socio socio = new Socio();

                socio.setNombre(binding.edNombre.getText().toString());
                socio.setPagado(binding.cbPagado.isChecked());
                socio.setNum(0);

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("socio", socio);
                setResult(10, intent);
                finish();
            }else{
                //Editamos el socio
                Socio socio = getIntent().getExtras().getParcelable("socio");

                socio.setNombre(binding.edNombre.getText().toString());
                socio.setPagado(binding.cbPagado.isChecked());

                EditarSocio(socio);
                finish();
            }

        }
    }

    private void EditarSocio(Socio socio){
        try{
            //Modificamos la entrada del socio en la base de datos
            DatabaseReference ref = database.getReference("socios");
            ref.child(String.valueOf(socio.getNum())).setValue(socio);

            //Damos feedback al usuario
            Toast.makeText(this, getString(R.string.socio_modify_success), Toast.LENGTH_LONG).show();
        }catch (Exception e){

            //Damos feedback de que no se ha podido realizar la subida
            Toast.makeText(this, getString(R.string.socio_modify_error), Toast.LENGTH_LONG).show();
        }
    }
}
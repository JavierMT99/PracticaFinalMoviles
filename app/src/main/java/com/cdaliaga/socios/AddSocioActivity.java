package com.cdaliaga.socios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.cdaliaga.socios.databinding.ActivityAddSocioBinding;
import com.cdaliaga.socios.models.Socio;

import java.io.Serializable;

public class AddSocioActivity extends AppCompatActivity {

    ActivityAddSocioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSocioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

            //Si el campo nombre esta lleno entonces creamos un socio.
            Socio socio = new Socio();

            socio.setNombre(binding.edNombre.getText().toString());
            socio.setPagado(binding.cbPagado.isChecked());
            socio.setNum(0);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("socio", socio);
            setResult(10, intent);
            finish();
        }
    }
}
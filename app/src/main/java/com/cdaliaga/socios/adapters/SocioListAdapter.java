package com.cdaliaga.socios.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cdaliaga.socios.R;
import com.cdaliaga.socios.models.Socio;

import java.util.ArrayList;

public class SocioListAdapter extends BaseAdapter {

        //Creamos algunos atributos necesarios para esta clase
        private ArrayList<Socio> listaSocios;
        private Context context;

        //Generamos el constructor
        public SocioListAdapter(ArrayList<Socio> listaSocios, Context context) {
            this.listaSocios = listaSocios;
            this.context = context;
        }

        //Este metodole indica a la ListView cuantos elementos va a tener
        @Override
        public int getCount() {
            return listaSocios.size();
        }
        //Este metodo nos va a devolver la posicion
        @Override
        public Socio getItem(int position) {
            return listaSocios.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        // Aqui se va a crear cada item y se le asignan los valores de cada elemento a cada item
        // Este metodo se va a ejecutar tantas veces como elementos tenga la lista
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Creamos el elemento socio
            Socio socio = getItem(position);

            convertView = LayoutInflater.from(context).inflate(R.layout.socio_list_item, null);

            // Cogemos los componentes del layout del item
            TextView tvNumero = convertView.findViewById(R.id.tvNumero);
            TextView tvNombre = convertView.findViewById(R.id.tvNombre);
            CheckBox cbPagado = convertView.findViewById(R.id.tbPagado);

            // Rellenamos cada item de socio
            tvNumero.setText(String.valueOf(socio.getNum()));
            tvNombre.setText(socio.getNombre());

            if(socio.isPagado() == true){
                cbPagado.setChecked(true);
            }else{
                cbPagado.setChecked(false);
            }

            return convertView;
        }
}


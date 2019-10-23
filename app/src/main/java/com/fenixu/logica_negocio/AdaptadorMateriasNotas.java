package com.fenixu.logica_negocio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fenixu.R;
import com.fenixu.gui.CrearNotasMaterias;
import com.fenixu.gui.Notas;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorMateriasNotas extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    List<List<String>> itemMateria = new ArrayList<List<String>>();

    public AdaptadorMateriasNotas(Context contexto, List itemMateria){
        this.contexto = contexto;
        this.itemMateria = itemMateria;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return itemMateria.get(0).size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent){

        final View vista = inflater.inflate(R.layout.elemento_lista,null);

        TextView titulo = (TextView) vista.findViewById(R.id.nombreMateria);
        TextView porcentaje = (TextView) vista.findViewById(R.id.itemPorcentajeEvaluado);
        TextView notaActual = (TextView) vista.findViewById(R.id.itemNotaActual);
        TextView notaNecesaria = (TextView) vista.findViewById(R.id.itemNotaNecesaria);
        TextView creditos = (TextView) vista.findViewById(R.id.itemNumeroCreditos);
        ImageButton btnElminarMateria = (ImageButton) vista.findViewById(R.id.btnEliminarMaterias);
        ImageButton btnAgregarNota = (ImageButton) vista.findViewById(R.id.btnAgregarNotas);

        titulo.setText(itemMateria.get(0).get(i));
        porcentaje.setText(itemMateria.get(1).get(i));
        notaActual.setText(itemMateria.get(2).get(i));
        notaNecesaria.setText(itemMateria.get(3).get(i));
        creditos.setText(itemMateria.get(4).get(i));

        //btnElminarMateria.setTag(i);
        btnAgregarNota.setTag(i);

        btnAgregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notas = new Intent(contexto, CrearNotasMaterias.class);
                notas.putExtra("posicion", (Integer) v.getTag());
                contexto.startActivity(notas);
            }
        });

        btnElminarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notas = new Intent(contexto, Notas.class);
                notas.putExtra("posicion", (Integer) v.getTag());
                contexto.startActivity(notas);
            }
        });

        return vista;
    }

}

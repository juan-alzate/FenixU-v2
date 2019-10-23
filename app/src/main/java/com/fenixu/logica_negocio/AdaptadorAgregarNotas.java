package com.fenixu.logica_negocio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fenixu.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorAgregarNotas extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    List<List<String>> itemNotas = new ArrayList<List<String>>();

    public AdaptadorAgregarNotas(Context contexto, List itemNotas) {
        this.contexto = contexto;
        this.itemNotas = itemNotas;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemNotas.get(0).size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.elemento_lista_nota,null);
        TextView nota = (TextView) vista.findViewById(R.id.listaNota);
        TextView porcentaje = (TextView) vista.findViewById(R.id.listaPorcentaje);
        ImageButton btnElminarNota = (ImageButton) vista.findViewById(R.id.btnEliminarNotas);

        nota.setText(itemNotas.get(1).get(i));
        porcentaje.setText(itemNotas.get(2).get(i));

        btnElminarNota.setTag(i);

        return vista;
    }
}

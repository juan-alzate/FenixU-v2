package com.fenixu.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorAgregarNotas;
import com.fenixu.logica_negocio.AdaptadorMateriasNotas;
import com.fenixu.logica_negocio.DialogoAgregarNota;
import com.fenixu.logica_negocio.MateriasNotas;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CrearNotasMaterias extends AppCompatActivity implements DialogoAgregarNota.DialogoCrearNotasListener {

    private Toolbar toolbar;
    private ListView lista;

    Bundle b;

    int cont;
    int posicionfk;
    int limite;
    int limiteFk;

    List<List<String>> itemNotas = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_notas_materias);

        toolbar = (Toolbar)findViewById(R.id.toolbarAgregarNotas);
        setSupportActionBar(toolbar);

        lista = (ListView)findViewById(R.id.listViewAgregarNotas);

        //creamos la cantidad de sublistas necesarios segun las variables.
        if(itemNotas.size()==0){
            for(int i = 0; i < 6; i++) {
                itemNotas.add(new ArrayList<String>());
            }}

        Intent intent = getIntent();
        b = intent.getExtras();

        if(b!=null){
            posicionfk = b.getInt("posicion");
        }

        //Abrimos la base de datos para agregar todos los datos de la db a la lista multidimensional.
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor limiteFkDb = db.rawQuery("select count(idNota) from notas where idMateria!="+posicionfk, null);

        if(limiteFkDb.moveToFirst()){
            limiteFk = limiteFkDb.getInt(0);
        }

        Cursor limiteFor = db.rawQuery("select count(idNota) from notas", null);

        if(limiteFor.moveToFirst()){
            limite = limiteFor.getInt(0);
            cont=limite;
            for(int i = 0; i < limite; i++) {
                Cursor nota = db.rawQuery("select * from notas where idMateria="+posicionfk+" and idNota="+i , null);
                if(nota.moveToFirst()) {
                    String temp_idNota = nota.getString(0);
                    String temp_nota = nota.getString(1);
                    String temp_porcentaje = nota.getString(2);
                    String temp_idMateria = nota.getString(3);
                    String temp_notaActual = nota.getString(4);
                    String temp_notaNecesaria = nota.getString(5);

                    itemNotas.get(0).add(temp_idNota);
                    itemNotas.get(1).add(temp_nota);
                    itemNotas.get(2).add(temp_porcentaje);
                    itemNotas.get(3).add(temp_idMateria);
                    itemNotas.get(4).add(temp_notaActual);
                    itemNotas.get(5).add(temp_notaNecesaria);
                }
            }
        }else{
            db.close();
        }

        db.close();

        //pintamos la lista en la activity solo si la listas es mayor a 0
        if(itemNotas.get(0).size()>0) {
            lista.setAdapter(new AdaptadorAgregarNotas(this, itemNotas));
        }

        //Evitamos que el programa se voltee horizontalmente
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //Items del menu superior(agregar, eliminar)
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //Metodo que se ejecuta al dar click a los items del menu superior
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.agregar){
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyTexts2(String nota, String porcentaje) {

        if(b!=null){
            itemNotas.get(0).add(" ");
            itemNotas.get(1).add(nota);
            itemNotas.get(2).add(porcentaje);
            itemNotas.get(3).add(String.valueOf(posicionfk));
            itemNotas.get(4).add(" ");
            itemNotas.get(5).add(" ");

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            int idNotaBD = cont;
            String notaBD = itemNotas.get(1).get(cont-limiteFk);
            String porcentajeBD = itemNotas.get(2).get(cont-limiteFk);
            String fk = itemNotas.get(3).get(cont-limiteFk);

            MateriasNotas mn = new MateriasNotas();
            //mandar a calcularNota() la ultima nota y el ultimo porcentaje
            int notaActual = mn.calcularNota(posicionfk, this, itemNotas.get(1).get(cont-limiteFk),
                    itemNotas.get(2).get(cont-limiteFk));

            ContentValues registro = new ContentValues();

            registro.put("notaActual", notaActual);
            registro.put("idNota", idNotaBD);
            registro.put("nota", notaBD);
            registro.put("porcentaje", porcentajeBD);
            registro.put("idMateria", fk);

            db.insert("notas",null,registro);

            db.close();
        }

        lista.setAdapter(new AdaptadorAgregarNotas(this, itemNotas));
        cont++;
    }

    //Metodo para abrir la ventana donde se agrega la materia
    public void openDialog(){
        DialogoAgregarNota dcm =  new DialogoAgregarNota();
        dcm.show(getSupportFragmentManager(),"Agregar nota");
    }

    //Boton para regresar del celular
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Notas.class);
        startActivity(intent);
        finish();
    }
}
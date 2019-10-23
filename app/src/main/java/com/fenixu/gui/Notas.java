package com.fenixu.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorMateriasNotas;
import com.fenixu.logica_negocio.DialogoCrearMateria;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Notas extends AppCompatActivity implements DialogoCrearMateria.DialogoCrearMateriasListener {

    private Toolbar toolbar;
    private ListView lista;

    int cont;
    int limite;
    //nombre de la materia, porcentaje evaluado, nota actual, nota necesaria, numero de creditos
    List<List<String>> itemMateria = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        //Menu superior
        toolbar = (Toolbar)findViewById(R.id.toolbarNotas);
        setSupportActionBar(toolbar);

        lista = (ListView)findViewById(R.id.listViewNotas);

        //creamos la cantidad de sublistas necesarios segun las variables.
        if(itemMateria.size()==0){
        for(int i = 0; i < 5; i++) {
            itemMateria.add(new ArrayList<String>());
        }}

        //eliminamos la materia en la base de datos al darle al boton eliminar
        //eliminarMateria();

       //Abrimos la base de datos para agregar todos los datos de la db a la lista multidimensional.
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminMaterias", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor limiteFor = db.rawQuery("select count(idMateria) from materias", null);

        if(limiteFor.moveToFirst()){
            limite = limiteFor.getInt(0);
            cont=limite;
            for(int i = 0; i < limite; i++){
                Cursor materia = db.rawQuery("select * from materias where idMateria="+i, null);
                if(materia.moveToFirst()){

                    String temp_titulo = materia.getString(1);
                    //String temp_porcentaje = materia.getString(2);
                    //String temp_actual = materia.getString(3);
                    //String temp_necesaria = materia.getString(4);
                    String temp_creditos = materia.getString(3);

                    itemMateria.get(0).add(temp_titulo);
                    itemMateria.get(1).add(" ");
                    itemMateria.get(3).add(" ");
                    itemMateria.get(4).add(temp_creditos);
                }
            }
        }
            db.close();

        AdminSQLiteOpenHelper admin2 = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
        SQLiteDatabase db2 = admin2.getWritableDatabase();

        for(int i = 0; i < limite; i++){
            Cursor mayor = db2.rawQuery("select max(idNota) from notas where idMateria="+i,null);
            if (mayor.moveToFirst()){
                int m = mayor.getInt(0);
                Cursor notaActual = db2.rawQuery("select notaActual from notas where idNota="+m,null);
                if(notaActual.moveToFirst()){
                    float n =notaActual.getFloat(0)/100;
                    //itemMateria.get(2).add(notaActual.getString(0));
                    itemMateria.get(2).add(String.valueOf(n));
                }
                else{
                    itemMateria.get(2).add("no se encontro");
                }
            }
            else{
                itemMateria.get(2).add("else2");
            }

        }
        db2.close();

       //pintamos la lista en la activity solo si la listas es mayor a 0
        if(itemMateria.get(0).size()>0) {
            lista.setAdapter(new AdaptadorMateriasNotas(this, itemMateria));
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

    //Metodo para abrir la ventana donde se agrega la materia
    public void openDialog(){
        DialogoCrearMateria dcm =  new DialogoCrearMateria();
        dcm.show(getSupportFragmentManager(),"Agregar Materia");
    }

    //metodo en el cual insertamos los datos de materia y creditos tanto a la lista como a la db.
    @Override
    public void applyTexts(String materia, String creditos){
        itemMateria.get(0).add(materia);
        itemMateria.get(1).add(" ");
        itemMateria.get(2).add(" ");
        itemMateria.get(3).add(" ");
        itemMateria.get(4).add(creditos);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminMaterias", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String tituloBD = itemMateria.get(0).get(cont);
        String creditosBD = itemMateria.get(4).get(cont);
        int idMateria = cont;

        ContentValues registro = new ContentValues();
        registro.put("titulo", tituloBD);
        registro.put("creditos", creditosBD);
        registro.put("idMateria", idMateria);

        db.insert("materias",null,registro);
        db.close();

        lista.setAdapter(new AdaptadorMateriasNotas(this, itemMateria));
        cont++;
    }


    public void eliminarMateria(){

        int posicion;
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null){
            posicion = b.getInt("posicion");
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminMaterias", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            db.execSQL("delete from materias where idMateria="+posicion);
            db.close();
        }
    }

    //Boton para regresar del celular
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

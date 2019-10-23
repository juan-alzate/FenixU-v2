package com.fenixu.logica_negocio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MateriasNotas{

    int porcentaje=0;
    int notaAcumulada=0;
    int notaActual;
    List<Float> listaNotas = new ArrayList<Float>();

    public MateriasNotas() {

    }

    public int getPorcentaje(){
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje){
        this.porcentaje = porcentaje;
    }

    public void ingresarNota(float nota, int porcentajeActual){
        if(porcentaje<=100){
            float notaActual = nota*(porcentaje/100);
            listaNotas.add(notaActual);
            porcentaje+=porcentajeActual;
        }
    }

    public int calcularNota(int fk, Context contexto, String ultimaNota, String ultimoPorcentaje){
        notaActual=0;
        notaAcumulada=0;
        porcentaje=0;

        notaAcumulada =Integer.parseInt(ultimaNota);
        porcentaje =Integer.parseInt(ultimoPorcentaje);
        notaActual = notaAcumulada * (porcentaje);

        //Abrimos la base de datos para agregar todos los datos de la db a la lista multidimensional.
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto,"adminNotas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor limiteFor = db.rawQuery("select count(idNota) from notas", null);

        if(limiteFor.moveToFirst()) {
            int limite = limiteFor.getInt(0);
            for (int i = 0; i < limite; i++) {
                Cursor calcN = db.rawQuery("select nota from notas where idMateria=" + fk + " and idNota=" + i, null);
                if(calcN.moveToFirst()){
                    notaAcumulada = calcN.getInt(0);
                }
                Cursor pN = db.rawQuery("select porcentaje from notas where idMateria=" + fk + " and idNota=" + i, null);
                if(pN.moveToFirst()){
                    porcentaje = pN.getInt(0);
                }
                notaActual += notaAcumulada*porcentaje;
            }
        }
        db.close();
        return notaActual/10;
    }

    public float notaFaltante(){
        float notaNecesaria = (3-notaAcumulada)/(100-porcentaje);
        return notaNecesaria;
    }
}

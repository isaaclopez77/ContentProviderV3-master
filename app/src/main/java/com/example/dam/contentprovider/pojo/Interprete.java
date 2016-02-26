package com.example.dam.contentprovider.pojo;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.dam.contentprovider.contratos.ContratoInterprete;

import java.io.Serializable;

public class Interprete implements Serializable{

    private long id;
    private String nombre;

    public Interprete(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Interprete() {
        this(0,"");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(ContratoInterprete.TablaInterprete._ID,this.id);
        cv.put(ContratoInterprete.TablaInterprete.NOMBRE,this.nombre);
        return cv;
    }

    public void set(Cursor c){ //A partir del cursor recuperar nombre, apellido y telefono
        this.id = c.getLong(c.getColumnIndex(ContratoInterprete.TablaInterprete._ID));
        this.nombre = c.getString(c.getColumnIndex(ContratoInterprete.TablaInterprete.NOMBRE));
    }

    @Override
    public String toString() {
        return "Interprete{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
    
}

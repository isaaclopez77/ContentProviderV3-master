package com.example.dam.contentprovider.pojo;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.dam.contentprovider.contratos.ContratoDisco;

import java.io.Serializable;

public class Disco implements Serializable {
    
    private long id, idInterprete;
    private String nombre;

    public Disco(long id, long idInterprete, String nombre) {
        this.id = id;
        this.idInterprete = idInterprete;
        this.nombre = nombre;
    }

    public Disco(){
        this(0,0,"");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdInterprete() {
        return idInterprete;
    }

    public void setIdInterprete(long idInterprete) {
        this.idInterprete = idInterprete;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(ContratoDisco.TablaDisco._ID,this.id);
        cv.put(ContratoDisco.TablaDisco.NOMBRE,this.nombre);
        cv.put(ContratoDisco.TablaDisco.IDINTERPRETE,this.idInterprete);
        return cv;
    }

    public void set(Cursor c){ //A partir del cursor recuperar nombre, apellido y telefono
        this.id = c.getLong(c.getColumnIndex(ContratoDisco.TablaDisco._ID));
        this.nombre = c.getString(c.getColumnIndex(ContratoDisco.TablaDisco.NOMBRE));
        this.idInterprete= c.getLong(c.getColumnIndex(ContratoDisco.TablaDisco.IDINTERPRETE));

    }

    @Override
    public String toString() {
        return "Disco{" +
                "id=" + id +
                ", idInterprete=" + idInterprete +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

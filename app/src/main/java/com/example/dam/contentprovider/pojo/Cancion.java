package com.example.dam.contentprovider.pojo;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.dam.contentprovider.contratos.ContratoCancion;

import java.io.Serializable;


public class Cancion implements Serializable {

    private long id, idDisco;
    private String titulo;

    public Cancion(){
        this(0,0,"");
    }

    public Cancion(long id, long idDisco, String titulo){
        this.id=id;
        this.idDisco=idDisco;
        this.titulo=titulo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdDisco() {
        return idDisco;
    }

    public void setIdDisco(long idDisco) {
        this.idDisco = idDisco;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "id=" + id +
                ", idDisco=" + idDisco +
                ", titulo='" + titulo + '\'' +
                '}';
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(ContratoCancion.TablaCancion._ID,this.id);
        cv.put(ContratoCancion.TablaCancion.TITULO,this.titulo);
        cv.put(ContratoCancion.TablaCancion.idDisco,this.idDisco);
        return cv;
    }

    public void set(Cursor c){ //A partir del cursor recuperar nombre, apellido y telefono
        this.id = c.getLong(c.getColumnIndex(ContratoCancion.TablaCancion._ID));
        this.titulo = c.getString(c.getColumnIndex(ContratoCancion.TablaCancion.TITULO));
        this.idDisco= c.getLong(c.getColumnIndex(ContratoCancion.TablaCancion.idDisco));

    }
}
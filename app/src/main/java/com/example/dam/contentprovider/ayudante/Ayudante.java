package com.example.dam.contentprovider.ayudante;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dam.contentprovider.contratos.ContratoCancion;
import com.example.dam.contentprovider.contratos.ContratoDisco;
import com.example.dam.contentprovider.contratos.ContratoInterprete;

public class Ayudante extends SQLiteOpenHelper{

    public static final String DATABASE_NAME ="canciones.sqlite";
    public static final int DATABASE_VERSION = 2;

    public Ayudante(Context context) {
        super(context, context.getExternalFilesDir(null) + "/" + DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

        String sql="drop table if exists "
                + ContratoCancion.TablaCancion.TABLA;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//Cuando se baja la aplicación y se crea por primera vez(no hay versión previa de la aplicación)

        String sql;
        sql="create table "+ ContratoCancion.TablaCancion.TABLA+ " ("+
                ContratoCancion.TablaCancion._ID+ " integer primary key autoincrement, "+
                ContratoCancion.TablaCancion.TITULO+" text, "+
                ContratoCancion.TablaCancion.idDisco+" long)";
        db.execSQL(sql);
        String sql2;
        sql2="create table "+ ContratoDisco.TablaDisco.TABLA+ " ("+
                ContratoDisco.TablaDisco._ID+ " integer primary key autoincrement, "+
                ContratoDisco.TablaDisco.NOMBRE+" text, "+
                ContratoDisco.TablaDisco.IDINTERPRETE+" long)";

        db.execSQL(sql2);

        String sql3;
        sql3="create table "+ ContratoInterprete.TablaInterprete.TABLA+ " ("+
                ContratoInterprete.TablaInterprete._ID+ " integer primary key autoincrement, "+
                ContratoInterprete.TablaInterprete.NOMBRE+" text)";

        db.execSQL(sql3);
    }
}
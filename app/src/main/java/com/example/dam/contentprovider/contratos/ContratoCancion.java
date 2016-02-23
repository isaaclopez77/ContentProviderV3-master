package com.example.dam.contentprovider.contratos;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContratoCancion {

    public ContratoCancion(){

    }

    public static abstract class TablaCancion implements BaseColumns {
        public static final String TABLA = "cancion";
        public static final String TITULO = "titulo";
        public static final String idDisco = "idDisco";

        //La autoridad es la cadena q identifica a qué contentprovider se llama
        public final static String AUTHORITY = "com.example.dam.contentprovider.provider.ProveedorClientes";
        //Definir como llego a la tabla cliente (a q tabla estoy llegando)
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLA);
        public final static String SINGLE_MIME ="vnd.android.cursor.item/vnd." + AUTHORITY + TABLA;
        public final static String MJLTIPLE_MIME ="vnd.android.cursor.dir/vnd." + AUTHORITY + TABLA;
    }
}

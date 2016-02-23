package com.example.dam.contentprovider.contratos;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContratoDisco {

    private ContratoDisco(){
    }

    public static abstract class TablaDisco implements BaseColumns {
        public static final String TABLA = "disco";
        public static final String NOMBRE = "nombre";
        public static final String IDINTERPRETE = "idInterprete";

        //La autoridad es la cadena q identifica a qué contentprovider se llama
        public final static String AUTHORITY = "com.example.dam.contentprovider.provider.ProveedorClientes";
        //Definir como llego a la tabla cliente (a q tabla estoy llegando)
        public final static Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/" + TABLA);
        public final static String SINGLE_MIME =
                "vnd.android.cursor.item/vnd." + AUTHORITY + TABLA;
        public final static String MJLTIPLE_MIME =
                "vnd.android.cursor.dir/vnd." + AUTHORITY + TABLA;
    }
}
package com.example.dam.contentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dam.contentprovider.ayudante.Ayudante;
import com.example.dam.contentprovider.contratos.ContratoCancion;
import com.example.dam.contentprovider.contratos.ContratoDisco;
import com.example.dam.contentprovider.contratos.ContratoInterprete;

public class ProveedorClientes extends ContentProvider{

    //La Uri es cm una URL. LLegamos a un mismo sitio. UriMatcher establece si es un 1 haz una cosa, si es 2 haz otra cosa.
    public static final UriMatcher convierteUri2Int;
    public static final int CANCION = 1;
    public static final int CANCION_ID = 2;

    public static final int DISCO = 1;
    public static final int DISCO_ID = 2;

    public static final int INTERPRETE = 1;
    public static final int INTERPRETE_ID = 2;

    static{
        convierteUri2Int = new UriMatcher(UriMatcher.NO_MATCH);
        convierteUri2Int.addURI(ContratoCancion.TablaCancion.AUTHORITY, ContratoCancion.TablaCancion.TABLA, CANCION);//Le damos la instrucción de qué hacer a la URI
        convierteUri2Int.addURI(ContratoCancion.TablaCancion.AUTHORITY, ContratoCancion.TablaCancion.TABLA + "/#", CANCION_ID);

        convierteUri2Int.addURI(ContratoDisco.TablaDisco.AUTHORITY, ContratoDisco.TablaDisco.TABLA, DISCO);//Le damos la instrucción de qué hacer a la URI
        convierteUri2Int.addURI(ContratoDisco.TablaDisco.AUTHORITY, ContratoDisco.TablaDisco.TABLA + "/#", DISCO_ID);

        convierteUri2Int.addURI(ContratoInterprete.TablaInterprete.AUTHORITY, ContratoInterprete.TablaInterprete.TABLA, INTERPRETE);//Le damos la instrucción de qué hacer a la URI
        convierteUri2Int.addURI(ContratoInterprete.TablaInterprete.AUTHORITY, ContratoInterprete.TablaInterprete.TABLA + "/#", INTERPRETE_ID);
    }

    private Ayudante abd;

    @Override
    public boolean onCreate() {
        abd = new Ayudante(getContext());
        return true;
    }



    @Nullable
    @Override
    public String getType(Uri uri) {//Devuelve el tipo mime que corresponde a la uri con que se ha llamado
        switch (convierteUri2Int.match(uri)){
            case CANCION:
                if(uri.toString().contains("cancion")){
                    return ContratoCancion.TablaCancion.MJLTIPLE_MIME;
                }else if(uri.toString().contains("disco")){
                    return ContratoDisco.TablaDisco.MJLTIPLE_MIME;
                }else if (uri.toString().contains("interprete")){
                    return ContratoInterprete.TablaInterprete.MJLTIPLE_MIME;
                }
            case CANCION_ID:
                if(uri.toString().contains("cancion")){
                    return ContratoCancion.TablaCancion.SINGLE_MIME;
                }else if(uri.toString().contains("disco")){
                    return ContratoDisco.TablaDisco.SINGLE_MIME;
                }else if (uri.toString().contains("interprete")){
                    return ContratoInterprete.TablaInterprete.SINGLE_MIME;
                }
            default:
                throw new IllegalArgumentException("Tipo de actividad desconocida " + uri);
        }
    }

    //METODO INSERT
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Comprobar que la uri utilizada para hacer la insercion es correcta
        if (convierteUri2Int.match(uri) != CANCION || convierteUri2Int.match(uri) != DISCO || convierteUri2Int.match(uri) != INTERPRETE) {
            throw new IllegalArgumentException("URI desconocida : " + uri);//SI no es correcta la Uri
        }

        //Si el ContentValues es nulo, crea un ContentValues
        ContentValues contentValues;
        if (values == null) {
            throw new IllegalArgumentException("content values null ");
        }
        //Validar

        // Inserción de nueva fila
        SQLiteDatabase db = abd.getWritableDatabase();//Conectamos a la base de datos en modo escritura
        long rowId=0;
        if(uri.toString().contains("cancion")){
             rowId = db.insert(ContratoCancion.TablaCancion.TABLA, null, values);
        }else if(uri.toString().contains("disco")){
             rowId = db.insert(ContratoDisco.TablaDisco.TABLA, null, values);
        }else if (uri.toString().contains("interprete")){
             rowId = db.insert(ContratoInterprete.TablaInterprete.TABLA, null, values);
        }

        if (rowId > 0) {
            //Si se ha insertado el elemento correctamente, entonces devolvemos la uri del elemento que se acaba de insertar
            Uri uri_actividad=null;
            if(uri.toString().contains("cancion")){
                uri_actividad = ContentUris.withAppendedId(ContratoCancion.TablaCancion.CONTENT_URI, rowId);
            }else if(uri.toString().contains("disco")){
                uri_actividad = ContentUris.withAppendedId(ContratoDisco.TablaDisco.CONTENT_URI, rowId);
            }else if (uri.toString().contains("interprete")){
                uri_actividad = ContentUris.withAppendedId(ContratoInterprete.TablaInterprete.CONTENT_URI, rowId);
            }

            getContext().getContentResolver().notifyChange(uri_actividad, null);
            return uri_actividad;
        }
            throw new SQLException("Error al insertar fila en : " + uri);

    }

    //METODO BORRAR
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = abd.getWritableDatabase();// Vuelve a abrir la base de datos para conectar con ella en modo escritura
        int match = convierteUri2Int.match(uri);//Obtengo la uri
        int affected;
        switch (match) {
            case CANCION: //Muchos clientes
                affected=0;
                if(uri.toString().contains("cancion")){
                    affected = db.delete(ContratoCancion.TablaCancion.TABLA,selection,selectionArgs);
                }else if(uri.toString().contains("disco")){
                    affected = db.delete(ContratoDisco.TablaDisco.TABLA,selection,selectionArgs);
                }else if (uri.toString().contains("interprete")){
                    affected = db.delete(ContratoInterprete.TablaInterprete.TABLA,selection,selectionArgs);
                }
                break;
            case CANCION_ID: //Un sólo cliente
                long idActividad = ContentUris.parseId(uri);
                affected=0;
                if(uri.toString().contains("cancion")){
                    affected = db.delete(ContratoCancion.TablaCancion.TABLA,ContratoCancion.TablaCancion._ID + "= ?" , new String [] {idActividad + ""});
                }else if(uri.toString().contains("disco")){
                    affected = db.delete(ContratoDisco.TablaDisco.TABLA,ContratoDisco.TablaDisco._ID + "= ?" , new String [] {idActividad + ""});
                }else if (uri.toString().contains("interprete")){
                    affected = db.delete(ContratoInterprete.TablaInterprete.TABLA,ContratoInterprete.TablaInterprete._ID + "= ?" , new String [] {idActividad + ""});
                }
                break;
            default:
                throw new IllegalArgumentException("Elemento actividad desconocido: " +
                        uri);
        }
        // Notificar cambio asociado a la urigetContext().getContentResolver().notifyChange(uri, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;//Devuelve el numero de filas borradas
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = abd.getWritableDatabase();
        int affected;
        switch (convierteUri2Int.match(uri)) {
            case CANCION:
                affected=0;
                if(uri.toString().contains("cancion")){
                    affected = db.update(ContratoCancion.TablaCancion.TABLA, values, selection, selectionArgs);
                }else if(uri.toString().contains("disco")){
                    affected = db.update(ContratoDisco.TablaDisco.TABLA, values, selection, selectionArgs);
                }else if (uri.toString().contains("interprete")){
                    affected = db.update(ContratoInterprete.TablaInterprete.TABLA, values, selection, selectionArgs);
                }
                break;
            case CANCION_ID:
                //Distintas formas de obtener el idActividad
                //uri.getLastPathSegment()
                //ContentUris.parseId(uri)
                //uri.getPathSegments().get(l)
                String idActividad = uri.getPathSegments().get(1);
                affected=0;
                if(uri.toString().contains("cancion")){
                    affected = db.update(ContratoCancion.TablaCancion.TABLA, values,ContratoCancion.TablaCancion._ID + "= ?" , new String [] {idActividad});
                }else if(uri.toString().contains("disco")){
                    affected = db.update(ContratoDisco.TablaDisco.TABLA, values,ContratoDisco.TablaDisco._ID + "= ?" , new String [] {idActividad});
                }else if (uri.toString().contains("interprete")){
                    affected = db.update(ContratoInterprete.TablaInterprete.TABLA, values,ContratoInterprete.TablaInterprete._ID + "= ?" , new String [] {idActividad});
                }
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Obtener base de datos
        SQLiteDatabase db = abd.getReadableDatabase();
        // Comparar Uri
        int match = convierteUri2Int.match(uri);

        Cursor c;

        switch (match) {
            case CANCION:
                // Consultando todos los registros
                c=null;
                if(uri.toString().contains("cancion")){
                    c = db.query(ContratoCancion.TablaCancion.TABLA, projection, selection, selectionArgs, null, null, sortOrder);
                }else if(uri.toString().contains("disco")){
                    c = db.query(ContratoDisco.TablaDisco.TABLA, projection, selection, selectionArgs, null, null, sortOrder);
                }else if (uri.toString().contains("interprete")){
                    c = db.query(ContratoInterprete.TablaInterprete.TABLA, projection, selection, selectionArgs, null, null, sortOrder);
                }
                Log.v("Camino", "nos hemos metido por el camino del case CLIENTE_ID");
                break;
            case CANCION_ID:
                // Consultando un solo registro basado en el Id del Uri
                c=null;
                long idActividad = ContentUris.parseId(uri);
                if(uri.toString().contains("cancion")){
                    c = db.query(ContratoCancion.TablaCancion.TABLA, projection, ContratoCancion.TablaCancion._ID + "= ? " , new String [] {idActividad + ""},null, null, sortOrder);
                }else if(uri.toString().contains("disco")){
                    c = db.query(ContratoDisco.TablaDisco.TABLA, projection, ContratoDisco.TablaDisco._ID + "= ? " , new String [] {idActividad + ""},null, null, sortOrder);
                }else if (uri.toString().contains("interprete")){
                    c = db.query(ContratoInterprete.TablaInterprete.TABLA, projection, ContratoInterprete.TablaInterprete._ID + "= ? " , new String [] {idActividad + ""},null, null, sortOrder);
                }
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }

        if(uri.toString().contains("cancion")){
            c.setNotificationUri(getContext().getContentResolver(),ContratoCancion.TablaCancion.CONTENT_URI);
        }else if(uri.toString().contains("disco")){
            c.setNotificationUri(getContext().getContentResolver(),ContratoDisco.TablaDisco.CONTENT_URI);
        }else if (uri.toString().contains("interprete")){
            c.setNotificationUri(getContext().getContentResolver(),ContratoInterprete.TablaInterprete.CONTENT_URI);
        }

        return c;
    }

}


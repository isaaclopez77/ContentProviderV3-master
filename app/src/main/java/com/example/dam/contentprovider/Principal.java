package com.example.dam.contentprovider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dam.contentprovider.contratos.ContratoCancion;
import com.example.dam.contentprovider.contratos.ContratoDisco;
import com.example.dam.contentprovider.contratos.ContratoInterprete;
import com.example.dam.contentprovider.pojo.Cancion;
import com.example.dam.contentprovider.pojo.Disco;
import com.example.dam.contentprovider.pojo.Interprete;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    private static final Uri uriC = ContratoCancion.TablaCancion.CONTENT_URI;
    private static final Uri uriD = ContratoDisco.TablaDisco.CONTENT_URI;
    private static final Uri uriI = ContratoInterprete.TablaInterprete.CONTENT_URI;
    private ListView lv;
    private List<Cancion> canciones = new ArrayList<>();
    private List<Disco> discos = new ArrayList<>();
    private List<Interprete> interpretes = new ArrayList<>();
    private Cursor cur;

    /************* MENUS ********************************************/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (id){
            case R.id.mnBorrar:
                //Obtenemos la cancion seleccionada
                Cancion c = new Cancion();
                c.set(cur);

                //La borramos
                int cant = getContentResolver().delete(uriC, ContratoCancion.TablaCancion._ID + " = ? ", new String[]{c.getId()+""});
                canciones.remove(info.position);
                Toast.makeText(this,cant+" cancion eliminada.",Toast.LENGTH_LONG).show();
                break;
            case R.id.mnEditar:

                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /************* CREATE **********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        lv = (ListView)findViewById(R.id.listView);
        init();
    }

    private void init() {
        cur = getContentResolver().query(uriC, null, null, null, null); //Cursor canciones
        if (cur.getCount()==0) {
            Log.v("estado", "insertando");
            insertarDatos();
            cur = getContentResolver().query(uriC, null, null, null, null); //lo actualizamos
        }else{
            //Canciones
            while(cur.moveToNext()) {
                Cancion c = new Cancion(cur.getLong(cur.getColumnIndex(ContratoCancion.TablaCancion._ID)),cur.getLong(cur.getColumnIndex(ContratoCancion.TablaCancion.idDisco)),cur.getString(cur.getColumnIndex(ContratoCancion.TablaCancion.TITULO)));
                canciones.add(c);
            }
        }

        Adaptador ad = new Adaptador(this,cur);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Principal.this, canciones.get(position).toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Principal.this,MostrarDiscos.class);
                i.putExtra("cancion",canciones.get(position));
                startActivity(i);
            }
        });

        registerForContextMenu(lv);
    }

    /**************** METODOS *******************************/
    public void insertarDatos(){
        //Canciones
        Cursor curc = getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        while(curc.moveToNext()) {
            Cancion c = new Cancion((long)curc.getInt(curc.getColumnIndex("_id")),(long)curc.getInt(curc.getColumnIndex("album_id")),curc.getString(curc.getColumnIndex("title")));
            canciones.add(c);
            Uri uri = getContentResolver().insert(uriC, c.getContentValues());
        }

        //Interpretes
        Cursor curi = getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"DISTINCT artist_id, artist"}, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        while(curi.moveToNext()) {
            //Interpretes
            Interprete in = new Interprete((long)curi.getInt(curi.getColumnIndex("artist_id")),curi.getString(curi.getColumnIndex("artist")));
            interpretes.add(in);
            Uri uri3 = getContentResolver().insert(uriI, in.getContentValues());
        }

        //Discos
        Cursor curd = getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"DISTINCT album_id, artist_id, album"}, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        while (curd.moveToNext()) {
            Disco d = new Disco((long)curd.getInt(curd.getColumnIndex("album_id")),(long)curd.getInt(curd.getColumnIndex("artist_id")),curd.getString(curd.getColumnIndex("album")));
            discos.add(d);
            Uri uri2 = getContentResolver().insert(uriD, d.getContentValues());
        }
    }

    public static String getEstructuraCursor(Cursor cursor){
        String registro = "";
        String[] nombres = cursor.getColumnNames();
        int contador = 0;
        for (String s: nombres){
            contador ++;
            registro += contador + " " + s + "\n";
        }
        return registro;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cur.close();
    }
}

//Borrar
//getContentResolver().delete(uri, ContratoCancion.TablaCancion._ID + " > ? ", new String[]{"29"});

//Insertar 1
//Cancion cl = new Cancion(33, 15,"prok");
//Uri uri2 = getContentResolver().insert(uriC, cl.getContentValues());

//Estructura tabla 2
//Cursor c = getContentResolver().query(uriI, null, null, null, null);//Mi cursor de mi content provider
//tv.setText(Principal.getEstructuraCursor(c));

//Construir la uri a partir del id 3 //Ver
//long id = 1;
//Uri uril = ContentUris.withAppendedId(uriI,id);
//c = getContentResolver().query(uril, null, null, null, null);

//Ver 4
/*String todo ="";

while(c.moveToNext()){
        i = new Interprete();
        i.set(c);
        todo += i.toString()+ "\n";
        }
        tv2.setText(todo);*/
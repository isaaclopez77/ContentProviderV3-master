package com.example.dam.contentprovider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dam.contentprovider.adaptadores.AdaptadorDiscos;
import com.example.dam.contentprovider.contratos.ContratoCancion;
import com.example.dam.contentprovider.contratos.ContratoDisco;
import com.example.dam.contentprovider.contratos.ContratoInterprete;
import com.example.dam.contentprovider.pojo.Disco;
import com.example.dam.contentprovider.pojo.Interprete;

import java.util.ArrayList;
import java.util.List;

public class MostrarDiscos extends AppCompatActivity {

    private ListView lv;
    private AdaptadorDiscos ad;
    private Cursor cu;
    private static final Uri uriC = ContratoCancion.TablaCancion.CONTENT_URI;
    private static final Uri uriD = ContratoDisco.TablaDisco.CONTENT_URI;
    private static final Uri uriI = ContratoInterprete.TablaInterprete.CONTENT_URI;
    private List<Disco> discos = new ArrayList<>();

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
                Disco d = new Disco();
                d.set(cu);

                int cant = getContentResolver().delete(uriD, ContratoDisco.TablaDisco._ID + " = ? ", new String[]{d.getId()+""});
                discos.remove(info.position);
                Toast.makeText(this,cant+" disco eliminado.",Toast.LENGTH_LONG).show();
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
        int id = item.getItemId();

        switch (id){
            case R.id.mnInterpretes:
                Intent iI = new Intent(this, MostrarInterpretes.class);
                startActivity(iI);
                break;
            case R.id.mnCanciones:
                Intent iC = new Intent(this, Principal.class);
                startActivity(iC);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_discos);
        lv = (ListView)findViewById(R.id.lvDiscos);

        Bundle b = getIntent().getExtras();
        if(b != null){
            Interprete seleccionado = (Interprete)b.getSerializable("interprete");

            cu = getContentResolver().query(uriD, null, ContratoDisco.TablaDisco.IDINTERPRETE + " = ?", new String[]{seleccionado.getId()+""}, null); //Cursor canciones
        }else {
            cu = getContentResolver().query(uriD, null, null, null, null);
        }
        while(cu.moveToNext()) {
            Disco d = new Disco((long)cu.getInt(cu.getColumnIndex(ContratoDisco.TablaDisco._ID)),(long)cu.getInt(cu.getColumnIndex(ContratoDisco.TablaDisco.IDINTERPRETE)),cu.getString(cu.getColumnIndex(ContratoDisco.TablaDisco.NOMBRE)));
            discos.add(d);
        }
        ad = new AdaptadorDiscos(this,cu);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Disco seleccionado = discos.get(position);
                Toast.makeText(MostrarDiscos.this, seleccionado.toString(),Toast.LENGTH_LONG).show();

                Intent i = new Intent(MostrarDiscos.this,Principal.class);
                i.putExtra("disco",seleccionado);
                startActivity(i);
            }
        });

        registerForContextMenu(lv);
    }
}
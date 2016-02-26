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
import com.example.dam.contentprovider.adaptadores.AdaptadorInterpretes;
import com.example.dam.contentprovider.contratos.ContratoCancion;
import com.example.dam.contentprovider.contratos.ContratoDisco;
import com.example.dam.contentprovider.contratos.ContratoInterprete;
import com.example.dam.contentprovider.pojo.Disco;
import com.example.dam.contentprovider.pojo.Interprete;

import java.util.ArrayList;
import java.util.List;

public class MostrarInterpretes extends AppCompatActivity {

    private ListView lv;
    private AdaptadorInterpretes ad;
    private Cursor cu;
    private static final Uri uriC = ContratoCancion.TablaCancion.CONTENT_URI;
    private static final Uri uriD = ContratoDisco.TablaDisco.CONTENT_URI;
    private static final Uri uriI = ContratoInterprete.TablaInterprete.CONTENT_URI;
    private List<Interprete> interpretes = new ArrayList<>();

    /************* MENUS ********************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.mnDiscos:
                Intent iD = new Intent(this, MostrarInterpretes.class);
                startActivity(iD);
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
        setContentView(R.layout.mostrar_interpretes);
        lv = (ListView)findViewById(R.id.lvInterpretes);
        cu = getContentResolver().query(uriI,null,null,null,null);
        while(cu.moveToNext()) {
            Interprete in = new Interprete((long)cu.getInt(cu.getColumnIndex(ContratoInterprete.TablaInterprete._ID)),cu.getString(cu.getColumnIndex(ContratoInterprete.TablaInterprete.NOMBRE)));
            interpretes.add(in);
        }
        ad = new AdaptadorInterpretes(this,cu);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Interprete seleccionado = interpretes.get(position);
                Toast.makeText(MostrarInterpretes.this, seleccionado.toString(),Toast.LENGTH_LONG).show();

                Intent i = new Intent(MostrarInterpretes.this,MostrarDiscos.class);
                i.putExtra("interprete",seleccionado);
                startActivity(i);
            }
        });
    }
}
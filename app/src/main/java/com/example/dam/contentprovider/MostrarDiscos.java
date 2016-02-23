package com.example.dam.contentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dam.contentprovider.contratos.ContratoCancion;
import com.example.dam.contentprovider.contratos.ContratoDisco;
import com.example.dam.contentprovider.contratos.ContratoInterprete;
import com.example.dam.contentprovider.pojo.Cancion;

public class MostrarDiscos extends AppCompatActivity {

    private ListView lv;
    private AdaptadorDiscos ad;
    private Cursor cu;
    private static final Uri uriC = ContratoCancion.TablaCancion.CONTENT_URI;
    private static final Uri uriD = ContratoDisco.TablaDisco.CONTENT_URI;
    private static final Uri uriI = ContratoInterprete.TablaInterprete.CONTENT_URI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = (ListView)findViewById(R.id.lvDiscos);
        Cancion c;
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            c = (Cancion)extras.getSerializable("cancion");
            Toast.makeText(this,c.toString(),Toast.LENGTH_LONG).show();
        }

        cu = getContentResolver().query(uriD,null,null,null,null);
        ad = new AdaptadorDiscos(this,cu);
        lv.setAdapter(ad);
    }
}
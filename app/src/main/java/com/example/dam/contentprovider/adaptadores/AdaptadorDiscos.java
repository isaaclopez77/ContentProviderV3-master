package com.example.dam.contentprovider.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.dam.contentprovider.R;
import com.example.dam.contentprovider.pojo.Cancion;
import com.example.dam.contentprovider.pojo.Disco;

public class AdaptadorDiscos extends CursorAdapter {

    public AdaptadorDiscos(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.item, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView)view.findViewById(R.id.tvTitulo);
        Disco d = new Disco();
        d.set(cursor);
        tv.setText(d.getNombre());
    }
}
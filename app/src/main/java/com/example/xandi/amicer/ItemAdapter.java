package com.example.xandi.amicer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.xandi.amicer.modelo.Group;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<String> elementos;

    public ItemAdapter(@NonNull Context context, ArrayList<String> elementos) {
        super(context, R.layout.item_interesse, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_interesse, parent, false);

        TextView item = (TextView) rowView.findViewById(R.id.txtInteresse);

        item.setText(elementos.get(position));

        return rowView;
    }

}

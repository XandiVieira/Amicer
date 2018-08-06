package com.example.xandi.amicer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xandi.amicer.modelo.Group;

import java.util.ArrayList;

public class InteressesAdatper extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Group> elementos;

    public InteressesAdatper(@NonNull Context context, ArrayList<Group> elementos) {
        super(context, R.layout.item_interesse, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_interesse, parent, false);

        TextView interesse = (TextView) rowView.findViewById(R.id.txtInteresse);

        interesse.setText(elementos.get(position).getInteresses().get(0));

        return super.getView(position, convertView, parent);
    }
}

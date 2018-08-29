package com.example.xandi.amicer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xandi.amicer.modelo.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Group> elementos;

    public GroupAdapter(@NonNull Context context, ArrayList<Group> elementos) {
        super(context, R.layout.item_group, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_group, parent, false);

        TextView nomeGrupo = (TextView) rowView.findViewById(R.id.nameGroup);
        TextView creatorGroup = (TextView) rowView.findViewById(R.id.creatorGroup);
        TextView descrGroup = (TextView) rowView.findViewById(R.id.descrGroup);
        TextView interesse1 = (TextView) rowView.findViewById(R.id.interesse1);
        TextView interesse2 = (TextView) rowView.findViewById(R.id.interesse2);
        TextView interesse3 = (TextView) rowView.findViewById(R.id.interesse3);
        TextView interesse4 = (TextView) rowView.findViewById(R.id.interesse4);

        nomeGrupo.setText(elementos.get(position).getNome());
        creatorGroup.setText(elementos.get(position).getCriadorGrupo());
        descrGroup.setText(elementos.get(position).getDescricao());

        if(elementos.get(position).getInteresses().size() > 0)
        interesse1.setText(elementos.get(position).getInteresses().get(0));
        if(elementos.get(position).getInteresses().size() > 1)
        interesse2.setText(elementos.get(position).getInteresses().get(1));
        if(elementos.get(position).getInteresses().size() > 2)
        interesse3.setText(elementos.get(position).getInteresses().get(2));
        if(elementos.get(position).getInteresses().size() > 3)
        interesse4.setText(elementos.get(position).getInteresses().get(3));

        return rowView;
    }
}

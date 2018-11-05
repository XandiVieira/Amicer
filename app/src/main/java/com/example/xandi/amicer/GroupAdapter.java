package com.example.xandi.amicer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.Util;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Group> elementos;
    private List<Chip> chipList = new ArrayList<Chip>();
    private List<TextView> interesses = new ArrayList<TextView>();

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
        TextView category = (TextView) rowView.findViewById(R.id.category);
        interesses.add((TextView) rowView.findViewById(R.id.interesse1));
        interesses.add((TextView) rowView.findViewById(R.id.interesse2));
        interesses.add((TextView) rowView.findViewById(R.id.interesse3));
        interesses.add((TextView) rowView.findViewById(R.id.interesse4));
        interesses.add((TextView) rowView.findViewById(R.id.interesse5));
        interesses.add((TextView) rowView.findViewById(R.id.interesse6));
        interesses.add((TextView) rowView.findViewById(R.id.interesse7));

        nomeGrupo.setText(elementos.get(position).getNome());
        creatorGroup.setText(elementos.get(position).getCriadorGrupo());
        descrGroup.setText(elementos.get(position).getDescricao());
        category.setText(elementos.get(position).getCategoria().getCategoria());

        for(int i=0; i<elementos.get(position).getCategoria().getTags().size(); i++){
            chipList.add(elementos.get(position).getCategoria().getTags().get(i));
        }

        for (int i = 0; i < chipList.size(); i++) {
            interesses.get(i).setText(chipList.get(i).getLabel());
        }
        interesses.clear();
        chipList.clear();
        return rowView;
    }
}

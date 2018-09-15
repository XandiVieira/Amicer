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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GroupAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Group> elementos;
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
        interesses.add((TextView) rowView.findViewById(R.id.interesse1));
        interesses.add((TextView) rowView.findViewById(R.id.interesse2));
        interesses.add((TextView) rowView.findViewById(R.id.interesse3));
        interesses.add((TextView) rowView.findViewById(R.id.interesse4));

        nomeGrupo.setText(elementos.get(position).getNome());
        creatorGroup.setText(elementos.get(position).getCriadorGrupo());
        descrGroup.setText(elementos.get(position).getDescricao());

        for(List<Chip> chip : elementos.get(position).getInteresses().values()){
            for (int i=0; i<chip.size(); i++){
                interesses.get(i).setText(chip.get(i).label);
            }
        }

        return rowView;
    }
}

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
import java.util.List;

public class GroupAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Group> elementos;
    private List<Chip> chipList = new ArrayList<>();
    private List<TextView> interesses = new ArrayList<>();

    GroupAdapter(@NonNull Context context, ArrayList<Group> elementos) {
        super(context, R.layout.item_group, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    /*public GroupAdapter(@NonNull Context context, ArrayList<Group> elementos) {
        super(context, R.layout.item_group, elementos);
        this.context = context;
        this.elementos = elementos;
    }*/

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_group, parent, false);

        TextView nomeGrupo = rowView.findViewById(R.id.nameGroup);
        TextView creatorGroup = rowView.findViewById(R.id.creatorGroup);
        TextView descrGroup = rowView.findViewById(R.id.descrGroup);
        TextView category = rowView.findViewById(R.id.category);
        interesses.add(rowView.findViewById(R.id.interesse1));
        interesses.add(rowView.findViewById(R.id.interesse2));
        interesses.add(rowView.findViewById(R.id.interesse3));
        interesses.add(rowView.findViewById(R.id.interesse4));
        interesses.add(rowView.findViewById(R.id.interesse5));
        interesses.add(rowView.findViewById(R.id.interesse6));
        interesses.add(rowView.findViewById(R.id.interesse7));

        nomeGrupo.setText(elementos.get(position).getNome());
        creatorGroup.setText(elementos.get(position).getCriadorGrupo());
        descrGroup.setText(elementos.get(position).getDescricao());
        category.setText(elementos.get(position).getCategoria().getCategoria());

        chipList.addAll(elementos.get(position).getCategoria().getTags());

        for (int i = 0; i < chipList.size(); i++) {
            interesses.get(i).setText(chipList.get(i).getLabel());
        }
        interesses.clear();
        chipList.clear();
        return rowView;
    }
}

package com.example.xandi.amicer;

import android.content.Context;
import android.graphics.Color;
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
    private List<Chip> chipList = new ArrayList<Chip>();
    private List<TextView> interesses = new ArrayList<TextView>();

    public GroupAdapter(@NonNull Context context, ArrayList<Group> elementos) {
        super(context, R.layout.item_group, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    /*public GroupAdapter(@NonNull Context context, ArrayList<Group> elementos) {
        super(context, R.layout.item_group, elementos);
        this.context = context;
        this.elementos = elementos;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_group, parent, false);

        TextView nomeGrupo = (TextView) rowView.findViewById(R.id.nameGroup);
        TextView creatorGroup = (TextView) rowView.findViewById(R.id.creatorGroup);
        TextView descrGroup = (TextView) rowView.findViewById(R.id.descrGroup);
        TextView category = (TextView) rowView.findViewById(R.id.category);
        TextView compatSTR = (TextView) rowView.findViewById(R.id.compat);
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

        /*if(listCompat!=null && listCompat.size()>0) {
            if (this.listCompat.get(position) >= 80) {
                compatSTR.setTextColor(Color.parseColor("#20ff5d"));
            } else if (this.listCompat.get(position) < 80 && this.listCompat.get(position) >= 60) {
                compatSTR.setTextColor(Color.parseColor("#ffcc00"));
            } else if (this.listCompat.get(position) < 60 && this.listCompat.get(position) >= 30) {
                compatSTR.setTextColor(Color.parseColor("#fe251d"));
            } else {
                compatSTR.setTextColor(Color.parseColor("#000000"));
            }

            compatSTR.setText(this.listCompat.get(position) + "%");

            if (listCompat.get(position) == 0) {
                compatSTR.setVisibility(View.GONE);
            } else {
                compatSTR.setVisibility(View.VISIBLE);
            }
        }*/
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

package com.example.xandi.amicer;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.Message;

import java.util.ArrayList;

public class InteresseAdapter extends ArrayAdapter<Interesse> {

    private Context context;
    private ArrayList<Interesse> interesses;

    public InteresseAdapter(Context c, ArrayList<Interesse> interesses) {
        super(c, 0, interesses);

        this.context = c;
        this.interesses = interesses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (interesses != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            String interesse = "#";

            for(int i=0; i<interesses.get(position).getTags().size(); i++){
                interesse = interesse + " " + interesses.get(position).getTags().get(i);
            }

            if(interesses.get(position) != null) {
                view = inflater.inflate(R.layout.item_interesse_usuario, parent, false);
            }

            if(position == 0 || position == 2 || position == 4){
                view.setBackgroundColor(Color.parseColor("#8CE7FC"));
            }else{
                view.setBackgroundColor(Color.parseColor("#52D5F2"));
            }

            TextView interesseItem = (TextView) view.findViewById(R.id.interesse);

            interesseItem.setText(interesse);
        }

        return view;
    }
}

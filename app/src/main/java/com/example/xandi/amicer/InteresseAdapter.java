package com.example.xandi.amicer;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.Message;
import com.example.xandi.amicer.modelo.Util;

import java.util.ArrayList;

public class InteresseAdapter extends ArrayAdapter<Interesse> {

    private Context context;
    private ArrayList<Interesse> interesses;
    private TextView interesse;

    public InteresseAdapter(Context c, ArrayList<Interesse> interesses) {
        super(c, 0, interesses);

        this.context = c;
        this.interesses = interesses;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = null;
         Switch switchIdade = null;
         Switch switchDistancia = null;
         SeekBar seekBarIdade = null;
         SeekBar seekBarDistancia = null;
         TextView txtDistancia = null;
         TextView txtIdade = null;
        final int[] distancia = new int[1];
        final int[] idade = new int[1];
        int posicao = 0;

        if (interesses != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            String title = interesses.get(position).getCategoria();

            if(interesses.get(position) != null) {
                view = inflater.inflate(R.layout.item_interesse_usuario, parent, false);
                interesse = view.findViewById(R.id.interesse);
                seekBarDistancia = view.findViewById(R.id.seekBarDistancia);
                seekBarIdade = view.findViewById(R.id.seekBarIdade);
                switchDistancia = view.findViewById(R.id.switchDistancia);
                switchIdade = view.findViewById(R.id.switchIdade);
                //switchAtivado = convertView.findViewById(R.id.switchAtivado);
                txtDistancia = view.findViewById(R.id.txtDistancia);
                txtIdade = view.findViewById(R.id.txtIdade);
                posicao = position;
                seekBarDistancia.setProgress(interesses.get(posicao).getDistanciaMax());
                seekBarIdade.setProgress(interesses.get(posicao).getIdade());
                if(interesses.get(posicao).getDistanciaAtivada()){
                    switchDistancia.setChecked(true);
                    txtDistancia.setText(interesses.get(posicao).getDistancia()+"km");
                }else{
                    switchDistancia.setChecked(false);
                    txtDistancia.setText("");
                }
                if(interesses.get(posicao).getIdadeAtivada()){
                    switchIdade.setChecked(true);
                    txtIdade.setText(interesses.get(posicao).getIdade()+"");
                }else{
                    switchIdade.setChecked(false);
                    txtIdade.setText("");
                }
            }

            if(position == 0 || position == 2 || position == 4){
                view.setBackgroundColor(Color.parseColor("#8CE7FC"));
            }else{
                view.setBackgroundColor(Color.parseColor("#52D5F2"));
            }

            TextView interesseItem = (TextView) view.findViewById(R.id.interesse);

            interesseItem.setText(title);
        }

        if(!switchIdade.isChecked()){
            seekBarIdade.setVisibility(View.GONE);
        }
        if(!switchDistancia.isChecked()){
            seekBarDistancia.setVisibility(View.GONE);
        }

        final SeekBar finalSeekBarIdade = seekBarIdade;
        final Switch finalSwitchIdade = switchIdade;
        final SeekBar finalSeekBarDistancia = seekBarDistancia;
        final Switch finalSwitchDistancia = switchDistancia;
        final int finalPosicao = posicao;

        final TextView finalTxtIdade = txtIdade;
        switchIdade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!finalSwitchIdade.isChecked()){
                    finalTxtIdade.setText("");
                    finalSeekBarIdade.setVisibility(View.GONE);
                    Util.mUserDatabaseRef.child(Util.getFbUser().getUid()).child("categorias").child(String.valueOf(finalPosicao)).child("idadeAtivada").setValue(false);
                }else {
                    finalTxtIdade.setText(interesses.get(finalPosicao).getIdade()+"");
                    finalSeekBarIdade.setVisibility(View.VISIBLE);
                    Util.mUserDatabaseRef.child(Util.getFbUser().getUid()).child("categorias").child(String.valueOf(finalPosicao)).child("idadeAtivada").setValue(true);
                }
            }
        });

        final TextView finalTxtDistancia = txtDistancia;
        switchDistancia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!finalSwitchDistancia.isChecked()){
                    finalTxtDistancia.setText("");
                    finalSeekBarDistancia.setVisibility(View.GONE);
                    Util.mUserDatabaseRef.child(Util.getFbUser().getUid()).child("categorias").child(String.valueOf(finalPosicao)).child("distanciaAtivada").setValue(false);
                }else {
                    finalTxtDistancia.setText(interesses.get(finalPosicao).getDistancia()+"km");
                    finalSeekBarDistancia.setVisibility(View.VISIBLE);
                    Util.mUserDatabaseRef.child(Util.getFbUser().getUid()).child("categorias").child(String.valueOf(finalPosicao)).child("distanciaAtivada").setValue(true);
                }
            }
        });

        seekBarDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                finalTxtDistancia.setText((i+2)+"km");
                distancia[0] = (i+2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Util.getDistancia().set(finalPosicao, distancia[0]);

                Util.mUserDatabaseRef.child(Util.getFbUser().getUid()).child("categorias").child(String.valueOf(finalPosicao)).child("distancia").setValue(distancia[0]);
            }
        });

        seekBarIdade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                finalTxtIdade.setText((i+""));
                idade[0] = (i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Util.getIdade().set(finalPosicao, distancia[0]);

                Util.mUserDatabaseRef.child(Util.getFbUser().getUid()).child("categorias").child(String.valueOf(finalPosicao)).child("idade").setValue(idade[0]);
            }
        });

        return view;
    }

}

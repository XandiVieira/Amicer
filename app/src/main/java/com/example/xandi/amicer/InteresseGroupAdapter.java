package com.example.xandi.amicer;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.Util;

import java.util.ArrayList;

public class InteresseGroupAdapter extends ArrayAdapter<Group> {


    private Context context;
    private ArrayList<Group> groups;
    private TextView interesse;
    private ArrayList<Interesse> interessesList;
    private int cont = 0;

    public InteresseGroupAdapter(Context c, ArrayList<Group> groups) {
        super(c, 0, groups);

        this.context = c;
        this.groups = groups;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = null;
        Switch switchIdade = null;
        Switch switchDistancia = null;
        CrystalRangeSeekbar rangeSeekbar = null;
        SeekBar seekBarDistancia = null;
        TextView txtDistancia = null;
        TextView txtIdadeMin = null;
        TextView txtIdadeMax = null;
        TextView nomeGrupo = null;
        TextView interesse = null;
        final int[] distancia = new int[1];
        final int[] idadeMin = new int[1];
        final int[] idadeMax = new int[1];
        int posicao = 0;

        interessesList = new ArrayList<Interesse>();
        for(int i=0; i<groups.size(); i++){
            interessesList.add(groups.get(i).getCategoria());
        }

        if (interessesList != null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            String title = interessesList.get(position).getCategoria();

            if(interessesList.get(position) != null) {
                view = inflater.inflate(R.layout.item_interesse_grupo, parent, false);
                interesse = view.findViewById(R.id.interesse);
                seekBarDistancia = view.findViewById(R.id.seekBarDistancia);
                rangeSeekbar = view.findViewById(R.id.rangeSeekbar);
                switchDistancia = view.findViewById(R.id.switchDistancia);
                switchIdade = view.findViewById(R.id.switchIdade);
                //switchAtivado = convertView.findViewById(R.id.switchAtivado);
                txtDistancia = view.findViewById(R.id.txtDistancia);
                txtIdadeMin = view.findViewById(R.id.txtIdadeInicio);
                txtIdadeMax = view.findViewById(R.id.txtIdadeFim);
                nomeGrupo = (TextView) view.findViewById(R.id.nomeGrupo);
                interesse = (TextView) view.findViewById(R.id.interesseNome);
                nomeGrupo.setText(groups.get(cont).getNome());
                interesse.setText(interessesList.get(posicao).getCategoria());
                posicao = position;
                seekBarDistancia.setProgress(interessesList.get(posicao).getDistanciaMax());

                if(interessesList.get(posicao).getDistanciaAtivada()){
                    switchDistancia.setChecked(true);
                    txtDistancia.setText(interessesList.get(posicao).getDistancia()+"km");
                }else{
                    switchDistancia.setChecked(false);
                    txtDistancia.setText("");
                }
                if(interessesList.get(posicao).getIdadeAtivada()){
                    switchIdade.setChecked(true);
                    txtIdadeMin.setText(interessesList.get(position).getIdade()+"");
                    txtIdadeMax.setText(interessesList.get(position).getIdadeFinal()+"");
                    rangeSeekbar.setMinStartValue(interessesList.get(position).getIdade()).apply();
                    rangeSeekbar.setMaxStartValue(interessesList.get(position).getIdadeFinal()).apply();
                }else{
                    switchIdade.setChecked(false);
                    txtIdadeMin.setText("");
                    txtIdadeMax.setText("");
                }
                cont++;
            }

            if(position == 0 || position%2==0){
                view.setBackgroundColor(Color.parseColor("#8CE7FC"));
            }else{
                view.setBackgroundColor(Color.parseColor("#52D5F2"));
            }

            TextView interesseItem = (TextView) view.findViewById(R.id.interesseNome);

            interesseItem.setText(title);
        }

        if(!switchIdade.isChecked()){
            rangeSeekbar.setVisibility(View.GONE);
        }
        if(!switchDistancia.isChecked()){
            seekBarDistancia.setVisibility(View.GONE);
        }

        //final SeekBar finalSeekBarIdade = seekBarIdade;
        final Switch finalSwitchIdade = switchIdade;
        final SeekBar finalSeekBarDistancia = seekBarDistancia;
        final Switch finalSwitchDistancia = switchDistancia;
        final int finalPosicao = posicao;
        final TextView finalTxtDistancia = txtDistancia;
        final TextView finalTxtIdadeMin = txtIdadeMin;
        final TextView finalTxtIdadeMax = txtIdadeMax;
        final CrystalRangeSeekbar finalRangeSeekbar = rangeSeekbar;
        switchIdade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!finalSwitchIdade.isChecked()){
                    finalTxtIdadeMin.setText("");
                    finalTxtIdadeMax.setText("");
                    finalRangeSeekbar.setVisibility(View.GONE);
                    Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("idadeAtivada").setValue(false);
                }else {
                    finalTxtIdadeMin.setText(interessesList.get(finalPosicao).getIdade()+"");
                    finalTxtIdadeMax.setText(interessesList.get(finalPosicao).getIdadeFinal()+"");
                    finalRangeSeekbar.setVisibility(View.VISIBLE);
                    Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("idadeAtivada").setValue(true);
                }
            }
        });


        switchDistancia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!finalSwitchDistancia.isChecked()){
                    finalTxtDistancia.setText("");
                    finalSeekBarDistancia.setVisibility(View.GONE);
                    Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("distanciaAtivada").setValue(false);
                }else {
                    finalTxtDistancia.setText(interessesList.get(finalPosicao).getDistancia()+"km");
                    finalSeekBarDistancia.setVisibility(View.VISIBLE);
                    Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("distanciaAtivada").setValue(true);
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

                Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("distancia").setValue(distancia[0]);
            }
        });

        finalRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                finalTxtIdadeMax.setText(maxValue.toString());
                finalTxtIdadeMin.setText(minValue.toString());
            }
        });

        finalRangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                idadeMin[0] = minValue.intValue();
                idadeMax[0] = maxValue.intValue();
                Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("idade").setValue(idadeMin[0]);
                Util.mDatabaseRef.child("group").child(groups.get(position).getUid()).child("categoria").child("idadeFinal").setValue(idadeMax[0]);
            }
        });


        return view;
    }
}

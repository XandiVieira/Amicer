package com.example.xandi.amicer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfigProfileFragment extends Fragment {

    private TextView interesse;
    private TextView txtDistancia;
    private TextView txtIdade;
    private SeekBar seekBarDistancia;
    private SeekBar seekBarIdade;
    private Switch switchDistancia;
    private Switch switchIdade;
    private Switch switchAtivado;
    private ListView listViewInteresses;
    private ArrayList<Interesse> listaInteresses = new ArrayList<Interesse>();
    private FirebaseUser fbUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private ArrayAdapter adapter;
    private int idade;
    private int distancia;
    private Button deleteButton;

    public ConfigProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config_profile, container, false);
        View itemView = inflater.inflate(R.layout.item_interesse_usuario, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fbUser = mFirebaseAuth.getCurrentUser();
        startFirebase();

        deleteButton = rootView.findViewById(R.id.deleteButton);
        interesse = itemView.findViewById(R.id.interesse);
        seekBarDistancia = itemView.findViewById(R.id.seekBarDistancia);
        seekBarIdade = itemView.findViewById(R.id.seekBarIdade);
        switchDistancia = itemView.findViewById(R.id.switchDistancia);
        switchIdade = itemView.findViewById(R.id.switchIdade);
        switchAtivado = itemView.findViewById(R.id.switchAtivado);
        listViewInteresses = rootView.findViewById(R.id.listaInteresses);
        txtDistancia = itemView.findViewById(R.id.txtDistancia);
        txtIdade = itemView.findViewById(R.id.txtIdade);

        if(!switchIdade.isChecked()){
            seekBarIdade.setVisibility(View.GONE);
        }
        if(switchDistancia.isChecked()){
            seekBarDistancia.setVisibility(View.GONE);
        }

        eventoDatabase();

        seekBarDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtDistancia.setText((i+2)+"km");
                distancia = (i+2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarIdade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtIdade.setText((i+2));
                idade = (i+2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void eventoDatabase() {

        Query query = mDatabaseReference.child("user").child(fbUser.getUid()).child("interessesList");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Interesse interesse = snap.getValue(Interesse.class);
                        listaInteresses.add(interesse);

                        if (getActivity() != null) {
                            adapter = new InteresseAdapter(getActivity(), listaInteresses);
                            listViewInteresses.setAdapter(adapter);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

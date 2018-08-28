package com.example.xandi.amicer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    public CreateGroupFragment() {
    }

    private EditText editNomeGrupo, editDescrGrupo, editInteresses;
    private TextView participantes;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupsDatabaseRef, mUserDatabaseRef;
    private Button btCriarGrupo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbUser;
    private User user;
    private List<String> listaInteresses;
    private EditText editInteresseGp1, editInteresseGp2, editInteresseGp3, editInteresseGp4;
    private int numParticpantes = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        participantes = (TextView) rootView.findViewById(R.id.progresso);
        editNomeGrupo = (EditText) rootView.findViewById(R.id.editNomeGrupo);
        editDescrGrupo = (EditText) rootView.findViewById(R.id.editDescrGrupo);
        btCriarGrupo = (Button) rootView.findViewById(R.id.button3);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        listaInteresses = new ArrayList<>();
        editInteresseGp1 = rootView.findViewById(R.id.editInteresseGp1);
        editInteresseGp2 = rootView.findViewById(R.id.editInteresseGp2);
        editInteresseGp3 = rootView.findViewById(R.id.editInteresseGp3);
        editInteresseGp4 = rootView.findViewById(R.id.editInteresseGp4);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                participantes.setText(""+(i+2));
                numParticpantes = (i+2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        fbUser = firebaseAuth.getCurrentUser();

        startFirebase();
        getUser();

        btCriarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group grupo = new Group();
                listaInteresses = new ArrayList<>();
                grupo.setUid(UUID.randomUUID().toString());
                grupo.setCriadorGrupo(fbUser.getDisplayName());
                grupo.setDescricao(editDescrGrupo.getText().toString());
                grupo.setInteresses(listaInteresses);
                grupo.setNome(editNomeGrupo.getText().toString());
                grupo.setNumParticipante(numParticpantes);
                //attendee.setNome(fbUser.getDisplayName());
                HashMap<String, Boolean> map;
                if(user.getListGroups()!=null){
                    map = user.getListGroups();
                    map.put(grupo.getUid(), true);
                    user.setListGroups(map);
                }else{
                    map = new HashMap<>();
                    map = user.getListGroups();
                    map.put(grupo.getUid(), true);
                }

                if(!editInteresseGp1.getText().toString().equals(""))
                    listaInteresses.add(editInteresseGp1.getText().toString());
                if(!editInteresseGp2.getText().toString().equals(""))
                    listaInteresses.add(editInteresseGp2.getText().toString());
                if(!editInteresseGp3.getText().toString().equals(""))
                    listaInteresses.add(editInteresseGp3.getText().toString());
                if(!editInteresseGp4.getText().toString().equals(""))
                    listaInteresses.add(editInteresseGp4.getText().toString());

                grupo.setInteresses(listaInteresses);

                mGroupsDatabaseRef.child("group").child(grupo.getUid()).setValue(grupo);
                mUserDatabaseRef.child("user").child(fbUser.getUid()).child("listGroups").setValue(map);
                limparCampos();
                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", grupo.getUid());
                intent.putExtra("nome", grupo.getNome());
                intent.putExtra("userUid", fbUser.getUid());
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void limparCampos() {
        editNomeGrupo.setText("");
        editDescrGrupo.setText("");
        editInteresseGp1.setText("");
        editInteresseGp2.setText("");
        editInteresseGp3.setText("");
        editInteresseGp4.setText("");
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupsDatabaseRef = mFirebaseDatabase.getReference();
        mUserDatabaseRef =mFirebaseDatabase.getReference();
    }

    private void getUser(){
        mUserDatabaseRef.child("user").child(fbUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
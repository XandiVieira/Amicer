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

import com.example.xandi.amicer.modelo.Attendee;
import com.example.xandi.amicer.modelo.Group;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    public CreateGroupFragment() {
    }

    private EditText editNomeGrupo, editDescrGrupo, editInteresses;
    private TextView participantes;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupsDatabaseRef, mAttendeeDatabaseRef;
    private Button btCriarGrupo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbUser;

    private int numParticpantes = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        participantes = (TextView) rootView.findViewById(R.id.progresso);
        editNomeGrupo = (EditText) rootView.findViewById(R.id.editNomeGrupo);
        editDescrGrupo = (EditText) rootView.findViewById(R.id.editDescrGrupo);
        editInteresses = (EditText) rootView.findViewById(R.id.editInteresses);
        btCriarGrupo = (Button) rootView.findViewById(R.id.button3);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                participantes.setText(""+(1+2));
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

        btCriarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group grupo = new Group();
                Attendee attendee = new Attendee();
                List<String> listaInteresses = new ArrayList<>();
                listaInteresses.add(editInteresses.getText().toString());
                grupo.setUid(UUID.randomUUID().toString());
                grupo.setCriadorGrupo(fbUser.getDisplayName());
                grupo.setDescricao(editDescrGrupo.getText().toString());
                grupo.setInteresses(listaInteresses);
                grupo.setNome(editNomeGrupo.getText().toString());
                grupo.setNumParticipante(numParticpantes);
                attendee.setUserUID(fbUser.getUid());
                attendee.setGroupUID(grupo.getUid());

                mGroupsDatabaseRef.child("group").child(grupo.getUid()).setValue(grupo);
                mAttendeeDatabaseRef.child("attendee").child(grupo.getUid()).setValue(attendee);
                limparCampos();
                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", grupo.getUid());
                intent.putExtra("nome", grupo.getNome());
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void limparCampos() {
        editNomeGrupo.setText("");
        editDescrGrupo.setText("");
        editInteresses.setText("");
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupsDatabaseRef = mFirebaseDatabase.getReference();
        mAttendeeDatabaseRef =mFirebaseDatabase.getReference();
    }
}

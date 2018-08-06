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
import android.widget.Toast;

import com.example.xandi.amicer.modelo.Group;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateGroupFragment extends Fragment {

    public CreateGroupFragment() {
    }

    private EditText editNomeGrupo;
    private EditText editDescrGrupo;
    private EditText editInteresses;
    private SeekBar seekBar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupsDatabaseReference;
    private Button button3;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        editNomeGrupo = (EditText) rootView.findViewById(R.id.editNomeGrupo);
        editDescrGrupo = (EditText) rootView.findViewById(R.id.editDescrGrupo);
        editInteresses = (EditText) rootView.findViewById(R.id.editInteresses);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        button3 = (Button) rootView.findViewById(R.id.button3);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        startFirebase();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group grupo = new Group();
                List<String> listaInteresses = new ArrayList<>();
                listaInteresses.add(editInteresses.getText().toString());
                grupo.setUid(UUID.randomUUID().toString());
                grupo.setCriadorGrupo(user.getDisplayName());
                grupo.setDescricao(editDescrGrupo.getText().toString());
                grupo.setInteresses(listaInteresses);
                grupo.setNome(editNomeGrupo.getText().toString());
                grupo.setNumParticipante(5);
                mGroupsDatabaseReference.child("group").child(grupo.getUid()).setValue(grupo);
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
        mGroupsDatabaseReference = mFirebaseDatabase.getReference();
    }
}

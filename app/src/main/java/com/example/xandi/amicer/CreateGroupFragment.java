package com.example.xandi.amicer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.google.firebase.database.ValueEventListener;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    public CreateGroupFragment() {
    }

    private EditText editNomeGrupo, editDescrGrupo;
    private TextView participantes;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupsDatabaseRef, mUserDatabaseRef, mTagsDatabaseRef;
    private Button btCriarGrupo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbUser;
    private User user;
    private int numParticpantes = 2;
    private List<List<Chip>> listaTags = new ArrayList<>();
    private ChipsInput mChipsInput;
    private List<String> listaCategorias;
    private Spinner spinnerTags;
    private List<Chip> tagsSugestoes = new ArrayList<>();
    private List<Chip> tagsSugestoes2;
    private List<String> tagsStr;
    private List<Chip> saveTags = new ArrayList<Chip>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        participantes = (TextView) rootView.findViewById(R.id.progresso);
        editNomeGrupo = (EditText) rootView.findViewById(R.id.editNomeGrupo);
        editDescrGrupo = (EditText) rootView.findViewById(R.id.editDescrGrupo);
        btCriarGrupo = (Button) rootView.findViewById(R.id.button3);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        spinnerTags = (Spinner) rootView.findViewById(R.id.spinner1);
        mChipsInput = (ChipsInput) rootView.findViewById(R.id.chipsInput1);

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
                grupo.setUid(UUID.randomUUID().toString());
                grupo.setCriadorGrupo(fbUser.getDisplayName());
                grupo.setDescricao(editDescrGrupo.getText().toString());
                //grupo.setInteresses(listaInteresses);
                grupo.setNome(editNomeGrupo.getText().toString());
                grupo.setNumParticipante(numParticpantes);
                HashMap<String, Boolean> map = new HashMap<>();
                if(user.getListGroups()!=null){
                    map = user.getListGroups();
                    map.put(grupo.getUid(), true);
                    user.setListGroups(map);
                }else{
                    user.setListGroups(map);
                    map.put(grupo.getUid(), true);
                }

                Interesse interesse;

                if(mChipsInput.equals("")) {
                }else {
                    List<Chip> lista = (List<Chip>) mChipsInput.getSelectedChipList();
                    interesse = new Interesse(lista, spinnerTags.getSelectedItem().toString());
                    if(!spinnerTags.getSelectedItem().equals("Selecione uma categoria"))
                        grupo.setCategoria(interesse);

                    List<String> listaSTR = new ArrayList<String>();
                    List<String> tagsSugestoesSTR = new ArrayList<String>();
                    for(int i=0; i<lista.size(); i++){
                        listaSTR.add(lista.get(i).getLabel());
                    }
                    for(int i=0; i<tagsSugestoes.size(); i++){
                        listaSTR.add(tagsSugestoes.get(i).getLabel());
                    }
                    for(int j=0; j<listaSTR.size(); j++){
                        if(!tagsSugestoesSTR.contains(listaSTR.get(j).trim())){
                            tagsSugestoesSTR.add(listaSTR.get(j).trim());
                        }
                    }
                }

                mGroupsDatabaseRef.child("group").child(grupo.getUid()).setValue(grupo);
                mUserDatabaseRef.child("user").child(fbUser.getUid()).child("listGroups").setValue(map);
                addTags();
                limparCampos();
                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", grupo.getUid());
                intent.putExtra("nome", grupo.getNome());
                intent.putExtra("userUid", fbUser.getUid());
                startActivity(intent);
                getTags();
            }
        });

        getTags();
        mChipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                // get the list
                List<Chip> tagsSelected = (List<Chip>) mChipsInput.getSelectedChipList();
                saveTags.addAll(tagsSelected);
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
            }

            @Override
            public void onTextChanged(CharSequence text) {
                if(text.length()>0){
                    if(text.charAt(text.length()-1) == ' '){
                        String texto = text.toString();
                        if(!texto.trim().isEmpty())
                        mChipsInput.addChip(texto.trim(), null);
                        text = "";
                    }}
            }
        });

        setSpinner();

        return rootView;
    }

    private void addTags() {
        mTagsDatabaseRef.child("tagsSuggestions").setValue(tagsSugestoes);
    }

    private void getTags(){
        tagsSugestoes = new ArrayList<Chip>();
        mTagsDatabaseRef.child("tagsSuggestions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Chip chip = snap.getValue(Chip.class);
                    tagsSugestoes.add(chip);
                    mChipsInput.setFilterableList(tagsSugestoes);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(mChipsInput!=null)
            mChipsInput.setFilterableList(tagsSugestoes);
    }

    private void setSpinner() {

        listaCategorias = new ArrayList<String>();

        mUserDatabaseRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCategorias.add("Selecione uma categoria");
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    listaCategorias.add(snap.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaCategorias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTags.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void limparCampos() {
        editNomeGrupo.setText("");
        editDescrGrupo.setText("");
        mChipsInput.removeAllViews();
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupsDatabaseRef = mFirebaseDatabase.getReference();
        mUserDatabaseRef = mFirebaseDatabase.getReference();
        mTagsDatabaseRef = mFirebaseDatabase.getReference();
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

    @Override
    public void onStop() {
        super.onStop();
    }
}
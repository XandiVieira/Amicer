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
import android.widget.Toast;

import com.example.xandi.amicer.modelo.Group;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.User;
import com.example.xandi.amicer.modelo.Util;
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

    //Screen elements
    private EditText editNomeGrupo, editDescrGrupo;
    private TextView participantesTV;
    private Button btCriarGrupo;
    private Spinner spinnerTags;
    private ChipsInput mChipsInput;
    private SeekBar seekBarParticipantes;

    //Database Settings
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupsDatabaseRef, mUserDatabaseRef, mTagsDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbUser;

    private User user;
    private int numParticpantes = 2;

    private List<String> listaCategorias;

    //Tags settings
    private List<Chip> tagsSugestoes = new ArrayList<>();
    private List<Chip> saveTags = new ArrayList<Chip>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        //Setting elements
        participantesTV = rootView.findViewById(R.id.progresso);
        editNomeGrupo = rootView.findViewById(R.id.editNomeGrupo);
        editDescrGrupo = rootView.findViewById(R.id.editDescrGrupo);
        btCriarGrupo = rootView.findViewById(R.id.button3);
        seekBarParticipantes = rootView.findViewById(R.id.seekBar);
        spinnerTags = rootView.findViewById(R.id.spinner1);
        mChipsInput = rootView.findViewById(R.id.chipsInput1);

        //Handling Number of participants seekbar
        seekBarParticipantes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                participantesTV.setText(""+(i+2));
                numParticpantes = (i+2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //initialize Firebase and get fb User
        firebaseAuth = FirebaseAuth.getInstance();
        fbUser = firebaseAuth.getCurrentUser();

        startFirebase();
        getUser();

        //get TagsSuggestions from database
        getTags();
        //Handle input chips
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

        //get Spinner itens from db
        setSpinner();

        btCriarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verify if the inputs are valid
                if(valida()){
                Group grupo = new Group();
                //Setting group Info
                grupo.setUid(UUID.randomUUID().toString());
                grupo.setCriadorGrupo(fbUser.getDisplayName());
                grupo.setDescricao(editDescrGrupo.getText().toString());
                grupo.setNome(editNomeGrupo.getText().toString());
                grupo.setNumParticipante(numParticpantes);
                //Add created group to the userGroups List
                HashMap<String, Boolean> mapUserGroups = new HashMap<>();
                if(user.getListGroups()!=null){
                    mapUserGroups = user.getListGroups();
                    mapUserGroups.put(grupo.getUid(), true);
                    user.setListGroups(mapUserGroups);
                }else{
                    user.setListGroups(mapUserGroups);
                    mapUserGroups.put(grupo.getUid(), true);
                }

                    //Get Chips
                    List<com.pchmn.materialchips.model.Chip> listaDeChips = (List<com.pchmn.materialchips.model.Chip>) mChipsInput.getSelectedChipList();
                List<Chip> chipList = new ArrayList<Chip>();
                for(int i=0; i<listaDeChips.size(); i++){
                    chipList.add(new Chip(listaDeChips.get(i).getLabel()));
                }
                //get Group's owner Localization
                    Localizacao localizacao = new Localizacao(Util.latitude, Util.getLongitude());
                    Interesse interesse = new Interesse(chipList, spinnerTags.getSelectedItem().toString(), localizacao);
                    // Group Interests
                    grupo.setCategoria(interesse);

                    //Adding tags Suggestions to a helping String list
                    List<String> tagsSugestoesSTR = new ArrayList<String>();
                    for(int i=0; i<tagsSugestoes.size(); i++){
                        tagsSugestoesSTR.add(tagsSugestoes.get(i).getLabel().trim());
                    }

                    //add chips to the tagsSuggestions list (if not repeated)
                    for(int i=0; i<chipList.size(); i++){
                        if(!tagsSugestoesSTR.contains(chipList.get(i).getLabel().trim())){
                            tagsSugestoes.add(chipList.get(i));
                        }
                    }

                    //save Group
                mGroupsDatabaseRef.child("group").child(grupo.getUid()).setValue(grupo);
                    //save User's groupList
                mUserDatabaseRef.child("user").child(fbUser.getUid()).child("listGroups").setValue(mapUserGroups);
                //Save tags Suggestions
                addTags();
                limparCampos();
                //Open New Group created screen
                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("uid", grupo.getUid());
                intent.putExtra("nome", grupo.getNome());
                intent.putExtra("userUid", fbUser.getUid());
                startActivity(intent);
            }}
        });

        return rootView;
    }

    private boolean valida() {
        if((!spinnerTags.getSelectedItem().equals("Selecione uma categoria") && spinnerTags != null) && !mChipsInput.getSelectedChipList().isEmpty()){
            if(!editNomeGrupo.getText().equals("")){
                if(editDescrGrupo.getText().toString().trim().length()>0){
                    if(mChipsInput.getSelectedChipList().size()<=7) {
                        if(!mChipsInput.equals("")){
                            return true;
                        }else{
                            Toast.makeText(getActivity(), "Adicione pelo menos 2 tags!", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else{
                        Toast.makeText(getActivity(), "Adicione até 7 interesses!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(getActivity(), "Descrição não pode estar vazia!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(getActivity(), "Nome do grupo não pode estar vazio!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(getActivity(), "Selecione uma categoria e adicione pelo menos 2 tags!", Toast.LENGTH_SHORT).show();
            return false;
        }
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

    @Override
    public void onStart() {
        super.onStart();
    }
}
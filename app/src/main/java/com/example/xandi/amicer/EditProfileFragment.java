package com.example.xandi.amicer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.User;
import com.example.xandi.amicer.modelo.Util;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EditProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private EditText editDescr;
    private Util util;
    private User user;
    private FirebaseUser fbUser = Util.fbUser;

    //private List<EditText> editInteresses= new ArrayList<EditText>();
    private List<String> listaCategorias;
    private List<Spinner> spinners = new ArrayList<Spinner>();
    //public User user;

    private List<List<Chip>> listaTags = new ArrayList<>();
    private List<Chip> tagsSugestoes = new ArrayList<>();

    private List<ChipsInput> mChipsInputList;
    private List<String> categories;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        //Fill the Chip's list with 4 chipsInput
        mChipsInputList = new ArrayList<ChipsInput>();
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput1));
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput2));
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput3));
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput4));

        photoImageView = rootView.findViewById(R.id.photoImageView);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        Button btLogout = rootView.findViewById(R.id.btLogout);
        editDescr = rootView.findViewById(R.id.editDescricao);

        //Set the spinner's list with 4 spinners
        spinners.add((Spinner) rootView.findViewById(R.id.spinner1));
        spinners.add((Spinner) rootView.findViewById(R.id.spinner2));
        spinners.add((Spinner) rootView.findViewById(R.id.spinner3));
        spinners.add((Spinner) rootView.findViewById(R.id.spinner4));

        getUserFromFB();

        //Initialize Firebase Auth
        Util.mFirebaseAuth = FirebaseAuth.getInstance();

        Util.mDatabaseRef.child("tagsSuggestions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagsSugestoes = new ArrayList<Chip>();
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Chip chip = snap.getValue(Chip.class);
                        tagsSugestoes.add(chip);
                        for(int i=0; i<mChipsInputList.size(); i++){
                            mChipsInputList.get(i).setFilterableList(tagsSugestoes);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listaCategorias = new ArrayList<String>();

        //Fill spinners with categories and set if the user already has it set
        Util.mDatabaseRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCategorias.add("Selecione uma categoria");
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    listaCategorias.add(snap.getKey());
                }
                for (int i=0; i<spinners.size(); i++){
                    if(user.getCategorias() != null && user.getCategorias().size() > i)
                        spinners.get(i).setSelection(4);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listaCategorias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinners.get(i).setAdapter(adapter);
                    if(user.getCategorias() != null && user.getCategorias().size() > i)
                        spinners.get(i).setSelection(adapter.getPosition(user.getCategorias().get(i)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button btUpdate = rootView.findViewById(R.id.btUpdate);

        for(int i=0; i<mChipsInputList.size(); i++){
            final int finalI = i;
            mChipsInputList.get(i).addChipsListener(new ChipsInput.ChipsListener() {
                @Override
                public void onChipAdded(ChipInterface chip, int newSize) {
                    // get the list
                    List<Chip> tagsSelected = (List<Chip>) mChipsInputList.get(finalI).getSelectedChipList();
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
                            mChipsInputList.get(finalI).addChip(texto.trim(), null);
                            text = "";
                        }}
                }
            });
        }


        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<List<Chip>> listChip = new ArrayList<List<Chip>>();
                //user = new User();
                user.setTags(listChip);

                for (int i=0; i<mChipsInputList.size(); i++){
                    if(mChipsInputList.get(i).equals("")) {
                    }else {
                        List<Chip> lista = (List<Chip>) mChipsInputList.get(i).getSelectedChipList();
                        listChip.add(lista);
                        for(int j=0; j<lista.size(); j++){
                            if(!tagsSugestoes.contains(lista.get(j))){
                                tagsSugestoes.add(lista.get(j));
                            }
                        }
                        Util.mDatabaseRef.child("tagsSuggestions").setValue(tagsSugestoes);
                        if(!spinners.get(i).getSelectedItem().equals("Selecione uma categoria"))
                            user.getCategorias().set(i, spinners.get(i).getSelectedItem().toString());
                    }
                }
                user.setTags(listChip);
                updateProfile();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        return rootView;
    }

    //Get user from Firebase Database
    private void getUserFromFB() {
        Util.mUserDatabaseRef.child(Util.fbUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                if(user == null){
                    user = new User();
                    user.setNome(Util.fbUser.getDisplayName());
                    user.setUid(Util.fbUser.getUid());
                    List<String> listaCategorias = new ArrayList<String>();
                    listaCategorias.add("Selecione uma categoria");
                    listaCategorias.add("Selecione uma categoria");
                    listaCategorias.add("Selecione uma categoria");
                    listaCategorias.add("Selecione uma categoria");
                    user.setCategorias(listaCategorias);
                    user.setFotoPerfil(Util.fbUser.getPhotoUrl().toString());
                    Util.mUserDatabaseRef.child(Util.fbUser.getUid()).setValue(user);
                }else{
                    getUserProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Util.mFirebaseAuth.addAuthStateListener(Util.mFirebaseAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Util.googleApiClient.stopAutoManage(getActivity());
        Util.googleApiClient.disconnect();
    }

    public void goLogInScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.mFirebaseAuthListener != null) {
            Util.mFirebaseAuth.removeAuthStateListener(Util.mFirebaseAuthListener);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void logOut() {

        //Logout Facebook
        Util.mFirebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        goLogInScreen();

        /*Util.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(Util.googleApiClient == null){
            Util.googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, Util.gso)
                    .build();}

        Util.googleApiClient.connect();
        Auth.GoogleSignInApi.signOut(Util.googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Util.googleApiClient.disconnect();
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.log_out, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    //Get, if there is, spinners and chips data
    private void getUserProfile(){
        setBasicInfo();
            if (user.getCategorias() != null) {
                for (int i = 0; i < user.getCategorias().size(); i++) {
                    spinners.get(i).setSelection(listaCategorias.indexOf(user.getCategorias().get(i)));
                }
            }

            editDescr.setText(user.getDescricao(), TextView.BufferType.EDITABLE);
            if(user.getTags() != null){
                for (int i=0; i<user.getTags().size(); i++){
                    for (int j=0; j<user.getTags().get(i).size(); j++){
                        mChipsInputList.get(i).addChip(user.getTags().get(i).get(j).getLabel(), null);
                    }
                }}
    }

    //Set Basic Info -- Age, Name and Photo
    private void setBasicInfo() {
        if(user.getIdade() > 0){
            if(user.getNome()!=null){
                nameTextView.setText(user.getNome() + ", " + user.getIdade());
            }else{
                nameTextView.setText(fbUser.getDisplayName() + ", " + user.getIdade());
            }
        }else{
            nameTextView.setText(Util.fbUser.getDisplayName());
        }

        Glide.with(getApplicationContext()).load(user.getFotoPerfil()).into(photoImageView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateProfile(){
        if(!editDescr.getText().toString().equals(""))
            user.setDescricao(editDescr.getText().toString());
        //user.setFotoPerfil();
        //user.setListaTags(listaTags);

        Util.mUserDatabaseRef.child(Util.fbUser.getUid()).setValue(user);
        Toast.makeText(getApplicationContext(), "Perfil atualizado",Toast.LENGTH_SHORT).show();
        listaTags.clear();
    }
}

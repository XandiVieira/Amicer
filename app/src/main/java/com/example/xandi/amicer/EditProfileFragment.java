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
    //private EditText editFrase;

    //private List<EditText> editInteresses= new ArrayList<EditText>();
    private List<String> listaCategorias;
    private List<Spinner> spinners = new ArrayList<Spinner>();

    private FirebaseUser fbUser;
    public User user;
    private Interesse interesse;

    private DatabaseReference mUserDatabaseRef;

    private GoogleApiClient googleApiClient;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private List<List<Chip>> listaTags = new ArrayList<>();
    private List<List<Chip>> tagsSugestoes = new ArrayList<>();

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();}

        firebaseAuth = FirebaseAuth.getInstance();

        user = new User();

        startFirebase();

        mChipsInputList = new ArrayList<ChipsInput>();
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput1));
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput2));
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput3));
        mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput4));
        //mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput5));
        //mChipsInputList.add((ChipsInput) rootView.findViewById(R.id.chipsInput6));

        if(mChipsInputList!=null)
        for(int i=0; i<mChipsInputList.size(); i++){
            //if(mChipsInputList.get(i)!=null)
           // mChipsInputList.get(i).setFilterableList(tagsSugestoes.get(i));
        }

        photoImageView = rootView.findViewById(R.id.photoImageView);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        Button btLogout = rootView.findViewById(R.id.btLogout);
        editDescr = rootView.findViewById(R.id.editDescricao);
       //editFrase = rootView.findViewById(R.id.editFrasePerfil);

        spinners.add((Spinner) rootView.findViewById(R.id.spinner1));
        spinners.add((Spinner) rootView.findViewById(R.id.spinner2));
        spinners.add((Spinner) rootView.findViewById(R.id.spinner3));
        spinners.add((Spinner) rootView.findViewById(R.id.spinner4));
        //spinners.add((Spinner) rootView.findViewById(R.id.spinner5));
        //spinners.add((Spinner) rootView.findViewById(R.id.spinner6));

        setSpinner();

        Button btUpdate = rootView.findViewById(R.id.btUpdate);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null) {
                    setUserData(fbUser);
                    for (int i=0; i<mChipsInputList.size(); i++){
                        //mChipsInputList.set(i, user.getCategoriaTag().get(i).get())
                    }
                    //listaTags = user.getListaTags();
                } else {
                    goLogInScreen();
                }
            }
        };

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
                            mChipsInputList.get(finalI).addChip(text.toString(), null);
                            text = "";
                        }}
                }
            });
        }


        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    List<List<Chip>> listChip = new ArrayList<List<Chip>>();
                    user.setTags(listChip);

                    List<String> listCategories = new ArrayList<String>();
                    user.setCategorias(listCategories);


                    for (int i=0; i<mChipsInputList.size(); i++){
                        if(mChipsInputList.get(i).equals("")) {
                        }else {
                            List<Chip> lista = (List<Chip>) mChipsInputList.get(i).getSelectedChipList();
                            listChip.add(lista);

                            if(!spinners.get(i).getSelectedItem().equals("Selecione uma categoria"))
                                listCategories.add(spinners.get(i).getSelectedItem().toString());
                        }
                }
                user.setCategorias(listCategories);
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

    private void setSpinner() {
        listaCategorias = new ArrayList<String>();

        mUserDatabaseRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCategorias.add("Selecione uma categoria");
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    listaCategorias.add(snap.getKey());
                }
                for (int i=0; i<spinners.size(); i++){
                    if(user.getCategorias()!= null && user.getCategorias().size() > i)
                    spinners.get(i).setSelection(4);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaCategorias);
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
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
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

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void logOut() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();}

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.log_out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUserData(FirebaseUser fbUser) {
        if(user.getIdade() > 0){
            if(user.getNome()!=null){
                nameTextView.setText(user.getNome() + ", " + user.getIdade());
            }else{
                nameTextView.setText(fbUser.getDisplayName() + ", " + user.getIdade());
            }
        }else{
            nameTextView.setText(fbUser.getDisplayName());
        }
        getUser();
        Glide.with(getApplicationContext()).load(fbUser.getPhotoUrl()).into(photoImageView);
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(getActivity());
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseRef = mFirebaseDatabase.getReference();
    }

    private void getUser(){
        mUserDatabaseRef.child("user").child(fbUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if(user == null){
                    user = new User();
                    user.setNome(fbUser.getDisplayName());
                    user.setUid(fbUser.getUid());
                    mUserDatabaseRef.child("user").child(fbUser.getUid()).setValue(user);
                }else{
                   /* for (int i=0; i < editInteresses.size(); i++){
                        if(user.getInteressesList()!=null && user.getInteressesList().size()>i)
                            for(int j=0; j<user.getInteressesList().get(i).getTags().size(); j++) {
                                editInteresses.get(i).setText(editInteresses.get(i) + " " + user.getInteressesList().get(i).getTags().get(j));
                            }
                    }*/
                   if (user.getCategorias() != null)
                   for(int i=0; i<user.getCategorias().size(); i++){
                       spinners.get(i).setSelection(listaCategorias.indexOf(user.getCategorias().get(i)));
                   }

                    editDescr.setText(user.getDescricao(), TextView.BufferType.EDITABLE);
                    //editFrase.setText(user.getFrase(), TextView.BufferType.EDITABLE);
                    if(user.getTags() != null)
                    for (int i=0; i<user.getTags().size(); i++){
                        for (int j=0; j<user.getTags().get(i).size(); j++){
                            mChipsInputList.get(i).addChip(user.getTags().get(i).get(j).getLabel(), null);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateProfile(){
        //if(!editFrase.getText().toString().equals(""))
            //user.setFrase(editFrase.getText().toString());
        if(!editDescr.getText().toString().equals(""))
            user.setDescricao(editDescr.getText().toString());
        //user.setFotoPerfil();
        //user.setListaTags(listaTags);

        mUserDatabaseRef.child("user").child(user.getUid()).setValue(user);
        Toast.makeText(getApplicationContext(), "Perfil atualizado",Toast.LENGTH_SHORT).show();
        listaTags.clear();
    }
}

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EditProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private EditText editDescr;
    private EditText editFrase;
    private EditText editInteresse1, editInteresse2, editInteresse3, editInteresse4, editInteresse5, editInteresse6;
    private FirebaseUser fbUser;
    public User user;

    private DatabaseReference mUserDatabaseRef;

    private GoogleApiClient googleApiClient;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private List<String> listaInteresses;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        listaInteresses = new ArrayList<>();

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

        photoImageView = rootView.findViewById(R.id.photoImageView);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        Button btLogout = rootView.findViewById(R.id.btLogout);
        editDescr = rootView.findViewById(R.id.editDescricao);
        editFrase = rootView.findViewById(R.id.editFrasePerfil);
        editInteresse1 = rootView.findViewById(R.id.editInteresse1);
        editInteresse2 = rootView.findViewById(R.id.editInteresse2);
        editInteresse3 = rootView.findViewById(R.id.editInteresse3);
        editInteresse4 = rootView.findViewById(R.id.editInteresse4);
        editInteresse5 = rootView.findViewById(R.id.editInteresse5);
        editInteresse6 = rootView.findViewById(R.id.editInteresse6);
        Button btUpdate = rootView.findViewById(R.id.btUpdate);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null) {
                    setUserData(fbUser);
                } else {
                    goLogInScreen();
                }
            }
        };

        return rootView;
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
                    if(user.getInteressesList()!=null && user.getInteressesList().size()>0)
                        editInteresse1.setText(user.getInteressesList().get(0));
                    if(user.getInteressesList()!=null && user.getInteressesList().size()>1)
                        editInteresse2.setText(user.getInteressesList().get(1));
                    if(user.getInteressesList()!=null && user.getInteressesList().size()>2)
                        editInteresse3.setText(user.getInteressesList().get(2));
                    if(user.getInteressesList()!=null && user.getInteressesList().size()>3)
                        editInteresse4.setText(user.getInteressesList().get(3));
                    if(user.getInteressesList()!=null && user.getInteressesList().size()>4)
                        editInteresse5.setText(user.getInteressesList().get(4));
                    if(user.getInteressesList()!=null && user.getInteressesList().size()>5)
                        editInteresse6.setText(user.getInteressesList().get(5));
                    editDescr.setText(user.getDescricao(), TextView.BufferType.EDITABLE);
                    editFrase.setText(user.getFrase(), TextView.BufferType.EDITABLE);
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
        if(!editFrase.getText().toString().equals(""))
            user.setFrase(editFrase.getText().toString());
        if(!editDescr.getText().toString().equals(""))
            user.setDescricao(editDescr.getText().toString());
        //user.setFotoPerfil();


        if(!editInteresse1.getText().toString().equals(""))
            listaInteresses.add(editInteresse1.getText().toString());
        if(!editInteresse2.getText().toString().equals(""))
            listaInteresses.add(editInteresse2.getText().toString());
        if(!editInteresse3.getText().toString().equals(""))
            listaInteresses.add(editInteresse3.getText().toString());
        if(!editInteresse4.getText().toString().equals(""))
            listaInteresses.add(editInteresse4.getText().toString());
        if(!editInteresse5.getText().toString().equals(""))
            listaInteresses.add(editInteresse5.getText().toString());
        if(!editInteresse6.getText().toString().equals(""))
            listaInteresses.add(editInteresse6.getText().toString());

        user.setInteressesList(listaInteresses);

        mUserDatabaseRef.child("user").child(user.getUid()).setValue(user);
        Toast.makeText(getApplicationContext(), "Perfil atualizado",Toast.LENGTH_SHORT).show();
        listaInteresses.clear();
    }
}

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.User;
import com.example.xandi.amicer.modelo.Util;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EditProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    //Screen elements
    private ImageView photoImageView;
    private TextView nameTextView;
    private EditText editDescr;
    private List<Spinner> spinners = new ArrayList<Spinner>();
    private List<Chip> tagsSugestoes = Util.getTagsSugestoes();
    private List<ChipsInput> mChipsInputList;

    private User user;
    private List<List<Chip>> listaTags = new ArrayList<>();
    private List<String> listaCategorias = Util.getListaCategorias();

    private FirebaseUser fbUser = Util.fbUser;

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
        mChipsInputList.add(rootView.findViewById(R.id.chipsInput1));
        mChipsInputList.add(rootView.findViewById(R.id.chipsInput2));
        mChipsInputList.add(rootView.findViewById(R.id.chipsInput3));
        mChipsInputList.add(rootView.findViewById(R.id.chipsInput4));

        //Setting elements
        photoImageView = rootView.findViewById(R.id.photoImageView);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        Button btLogout = rootView.findViewById(R.id.btLogout);
        editDescr = rootView.findViewById(R.id.editDescricao);

        //Set the spinner's list with 4 spinners
        spinners.add(rootView.findViewById(R.id.spinner1));
        spinners.add(rootView.findViewById(R.id.spinner2));
        spinners.add(rootView.findViewById(R.id.spinner3));
        spinners.add(rootView.findViewById(R.id.spinner4));

       /*/////////////////////////////////////////*/



       /*/////////////////////////////////////////*/


        ImageButton btUpdate = rootView.findViewById(R.id.btUpdate);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Chip> listChip;
                //user = new User();
                List<Interesse> interesse = new ArrayList<Interesse>();
                user.setCategorias(interesse);

                //Adding tags Suggestions to a helping String list
                List<String> tagsSugestoesSTR = new ArrayList<String>();
                for(int i=0; i<tagsSugestoes.size(); i++){
                    tagsSugestoesSTR.add(tagsSugestoes.get(i).getLabel().trim());
                }

                //Handle chips and spinners by each spinner
                for(int i=0; i<spinners.size(); i++){
                    listChip = new ArrayList<Chip>();
                    if(!spinners.get(i).getSelectedItem().equals("Selecione uma categoria")){
                        if(mChipsInputList.get(i).getSelectedChipList().size()>0 && mChipsInputList.get(i) !=null)
                            for(int j=0; j<mChipsInputList.get(i).getSelectedChipList().size(); j++){
                                listChip.add(new Chip(mChipsInputList.get(i).getSelectedChipList().get(j).getLabel()));
                            }
                        user.getCategorias().add(new Interesse(listChip, spinners.get(i).getSelectedItem().toString()));

                        List<String> listaSTR = new ArrayList<String>();
                        //Add Chips texts to helping String list
                        for(int j=0; j<listChip.size(); j++){
                            listaSTR.add(listChip.get(j).getLabel().trim());
                        }
                        //Verify if suggestion is already on database (if it's not then add)
                        for(int j=0; j<listaSTR.size(); j++){
                            if(!tagsSugestoesSTR.contains(listaSTR.get(j).trim())){
                                tagsSugestoes.add(listChip.get(j));
                            }
                        }
                    }

                }

                Util.mDatabaseRef.child("tagsSuggestions").setValue(tagsSugestoes);

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
                if(user != null){
                    getUserProfile();
            }
    }

    @Override
    public void onStart() {
        super.onStart();

        Util.mFirebaseAuth.addAuthStateListener(Util.mFirebaseAuthListener);

        getUserFromFB();

        for(int i=0; i<mChipsInputList.size(); i++){
            mChipsInputList.get(i).setFilterableList(tagsSugestoes);
        }

        user = Util.getUser();
        if (user!=null) {
            for (int i = 0; i < spinners.size(); i++) {
                if (user.getCategorias() != null && user.getCategorias().size() > i)
                    spinners.get(i).setSelection(4);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listaCategorias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinners.get(i).setAdapter(adapter);
                if (user.getCategorias() != null && user.getCategorias().size() > i)
                    spinners.get(i).setSelection(adapter.getPosition(user.getCategorias().get(i).getCategoria()));
            }
        }
        getUserFromFB();

        //Handle input chips
        for(int i=0; i<mChipsInputList.size(); i++){
            final int finalI = i;
            mChipsInputList.get(i).addChipsListener(new ChipsInput.ChipsListener() {
                @Override
                public void onChipAdded(ChipInterface chip, int newSize) {
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
        Localizacao localizacao = new Localizacao(Util.getLatitude(), Util.getLongitude());
        Util.getmUserDatabaseRef().child(Util.getFbUser().getUid()).child("localizacao").setValue(localizacao);
            if (user.getCategorias() != null) {
                for (int i = 0; i < user.getCategorias().size(); i++) {
                    spinners.get(i).setSelection(listaCategorias.indexOf(user.getCategorias().get(i).getCategoria()));
                    if(user.getCategorias().get(i).getTags()!=null)
                    for(int j=0; j<user.getCategorias().get(i).getTags().size(); j++){
                        mChipsInputList.get(i).addChip(user.getCategorias().get(i).getTags().get(j));
                    }
                }
            }

            editDescr.setText(user.getDescricao(), TextView.BufferType.EDITABLE);
            /*if(user.getTags() != null){
                for (int i=0; i<user.getTags().size(); i++){
                    for (int j=0; j<user.getTags().get(i).size(); j++){
                        mChipsInputList.get(i).addChip(user.getTags().get(i).get(j).getLabel(), null);
                    }
                }}*/
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

package com.example.xandi.amicer;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xandi.amicer.adapters.MessageAdapter;
import com.example.xandi.amicer.models.Message;
import com.example.xandi.amicer.models.Util;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String FRIENDLY_MSG_LENGTH_KEY = "friendly_msg_length";
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER =  2;

    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    //fIREBASE Instance Variiables
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private StorageReference mChatPhotoStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String userUid;
    private AlertDialog alerta;
    private String groupUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mUsername = ANONYMOUS;

        Bundle bundleUid = getIntent().getExtras();
        String uid = bundleUid.getString("uid");
        groupUID = uid;

        Bundle bundleNome = getIntent().getExtras();
        bundleNome.getString("nome");
        String nome = bundleUid.getString("nome");

        Bundle bundleUserUid = getIntent().getExtras();
        bundleUserUid.getString("userUid");
        userUid = bundleUserUid.getString("userUid");

       setTitle(nome);

        //Initialize Firebase Components
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("/group/"+uid).child("messages");
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        // Initialize references to views
        ProgressBar mProgressBar = findViewById(R.id.progressBar);
        ListView mMessageListView = findViewById(R.id.messageListView);
        mMessageEditText = findViewById(R.id.messageEditText);

        // Initialize message ListView and its adapter
        ArrayList<Message> mensagens = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(ChatActivity.this, mensagens, userUid);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(view -> {
            SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

            Date data = new Date();

            Calendar  cal = Calendar.getInstance();
            cal.setTime(data);
            Date data_atual = cal.getTime();

            String hora_atual = dateFormat_hora.format(data_atual);
            Message message = new Message(mMessageEditText.getText().toString(), mUsername, null, hora_atual, Util.getUser().getUid());
            mMessagesDatabaseReference.push().setValue(message);
            // Clear input box
            mMessageEditText.setText("");
        });

        attachDatabaseReadListener();

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                onSignedInInitialize(user.getDisplayName());
                //Toast.makeText(MainActivity.this, "You are now signed in. Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();
            } else {
                onSignedOutCleanup();
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
            }
        };
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(FRIENDLY_MSG_LENGTH_KEY, DEFAULT_MSG_LENGTH_LIMIT);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);
        fecthConfig();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(ChatActivity.this, "Signed In!", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Signed In Canceled!", Toast.LENGTH_SHORT).show();
                finish();
            } else if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();

                //Get a reference to store file at chat_photos/<FILENAME>
                final StorageReference photoRef = mChatPhotoStorageReference.child(selectedImageUri.getLastPathSegment());

                //Upload file to Firebase Storage
                photoRef.putFile(selectedImageUri).addOnSuccessListener(this, taskSnapshot -> {
                    Task<Uri> downloadUrl = photoRef.getDownloadUrl();
                    // OU
                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data1 = new Date();

                    Calendar  cal = Calendar.getInstance();
                    cal.setTime(data1);
                    Date data_atual = cal.getTime();

                    String hora_atual = dateFormat_hora.format(data_atual);
                    Message message = new Message(null, mUsername, downloadUrl.toString(), hora_atual, userUid);
                    mMessagesDatabaseReference.push().setValue(message);
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }


    public void onSignedInInitialize(String username){
        mUsername = username;
        attachDatabaseReadListener();
    }

    public void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
    }

    private void detachDatabaseReadListener() {
        if(mChildEventListener != null){
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void attachDatabaseReadListener() {
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    mMessageAdapter.add(message);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }}

    private void fecthConfig() {
        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseRemoteConfig.activateFetched();
                applyRetrievedLengthLimit();
            }
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error fetching config", e);
            applyRetrievedLengthLimit();
        });
    }

    private void applyRetrievedLengthLimit() {
        long friendly_msg_length = mFirebaseRemoteConfig.getLong(FRIENDLY_MSG_LENGTH_KEY);

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) friendly_msg_length)});
        Log.d(TAG, FRIENDLY_MSG_LENGTH_KEY + " = " + friendly_msg_length);
    }

}

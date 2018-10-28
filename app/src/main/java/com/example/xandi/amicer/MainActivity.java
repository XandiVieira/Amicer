package com.example.xandi.amicer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.xandi.amicer.modelo.Interesse;
import com.example.xandi.amicer.modelo.User;
import com.example.xandi.amicer.modelo.Util;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseUser fbUser;
    private User user;
    private DatabaseReference mDatabaseRef, mUserDatabaseRef;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    public Util util;
    private GoogleSignInOptions gso;
    private double longitude, latitude;
    private List<Chip> tagsSugestoes = new ArrayList<>();
    private List<String> listaCategorias;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //SharePrefManager.getmInstance(MainActivity.this).getToken();
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseInstanceIDService.TOKEN_BROADCAST));

        if(SharePrefManager.getmInstance(this).getToken() != null){
            Log.d("myfcmtokenshared", SharePrefManager.getmInstance(this).getToken());
        }

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();}

        mFirebaseAuth = FirebaseAuth.getInstance();

        startFirebase();

        fbUser = mFirebaseAuth.getCurrentUser();

       getUserFromFB();
        getTagsSuggestions();
        fillSpinners();

        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (fbUser != null) {
                    location();
                    util = new Util(gso, fbUser, mDatabaseRef, mUserDatabaseRef, googleApiClient, mFirebaseAuth, mFirebaseAuthListener, latitude, longitude, user, tagsSugestoes, listaCategorias);
                } else {
                    goLogInScreen();
                }
            }
        };

        if(fbUser!=null){
            SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            ViewPager mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
    }

    private void fillSpinners() {
        listaCategorias = new ArrayList<String>();
        //Fill spinners with categories and set if the user already has it set
        mDatabaseRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCategorias.add("Selecione uma categoria");
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    listaCategorias.add(snap.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTagsSuggestions() {
        mDatabaseRef.child("tagsSuggestions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagsSugestoes = new ArrayList<Chip>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Chip chip = snap.getValue(Chip.class);
                    tagsSugestoes.add(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void location(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void logout(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signOut();
        LoginManager.getInstance().logOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this, this)
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

    private void startFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference();
        mUserDatabaseRef = mFirebaseDatabase.getReference().child("user");
    }

    public void goLogInScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mFirebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    //Get user from Firebase Database
    private void getUserFromFB() {
        mUserDatabaseRef.child(fbUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user == null){
                    user = new User();
                    user.setNome(fbUser.getDisplayName());
                    user.setUid(fbUser.getUid());
                    Localizacao localizacao = new Localizacao(latitude, longitude);
                    user.setLocalizacao(localizacao);
                    List<Interesse> listaCategorias = new ArrayList<Interesse>();
                    List<Chip> chipList = new ArrayList<Chip>();
                    listaCategorias.add(new Interesse(chipList, "Selecione uma categoria"));
                    listaCategorias.add(new Interesse(chipList, "Selecione uma categoria"));
                    listaCategorias.add(new Interesse(chipList, "Selecione uma categoria"));
                    listaCategorias.add(new Interesse(chipList, "Selecione uma categoria"));
                    user.setCategorias(listaCategorias);
                    user.setFotoPerfil(fbUser.getPhotoUrl().toString());
                    mUserDatabaseRef.child(fbUser.getUid()).setValue(user);
                }
                util.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

package com.example.xandi.amicer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.xandi.amicer.models.Interest;
import com.example.xandi.amicer.models.User;
import com.example.xandi.amicer.models.Util;
import com.example.xandi.amicer.objects.Tag;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseUser fbUser;
    private User user;
    private DatabaseReference mUserDatabaseRef,mTagsDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    public Util util;
    private double longitude, latitude;
    private List<Tag> tagsSugestoes = new ArrayList<>();

    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton tabProfile = findViewById(R.id.profileTab);
        ImageButton tabFriends = findViewById(R.id.friendsTab);

        //Initiate firebase instances
        startFirebase();

        //getting firebase Facebook info user
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        //verify the user is logged in
        if (fbUser == null) {
            //go to login activity
            goLoginScreen();
        } else if (user == null) {
            //set fbUser to Util class
            Util.setFbUser(fbUser);
            //retrieve user data from Firebase
            getUserFromFB();
        }

        tabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        tabFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
            }
        });
    }

    //create a new profile for the user
    private void createUser() {
        setUser();
        Util.setUser(user);
        mUserDatabaseRef.child(fbUser.getUid()).setValue(user);
    }

    private void setUser() {
        user = new User();
        user.setName(fbUser.getDisplayName());
        user.setUid(fbUser.getUid());
        user.setEmail(fbUser.getEmail());
        user.setInterests(new ArrayList<Interest>());
    }

    public void location() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            android.location.Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(android.location.Location location) {
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location();

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = mFirebaseDatabase.getReference();
        mUserDatabaseRef = mDatabaseRef.child("user");
        mTagsDatabaseRef = mDatabaseRef.child("tagsSuggestions");
        Util.setmTagsDatabaseRef(mTagsDatabaseRef);
        Util.setmDatabaseRef(mDatabaseRef);
        Util.setmUserDatabaseRef(mUserDatabaseRef);
    }

    public void goLoginScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Get user from Firebase Database
    private void getUserFromFB() {
        mUserDatabaseRef.child(fbUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user == null){
                    //In case it has not found anything, create a new profile for the user
                    createUser();
                }
                //set user for the Util class
                Util.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void goToMatches(View view) {
    }

    public void goToSettings(View view) {
    }
}

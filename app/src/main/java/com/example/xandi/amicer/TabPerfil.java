package com.example.xandi.amicer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TabPerfil extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;

    private GoogleApiClient googleApiClient;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private BottomNavigationView mPerfilNav;
    private FrameLayout mFramePerfil;

    private EditProfileFragment editProfileFragment;
    private ConfigProfileFragment configGroupFragment;

    private Context mContext;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__perfil, container, false);

        editProfileFragment = new EditProfileFragment();
        configGroupFragment = new ConfigProfileFragment();
        photoImageView = rootView.findViewById(R.id.photoImageView);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        emailTextView = rootView.findViewById(R.id.emailTextView);
        idTextView = rootView.findViewById(R.id.idTextView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setFragment(configGroupFragment);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
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

        mPerfilNav = view.findViewById(R.id.navPerfil);
        mFramePerfil = view.findViewById(R.id.framePerfil);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                }
            }
        };

        mPerfilNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_editar :
                        mPerfilNav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(editProfileFragment);
                        return true;

                    case R.id.item_config_perfil :
                        mPerfilNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(configGroupFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framePerfil, fragment);
        fragmentTransaction.commit();
    }

    public void logOut() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (!status.isSuccess()) {
                    Toast.makeText(getApplicationContext(), R.string.log_out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void revoke() {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (!status.isSuccess()) {
                    Toast.makeText(getApplicationContext(), R.string.not_revoke, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void setUserData(FirebaseUser user) {
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        idTextView.setText(user.getUid());
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(photoImageView);
    }
}

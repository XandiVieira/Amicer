package com.example.xandi.amicer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class TabPerfil extends Fragment {

    private BottomNavigationView mPerfilNav;
    private FrameLayout mFramePerfil;

    private EditProfileFragment editProfileFragment;
    private ConfigProfileFragment configGroupFragment;

    private Context mContext;
    public FirebaseUser user;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_perfil, container, false);

        editProfileFragment = new EditProfileFragment();
        configGroupFragment = new ConfigProfileFragment();

        setFragment(editProfileFragment);

        return rootView;
    }

    public void goLogInScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPerfilNav = view.findViewById(R.id.navPerfil);
        mFramePerfil = view.findViewById(R.id.framePerfil);

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
}

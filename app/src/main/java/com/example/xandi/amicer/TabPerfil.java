package com.example.xandi.amicer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TabPerfil extends Fragment {

    private BottomNavigationView mPerfilNav;
    private FrameLayout mFramePerfil;

    private EditProfileFragment editProfileFragment;
    private FilterProfileFragment filterProfileFragment;

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
        filterProfileFragment = new FilterProfileFragment();

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

                    case R.id.item_filtros_perfil :
                        mPerfilNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(filterProfileFragment);
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
        fragmentTransaction.commitAllowingStateLoss();
    }
}

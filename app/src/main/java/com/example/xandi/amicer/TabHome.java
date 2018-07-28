package com.example.xandi.amicer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.TextView;

public class TabHome extends Fragment {

    private BottomNavigationView mHomeNav;
    private FrameLayout mFrameHome;

    private NotifsHomeFragment notifsHomeFragment;
    private StatsHomeFragment statsHomeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        statsHomeFragment = new StatsHomeFragment();
        notifsHomeFragment = new NotifsHomeFragment();

        setFragment(statsHomeFragment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHomeNav = (BottomNavigationView) view.findViewById(R.id.navHome);
        mFrameHome = (FrameLayout) view.findViewById(R.id.frameHome);

        mHomeNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_dados :
                        mHomeNav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(statsHomeFragment);
                        return true;

                    case R.id.item_notificacoes :
                        mHomeNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(notifsHomeFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameHome, fragment);
        fragmentTransaction.commit();
    }

}

package com.example.xandi.amicer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class TabGrupos extends Fragment {

    private BottomNavigationView mGruposNav;
    private FrameLayout mFrameGrupos;

    private GroupsSeeFragment groupsSeeFragment;
    private FilterGroupFragment filterGroupFragment;
    private CreateGroupFragment createGroupFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_grupos, container, false);

        groupsSeeFragment = new GroupsSeeFragment();
        filterGroupFragment = new FilterGroupFragment();
        createGroupFragment = new CreateGroupFragment();

        setFragment(groupsSeeFragment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGruposNav = (BottomNavigationView) view.findViewById(R.id.navGrupos);
        mFrameGrupos = (FrameLayout) view.findViewById(R.id.frameGrupos);

        mGruposNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_filtros:
                        mGruposNav.setItemBackgroundResource(R.color.colorAccent);
                        FilterGroupFragment filterGroupFragment = new FilterGroupFragment();
                        setFragment(filterGroupFragment);
                        return true;

                    case R.id.item_criar :
                        mGruposNav.setItemBackgroundResource(R.color.colorPrimary);
                        CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                        setFragment(createGroupFragment);
                        return true;

                    case R.id.item_grupos :
                        mGruposNav.setItemBackgroundResource(R.color.colorPrimaryDark);
                        GroupsSeeFragment groupsSeeFragment = new GroupsSeeFragment();
                        setFragment(groupsSeeFragment);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameGrupos, fragment);
        ft.commit();
    }
}

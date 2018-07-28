package com.example.xandi.amicer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionPagerAdapter extends FragmentPagerAdapter {


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TabPerfil tabPerfil = new TabPerfil();
                return tabPerfil;
            case 1:
                TabHome tabHome = new TabHome();
                return tabHome;
            case 2:
                TabGrupos tabGrupos = new TabGrupos();
                return tabGrupos;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "PERFIL";
            case 1:
                return "HOME";
            case 2:
                return "GRUPOS";
        }
        return null;
    }
}

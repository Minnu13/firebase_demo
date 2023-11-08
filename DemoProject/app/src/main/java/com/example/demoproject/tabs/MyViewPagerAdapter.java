package com.example.demoproject.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

class MyViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> names;
    private ArrayList<Fragment> fragments;

    public MyViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<String> names, ArrayList<Fragment> fragments) {
        super(fm);
        this.names = names;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = names.get(position);
        return title;
    }
}

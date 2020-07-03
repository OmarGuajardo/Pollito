package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int countTab;

    public PagerAdapter(@NonNull FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Followers followers = new Followers();
                return followers;
            case 1:
                Following following = new Following();
                return  following;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}

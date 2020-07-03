package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity  {
//public class ProfileActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
ActivityProfileBinding binding;
Toolbar toolbar;
ViewPager viewPager;
TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.mainToolbar);

//        tabLayout = binding.tabLayout;
//        viewPager = binding.viewPager;
//
//        tabLayout.addTab(tabLayout.newTab().setText("Following"));
//        tabLayout.addTab(tabLayout.newTab().setText("Followers"));
//
//
//        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
//
//        viewPager.setAdapter(pagerAdapter);
//
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//    }
//
//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
}}
package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

//public class ProfileActivity extends AppCompatActivity  {
public class ProfileActivity extends AppCompatActivity  {
ActivityProfileBinding binding;
MaterialToolbar toolbar;
ViewPager viewPager;
TabLayout tabLayout;
    TabItem followers;
    TabItem following;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

//        tabLayout.addTab(tabLayout.newTab().setText("Followind"));
//        tabLayout.addTab(tabLayout.newTab().setText("Followers"));
        tabLayout.bringToFront();
        followers = findViewById(R.id.tabItemFollower);
        following = findViewById(R.id.tabItemFollowing);




        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());


        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("ProfileActivity", "onTabSelected: asfasdf");
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}

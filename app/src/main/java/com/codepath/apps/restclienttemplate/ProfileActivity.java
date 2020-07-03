package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.parceler.Parcels;

//public class ProfileActivity extends AppCompatActivity  {
public class ProfileActivity extends AppCompatActivity {
    String TAG  = "ProfileActivity";
    MaterialToolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    Tweet tweet;
    ImageView ivProfileBanner;
    ImageView ivProfileImage;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        tweet = (Tweet)Parcels.unwrap(getIntent().getExtras().getParcelable("tweet"));

        ivProfileBanner = findViewById(R.id.main_backdrop);
        ivProfileImage = findViewById(R.id.ivProfileImageDetails);

        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileImageUrl())
                .fitCenter()
                .circleCrop()
                .into(ivProfileImage);

        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileBackgroundUrl())
                .fitCenter()
                .into(ivProfileBanner);
        getSupportActionBar().setTitle(tweet.getUser().getHandle());

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

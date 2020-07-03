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
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

//public class ProfileActivity extends AppCompatActivity  {
public class ProfileActivity extends AppCompatActivity {
    String TAG  = "ProfileActivity";
    MaterialToolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    Tweet tweet;
    ImageView ivProfileBanner;
    TwitterClient client;
    FollowAdapter followAdapter;
    ImageView ivProfileImage;
    TextView tvName;
    List<User> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient(getApplicationContext());
        getFollowers(tweet.getUser().getUserID());

        //Setting up the toolbar
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        //Unwrapping the contents of the tweet that was clicked on
        tweet = (Tweet)Parcels.unwrap(getIntent().getExtras().getParcelable("tweet"));

        //Referencing elements
        ivProfileBanner = findViewById(R.id.main_backdrop);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //Setting up the Tabs Adapters
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //If there is no background Image then the backdrop remains one color
        if(tweet.getUser().getProfileBackgroundUrl() != null){
        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileBackgroundUrl())
                .fitCenter()
                .into(ivProfileBanner);
        }
        //Setting the title and home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(tweet.getUser().getHandle());


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

    public void getFollowers(long userID){
        client.getFollowers(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "we got the followers!! " + json.toString());
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "we didn't get the followers "+response);
            }
        },userID);
    }

}

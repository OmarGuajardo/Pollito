package com.codepath.apps.restclienttemplate.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.FollowAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
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
    RecyclerView rvFollow;
    ImageView ivProfileImage;
    EndlessRecyclerViewScrollListener scrollListener;
    int followersPage = -1;
    int followingPage = -1;
    TextView tvName;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Unwrapping the contents of the tweet that was clicked on
        tweet = (Tweet)Parcels.unwrap(getIntent().getExtras().getParcelable("tweet"));

        //Setting up the toolbar
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        //
        client = TwitterApp.getRestClient(getApplicationContext());
        getFollowers(tweet.getUser().getUserID());
        users = new ArrayList<>();

        //Referencing elements
        ivProfileBanner = findViewById(R.id.main_backdrop);
        tabLayout = findViewById(R.id.tabLayout);
        rvFollow = findViewById(R.id.rvFollow);

        //Setting up the Tabs Adapters


        //If there is no background Image then the backdrop remains one color
        if(tweet.getUser().getProfileBackgroundUrl() != null){
        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileBackgroundUrl())
                .fitCenter()
                .into(ivProfileBanner);
        }
        //Setting the title and home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("@"+tweet.getUser().getHandle());

        //Recycler view setup: layout manager and the adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        followAdapter = new FollowAdapter(this, users);
        rvFollow.setLayoutManager(linearLayoutManager);
        rvFollow.setAdapter(followAdapter);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getFollowers(tweet.getUser().getUserID());
                        return;
                    case 1:
                        getFollowing(tweet.getUser().getUserID());
                        return;
                    default:
                        Log.d(TAG, "onTabSelected: click click");
                        return;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    public void getFollowers(long userID) {
        client.getFollowers(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    Log.d(TAG, "user size " + users.size());
                    users.clear();
                    users.addAll(User.fromJsonArray(jsonArray));
                    followAdapter.notifyDataSetChanged();
                    followersPage++;
                    Log.d(TAG, "user size " + users.size());
                    Log.d(TAG, "nothing went wrong here is the size of users " + users.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "we didn't get the followers " + response);
            }
        }, userID,followersPage);
    }
        public void getFollowing(long userID){
        client.getFollowing(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    Log.d(TAG, "user size " + users.size());
                    users.clear();
                    users.addAll(User.fromJsonArray(jsonArray));
                    followAdapter.notifyDataSetChanged();
                    followingPage++;
                    Log.d(TAG, "user size " + users.size());
                    Log.d(TAG, "nothing went wrong here is the size of users " + users.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "we didn't get the followers "+response);
            }
        },userID,followingPage);





    }




}

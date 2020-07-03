package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.onSubmitListener {

    public static final String KEY_ITEM_TWEET = "tweet";
    public static final int EDIT_TEXT_CODE = 20;



    public final String TAG = "TimelineActivity";
    TwitterClient client;
    TweetsAdapter tweetsAdapter;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    EndlessRecyclerViewScrollListener scrollListener;
    ComposeDialog composeDialog;
    ActivityTimelineBinding binding;

    DonutProgress donutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Find the toolbar view inside the activity layout
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Getting the Views
        rvTweets = findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();

        binding.rvTweets.setVisibility(View.INVISIBLE);

        //Method that will be passed down to the adapter to be able to reply
        TweetsAdapter.OnReplyListener onReplyListener = new TweetsAdapter.OnReplyListener() {
            @Override
            public void onReplyListener(long tweetID, String userHandle) {
                Log.d(TAG, "onReplyListener: " + tweetID + userHandle);
                openDialog(tweetID ,userHandle );
            }
        };

        TweetsAdapter.OnTweetClickedListener onTweetClickedListener  = new TweetsAdapter.OnTweetClickedListener(){
            @Override
            public void onTweetClickedListener(Tweet tweet,int position) {
                Intent intent = new Intent(TimelineActivity.this,TweetDetailsActivity.class);
                intent.putExtra("tweetObject", Parcels.wrap(tweet));

                startActivityForResult(intent,EDIT_TEXT_CODE);
            }
        };


        //Initializing the client
        client = TwitterApp.getRestClient(this);

        //Recycler view setup: layout manager and the adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tweetsAdapter = new TweetsAdapter(this, tweets,onReplyListener,onTweetClickedListener);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetsAdapter);


        //On click listener for the Compose FAB
        binding.fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        //Configuring the swipe down to refresh
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeLine();
            }
        });

        //Configure the refreshing colors
        binding.refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Fetching data and populating the timeline
        populateHomeTimeLine();

        //Making the endless scroll
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };


        //Adds the scroll listener to the Recycler View
        rvTweets.addOnScrollListener(scrollListener);


    }

    //This dialog method will be called by the onReply feature
    //will PASS the recipients information to the dialog
    public void openDialog(long tweetID, String userHandle) {
        //Creating an opening a new Dialog
        composeDialog = new ComposeDialog();
        Bundle args = new Bundle();
        args.putString("userHandle", userHandle);
        args.putLong("tweetID", tweetID);
        composeDialog.setArguments(args);
        composeDialog.show(getSupportFragmentManager(), "Compose Dialog");
    }

    //This dialog method will be called by the compose feature and WON'T pass
    //any recipients to the dialog
    public void openDialog() {
        //Creating an opening a new Dialog
        composeDialog = new ComposeDialog();
        composeDialog.show(getSupportFragmentManager(), "Compose Dialog");

    }

    public void loadMoreData() {

        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweetsReceived = Tweet.fromJsonArray(json.jsonArray);
                    Log.d(TAG, "home time line received " + json.toString());
                    tweetsAdapter.addAll(tweetsReceived);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onSuccess: for load More Data ");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: for load more data ", throwable);
            }
        }, tweets.get(tweets.size() - 1).getId());
    }

    private void populateHomeTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!");
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweetsReceived = Tweet.fromJsonArray(jsonArray);
                    Log.d(TAG, "home time line received " + json.toString());
                    tweetsAdapter.clear();
                    tweetsAdapter.addAll(tweetsReceived);
                    binding.refreshLayout.setRefreshing(false);
                    binding.progressCircular.setVisibility(View.INVISIBLE);
                    binding.rvTweets.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: ", throwable);
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //Method that handles the submission by the dialog screen
    @Override
    public void submitTweet(String body) {
        Log.d(TAG, "submitted the following tweet " + body);
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    Tweet newTweet = Tweet.fromJson(jsonObject);
                    tweets.add(0,newTweet);
                    tweetsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Successfully posted a tweet " + json.toString());

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failure to post tweet " + response,throwable );
            }
        },body);
        composeDialog.dismiss();
        Snackbar.make(binding.mainContent, R.string.snackbar_text, Snackbar.LENGTH_SHORT).show();

    }
    //Method that handles that takes in the compose body and make a POST request to the client
    @Override
    public void submitTweet(String body, long tweetID) {
        Log.d(TAG, "submitted the following tweet " + body);
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    Tweet newTweet = Tweet.fromJson(jsonObject);
                    tweets.add(0,newTweet);
                    tweetsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Successfully posted a tweet " + json.toString());
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failure to post tweet " + response,throwable );
            }
        },body,tweetID);
        composeDialog.dismiss();
        Snackbar.make(binding.mainContent, R.string.snackbar_text, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " "+resultCode);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            Tweet receivedTweet = Parcels.unwrap(data.getParcelableExtra("newTweet"));
            tweets.add(0,receivedTweet);
            tweetsAdapter.notifyDataSetChanged();
        }
    }

}
package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.onSubmitListener {

    public final String TAG = "TimelineActivity";
    TwitterClient client;
    TweetsAdapter tweetsAdapter;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    SwipeRefreshLayout swipeContainer;
    FloatingActionButton fabCompose;
    EndlessRecyclerViewScrollListener scrollListener;
    ComposeDialog composeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));

        //Getting the Views
        fabCompose = findViewById(R.id.fabCompose);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        rvTweets = findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();



        //Recycler view setup: layout manager and the adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tweetsAdapter = new TweetsAdapter(this, tweets);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetsAdapter);


        //Initializing the client
        client = TwitterApp.getRestClient(this);


        //On click listener for the Compose FAB
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }

            public void openDialog() {
                //Creating an opening a new Dialog
                composeDialog = new ComposeDialog();
                composeDialog.show(getSupportFragmentManager(), "Compose Dialog");
            }
        });


        //Configuring the swipe down to refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeLine();
            }
        });
//         Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
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

    public void loadMoreData() {

        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweetsReceived = Tweet.fromJsonArray(json.jsonArray);
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
                    tweetsAdapter.clear();
                    tweetsAdapter.addAll(tweetsReceived);
                    swipeContainer.setRefreshing(false);
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


    //Method that handles that takes in the compose body and make a POST request to the client
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
                composeDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failure to post tweet " + response,throwable );
            }
        },body);
    }
}
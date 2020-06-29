package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public final String TAG = "TimelineActivity";
    TwitterClient client;
    TweetsAdapter tweetsAdapter;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        //Get the Recycler View
        rvTweets = findViewById(R.id.rvTweets);

        //Initialize the list of tweets
        tweets = new ArrayList<>();

        //Recycler view setup: layout manager and the adapter
        tweetsAdapter = new TweetsAdapter(this,tweets);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);


        client = TwitterApp.getRestClient(this);

        populateHomeTimeLine();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);





    }

    private void populateHomeTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!");
                Log.d(TAG, "onSuccess: thi is the dat that we got "+json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.clear();
                    List<Tweet> tweetsReceived= Tweet.fromJsonArray(jsonArray);
                    tweets.addAll(tweetsReceived);
                    tweetsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception",e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: ",throwable);
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweetsReceived= Tweet.fromJsonArray(jsonArray);
                    // Remember to CLEAR OUT old items before appending in the new ones
                    tweetsAdapter.clear();
                    // ...the data has come back, add new items to your adapter...
                    tweetsAdapter.addAll(tweetsReceived);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                    Log.d(TAG, "onRefresh Success " + json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: ",throwable);

            }


        });
    }
}
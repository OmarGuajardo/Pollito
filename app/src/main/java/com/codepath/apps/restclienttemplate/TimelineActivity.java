package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

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


    }

    private void populateHomeTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!");
                Log.d(TAG, "onSuccess: thi is the dat that we got "+json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
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
}
package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;

public class TwitterUserFunctions {
    String TAG = "TwitterUserFunctions.java";
    Context context;
    Tweet tweet;
    TwitterClient client;

    public TwitterUserFunctions(Context context, Tweet tweet) {
        this.context = context;
        this.tweet = tweet;
        this.client = TwitterApp.getRestClient(context);
    }

    public void toggleFavorite(final ImageButton btn, final TextView tv){
        String action;
        final int toggleVal;
        action = (tweet.getFavorited() ? "destroy" : "create");
        toggleVal = (tweet.getFavorited() ? -1 : 1);
        client.favoriteTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                tweet.changeFavorite_count(toggleVal);
                tweet.toggleFavorited();
                btn.setSelected(tweet.getFavorited());
                tv.setText(String.valueOf(tweet.getFavorite_count()));
                Log.d(TAG, "You favorite/unfavorited a tweet sucess! ");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failure to favorite/unfavorite tweet " + response,throwable );
            }
        },tweet.getId(),action);

    }

    public void toggleReTweet(final ImageButton btn, final TextView tv){
        String action;
        final int toggleVal;
        action = (tweet.getRetweeted() ? "retweet" : "unretweet");
        toggleVal= (tweet.getRetweeted() ? -1 : 1);

        client.reTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "You retweeted/unretweeted a tweet sucess! ");
                tweet.changeRetweet_count(toggleVal);
                tweet.toggleRetweeted();
                tv.setText(String.valueOf(tweet.getRetweet_count()));
                btn.setSelected(tweet.getRetweeted());
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failure to retweet/unretweed a tweet " + response,throwable );
            }
        },tweet.getId(),action);
    }


}

package com.codepath.apps.restclienttemplate.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.ComposeDialog;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.TwitterUserFunctions;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity implements ComposeDialog.onSubmitListener {



    ActivityTweetDetailsBinding binding;
    TwitterUserFunctions twitterUserFunctions;
    ComposeDialog composeDialog;
    TwitterClient client;
    String TAG = "TweetDetailsActivity";
    Tweet tweet;

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTweetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tooolbarDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        client = TwitterApp.getRestClient(this);

        //Unwrapping the tweet
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweetObject"));
        Tweet originalTweet;

        //Checking to see if the tweet is a retweet
//        if(tweet.getAttachedReTweet() != null){
//            binding.tvRetweetStatus.setText("@"+tweet.getUser().getHandle());
//            originalTweet = tweet;
//            tweet = originalTweet.getAttachedReTweet();
//            binding.tvRetweetStatus.setVisibility(View.VISIBLE);
//        }
//        else{
//            binding.tvRetweetStatus.setVisibility(View.GONE);
//        }

        //If there is no image in the tweet ivTweetImage will be hidden
        if(!tweet.getTweetImageURL().isEmpty()){
            binding.ivTweetImage.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(tweet.getTweetImageURL())
                    .fitCenter()
                    .transform(new RoundedCornersTransformation(30, 10))
                    .into(binding.ivTweetImage);
        }
        else{
           binding.ivTweetImage.setVisibility(View.GONE);
        }

        //Initializing the twitterUserFunctions
        twitterUserFunctions = new TwitterUserFunctions(getApplicationContext(),tweet);

        binding.btnFavorite.setSelected(tweet.getFavorited());
        binding.btnReTweet.setSelected(tweet.getRetweeted());
        binding.tvTimesStamp.setText(tweet.getCreatedAt());
        binding.tvBody.setText(tweet.getBody());
        binding.tvHandle.setText("@"+tweet.getUser().getHandle());
        binding.tvName.setText(tweet.getUser().getName());
        binding.tvRetweetCounter.setText(String.valueOf(tweet.getRetweet_count()));
        binding.tvFavoriteCounter.setText(String.valueOf(tweet.getFavorite_count()));
        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileImageUrl())
                .circleCrop()
                .into(binding.ivProfileImage2);

        //TODO: make the buttons work
        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterUserFunctions.toggleFavorite(binding.btnFavorite,binding.tvFavoriteCounter);
            }
        });
        binding.btnReTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterUserFunctions.toggleReTweet(binding.btnReTweet,binding.tvRetweetCounter);
            }
        });

        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeDialog = new ComposeDialog();
                Bundle args = new Bundle();
                args.putString("userHandle", tweet.getUser().getHandle());
                args.putLong("tweetID", tweet.getId());
                composeDialog.setArguments(args);
                composeDialog.show(getSupportFragmentManager(), "Compose Dialog");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getSupportActionBar().hide();
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void submitTweet(String body) {

    }

    @Override
    public void submitTweet(String body, long ID) {
        Log.d(TAG, "submitted the following tweet " + body);
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    Tweet newTweet = Tweet.fromJson(jsonObject);

                    // create and intent to be able to put in the new tweet
                    Intent intent =  new Intent();

                    //pass the data
                    intent.putExtra("newTweet",Parcels.wrap(newTweet));
                    //set the result of the intent
                    setResult(RESULT_OK,intent);

                    //finish activity, close the screen and go back
                    finish();

                    composeDialog.dismiss();
                    Snackbar.make(binding.detailsActivityContainer, R.string.snackbar_text, Snackbar.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Successfully posted a tweet " + json.toString());
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failure to post tweet " + response,throwable );
            }
        },body,tweet.getId());

    }
}
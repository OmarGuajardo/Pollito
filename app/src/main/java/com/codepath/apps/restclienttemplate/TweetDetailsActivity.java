package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity {


    ActivityTweetDetailsBinding binding;
    TwitterUserFunctions twitterUserFunctions;
    String TAG = "TweetDetailsActivity";

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

        //Unwrapping the tweet
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweetObject"));
        Tweet originalTweet;

        //Checking to see if the tweet is a retweet
        if(tweet.getAttachedReTweet() != null){
            binding.tvRetweetStatus.setText("@"+tweet.getUser().getHandle());
            originalTweet = tweet;
            tweet = originalTweet.getAttachedReTweet();
            binding.tvRetweetStatus.setVisibility(View.VISIBLE);
        }
        else{
            binding.tvRetweetStatus.setVisibility(View.GONE);
        }

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
}
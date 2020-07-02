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
    String TAG = "TweetDetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTweetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweetObject"));
        Tweet originalTweet = new Tweet();

        setSupportActionBar(binding.tooolbarDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().hide();

        if(tweet.getAttachedReTweet() != null){
            binding.tvRetweetStatus.setText("@"+tweet.getUser().getHandle());
            originalTweet = tweet;
            tweet = originalTweet.getAttachedReTweet();
            binding.tvRetweetStatus.setVisibility(View.VISIBLE);
        }
        else{
            binding.tvRetweetStatus.setVisibility(View.GONE);
        }
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
        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileImageUrl())
                .circleCrop()
                .into(binding.ivProfileImage2);


        binding.tvTimesStamp.setText(tweet.getCreatedAt());
        binding.tvBody.setText(tweet.getBody());
        binding.tvHandle.setText("@"+tweet.getUser().getHandle());
        binding.tvName.setText(tweet.getUser().getName());
        binding.tvRetweetCounter.setText(String.valueOf(tweet.getRetweet_count()));
        binding.tvFavoriteCounter.setText(String.valueOf(tweet.getFavorite_count()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }
}
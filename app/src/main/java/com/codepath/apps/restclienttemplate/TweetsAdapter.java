package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.text.BoringLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public interface OnReplyListener{
        void onReplyListener(long tweetID, String userHandle);
    }

    private static final String TAG = "TweetsAdapter" ;
    Context context;
    List<Tweet> tweets;
    TwitterClient client;
    OnReplyListener replyListener;

    // Pass context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets, OnReplyListener onReplyListener) {
        this.context = context;
        this.tweets = tweets;
        this.client = TwitterApp.getRestClient(context);

        this.replyListener = onReplyListener;
    }

    // For each layout we inflate an item_tweet.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        return new ViewHolder(view);
    }

    // Bind values per row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data at position
        Tweet tweet = tweets.get(position);

        //bind the data to view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }







    // Define a ViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvHandle;
        TextView tvBody;
        TextView tvTimeStamp;
        TextView tvName;
        ImageButton btnFavorite;
        ImageButton btnReply;
        ImageButton btnReTweet;
        ImageView ivTweetImage;
        TextView tvRetweetCounter;
        TextView tvFavoriteCounter;
        Chip tvRetweetStatus;
        Toolbar toolbar;
        long tweetID;
        Tweet tweet;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            //Display Views
            tvTimeStamp = itemView.findViewById(R.id.tvTimesStamp);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage2);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            tvRetweetStatus = itemView.findViewById(R.id.tvRetweetStatus);


            //Button Views
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnReTweet = itemView.findViewById(R.id.btnReTweet);
            btnReply = itemView.findViewById(R.id.btnReply);

            //Counter
            tvRetweetCounter = itemView.findViewById(R.id.tvRetweetCounter);
            tvFavoriteCounter = itemView.findViewById(R.id.tvFavoriteCounter);

            toolbar = itemView.findViewById(R.id.toolbar);
            cardView = itemView.findViewById(R.id.materialCardView);

        }

        public void bind(final Tweet t) {
            if(t.getAttachedReTweet() != null) {
                tvRetweetStatus.setText("@"+t.getUser().getHandle());
                tweet = t.getAttachedReTweet();
                tvRetweetStatus.setVisibility(View.VISIBLE);

            } else  {
                tweet = t;
                tvRetweetStatus.setVisibility(View.GONE);
            }

            if(!tweet.getTweetImageURL().isEmpty()){
                Log.d(TAG, "this is the image that is retrieved from tweet " + tweet.getTweetImageURL());
                ivTweetImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.getTweetImageURL())
                        .fitCenter()
                        .transform(new RoundedCornersTransformation(30, 10))
                        .into(ivTweetImage);
            }
            else{
                ivTweetImage.setVisibility(View.GONE);
            }

            tvTimeStamp.setText(tweet.getCreatedAt());
            tvBody.setText(tweet.getBody());
            tvHandle.setText("@"+tweet.getUser().getHandle());
            tvName.setText(tweet.getUser().getName());
            tvRetweetCounter.setText(String.valueOf(tweet.getRetweet_count()));
            tvFavoriteCounter.setText(String.valueOf(tweet.getFavorite_count()));
            tweetID = tweet.getId();
            btnFavorite.setSelected(tweet.getFavorited());
            btnReTweet.setSelected(tweet.getRetweeted());



            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .circleCrop()
                    .into(ivProfileImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,TweetDetailsActivity.class);
                    intent.putExtra("tweetObject", Parcels.wrap(t));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, (View)itemView, "detailsCard");

                    context.startActivity(intent,options.toBundle());
                }
            });

            //On Click Listener that will like/unlike a specific tweet
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String action;
                    final int toggleVal;
                    action = (tweet.getFavorited() ? "destroy" : "create");
                    toggleVal = (tweet.getFavorited() ? -1 : 1);
                    client.favoriteTweet(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            tweet.changeFavorite_count(toggleVal);
                            tvFavoriteCounter.setText(String.valueOf(tweet.getFavorite_count()));
                            Log.d(TAG, "You favorite/unfavorited a tweet sucess! ");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Failure to favorite/unfavorite tweet " + response,throwable );
                        }
                    },tweetID,action);
                        tweet.toggleFavorited();
                        btnFavorite.setSelected(tweet.getFavorited());


                }
            });

            //On Click Listener that will retweet/unretweet a specific tweet
            btnReTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String action;
                    final int toggleVal;
                    action = (tweet.getRetweeted() ? "retweet" : "unretweet");
                    toggleVal= (tweet.getRetweeted() ? -1 : 1);

                    client.reTweet(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "You retweeted/unretweeted a tweet sucess! ");
                            tweet.changeRetweet_count(toggleVal);
                            tvRetweetCounter.setText(String.valueOf(tweet.getRetweet_count()));
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Failure to retweet/unretweed a tweet " + response,throwable );
                        }
                    },tweetID,action);

                    tweet.toggleRetweeted();
                    btnReTweet.setSelected(tweet.getRetweeted());

                }

            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: call a method in TimeLine Activity to start
                    // a dialog screen with some
                    replyListener.onReplyListener(tweetID,tweet.getUser().getHandle());
                }
            });





        }


    }

}

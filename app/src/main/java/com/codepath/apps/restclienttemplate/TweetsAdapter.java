package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Entity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    private static final String TAG = "TweetsAdapter" ;
    Context context;
    List<Tweet> tweets;
    TwitterClient client;

    // Pass context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets,TwitterClient client) {
        this.context = context;
        this.tweets = tweets;
        this.client = client;
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

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            //Display Views
            tvTimeStamp = itemView.findViewById(R.id.tvTimesStamp);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage2);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);

            //Button Views
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnReTweet = itemView.findViewById(R.id.btnReTweet);
            btnReply = itemView.findViewById(R.id.btnReply);

        }

        public void bind(final Tweet tweet) {
            tvTimeStamp.setText(tweet.getCreatedAt());
            tvBody.setText(tweet.getBody());
            tvHandle.setText(tweet.getUser().getHandle());
            tvName.setText(tweet.getUser().getName());

//            if(!tweet.getTweetImageURL().isEmpty()){
//                Log.d(TAG, "this is the image that is retrieved from tweet " + tweet.getTweetImageURL());
//                ivTweetImage.setVisibility(View.VISIBLE);
//                Glide.with(context)
//                        .load(tweet.getTweetImageURL())
//                        .circleCrop()
//                        .into(ivTweetImage);
//            }
            ivTweetImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load("http://pbs.twimg.com/media/EbukrZpUcAEfwed.jpg")
                    .centerCrop()
                    .into(ivTweetImage);

            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .circleCrop()
                    .into(ivProfileImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "tweet image url " + tweet.getTweetImageURL());
                }
            });
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    client.favoriteTweet(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "You favorited a tweet sucess! ");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Failure to favorite tweet " + response,throwable );
                        }
                    },tweet.getId());
                }
            });

        }
    }

}

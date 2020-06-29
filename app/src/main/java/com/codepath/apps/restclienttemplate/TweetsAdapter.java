package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    private static final String TAG = "TweetsAdapter" ;
    Context context;
    List<Tweet> tweets;
    // Pass context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
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
        Log.d(TAG, "onBindViewHolder: "+tweet.getBody());

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvBody = itemView.findViewById(R.id.tvBody);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.getBody());
            tvHandle.setText(tweet.getUser().getHandle());
            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .into(ivProfileImage);
        }
    }
}

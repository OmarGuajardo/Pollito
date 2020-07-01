package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public long id;


    public long getId() {
        return id;
    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        String ago = getRelativeTimeAgo(this.createdAt);
        ago = ago.replace("ago","");
        if(ago.contains("minutes")){
            ago = ago.replace("minutes","min");
            return ago;
        }if(ago.contains("minute")){
            ago = ago.replace("minute","min");
            return ago;
        }
        else if(ago.contains("hours")){
            ago = ago.replace("hours","hr");
            return ago;
        } else if(ago.contains("hour")){
            ago = ago.replace("hour","hr");
            return ago;
        }
        else if(ago.contains("seconds")){
            ago = ago.replace("seconds","s");
            return ago;
        } else if(ago.contains("second")){
            ago = ago.replace("second","s");
            return ago;
        }
        else if(ago.contains("days ago")){
           ago = ago.replace("days","d");
            return ago;
        }
        return ago;

    }

    public User getUser() {
        return user;
    }



    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}



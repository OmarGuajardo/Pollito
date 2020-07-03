package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, Tweet.id AS tweet_id" +
            ", Tweet.retweet_count AS tweet_retweet_count, Tweet.favorited" +
            " AS tweet_favorited, Tweet.retweeted as tweet_retweeted, Tweet.favorite_count AS tweet_favorite_count, Tweet.tweetImageURL AS tweet_tweetImageURL " +
            ", User.* FROM Tweet INNER JOIN User ON Tweet.userID = User.id ORDER BY Tweet.createdAt DESC LIMIT 5")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}

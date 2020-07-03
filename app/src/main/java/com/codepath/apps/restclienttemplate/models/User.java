package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class User {

    @ColumnInfo
    @PrimaryKey
    public long userID;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String handle;

    @ColumnInfo
    public String profileImageUrl;

    @ColumnInfo
    public String profileBackgroundUrl;





    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.userID = jsonObject.getLong("id");
        user.handle = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");

        try {
            user.profileBackgroundUrl = jsonObject.getString("profile_banner_url");
        } catch (JSONException e) {
            e.printStackTrace();
            user.profileBackgroundUrl = null;
        }
        return user;
    }
    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<User> localUserList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            User localUser = User.fromJson(jsonArray.getJSONObject(i));
            localUserList.add(localUser);
        }
        return localUserList;
    }

    public long getUserID() {
        return userID;
    }
    public String getName() {
        return name;
    }

    public String getHandle() {
        return handle;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public String getProfileBackgroundUrl() {
        return profileBackgroundUrl;
    }
}

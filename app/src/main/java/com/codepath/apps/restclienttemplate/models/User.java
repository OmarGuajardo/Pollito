package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public String handle;
    public String profileImageUrl;
    public String profileBackgroundUrl;
    public long userID;




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

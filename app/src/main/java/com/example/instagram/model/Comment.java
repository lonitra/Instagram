package com.example.instagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    private static final String KEY_POST_ID = "postId";
    private static final String KEY_TEXT = "text";
    private static final String KEY_USER = "user";

    public String getPostId() {
        return getString(KEY_POST_ID);
    }

    public void setPostId(String postId) {
        put(KEY_POST_ID, postId);
    }


    public void setText(String text) {
        put(KEY_TEXT, text);
    }

    public String getText() {
        return getString(KEY_TEXT);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }


}

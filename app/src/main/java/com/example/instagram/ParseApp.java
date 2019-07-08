package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu-instagram")
                .clientKey("ig-ltra")
                .server("http://lonitra-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}

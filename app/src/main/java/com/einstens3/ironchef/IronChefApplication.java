package com.einstens3.ironchef;

import android.app.Application;

import com.einstens3.ironchef.models.Like;
import com.einstens3.ironchef.models.Recipe;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

public class IronChefApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // -------------------------------------------
        //  Parse configuration
        // -------------------------------------------

        // Parse log level
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        // Register parse models
        ParseObject.registerSubclass(Recipe.class);
        ParseObject.registerSubclass(Like.class);
        // Initialize parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(null)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(BuildConfig.PARSE_SERVER_URL).build());
    }
}

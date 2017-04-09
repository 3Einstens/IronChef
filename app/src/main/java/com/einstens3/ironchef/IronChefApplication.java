package com.einstens3.ironchef;

import android.app.Application;

import com.parse.Parse;

public class IronChefApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Parse log level
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Register parse models
        //ParseObject.registerSubclass(User.class);
        //ParseObject.registerSubclass(Recipe.class);
        //ParseObject.registerSubclass(Direction.class);
        //ParseObject.registerSubclass(Review.class);

        // Initialize parse
    /*    Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(null)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(BuildConfig.PARSE_SERVER_URL).build());*/
    }
}

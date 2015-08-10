package com.app.testapp.application;

import com.facebook.FacebookSdk;

/**
 * Created by Thanh Nguyen on 8/10/2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
package com.example.shoaib.user;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/*
 * This class is used to access the service variables across the Application and also to start the service.
 * */
public class RecordApplication extends Application {

    private static RecordApplication application;

    /*
    * This function is used to set the base context for this ContextWrapper.
    * */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
    }

    /*
    * This function start the service when this class is first started
    * */
    @Override
    public void onCreate() {
        super.onCreate();
        // Start service
        startService(new Intent(this, RecordService.class));
    }

    // Return the object of type class.
    public static RecordApplication getInstance() {
        return application;
    }
}
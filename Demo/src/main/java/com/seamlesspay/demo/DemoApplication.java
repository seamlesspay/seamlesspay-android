package com.seamlesspay.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import java.lang.Thread.UncaughtExceptionHandler;
import retrofit.RestAdapter;

public class DemoApplication extends Application implements UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("Exception", "Uncaught Exception", ex);
        mDefaultExceptionHandler.uncaughtException(thread, ex);
    }
}

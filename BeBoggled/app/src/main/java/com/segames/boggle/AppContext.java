package com.segames.boggle;


import android.app.Application;
import android.content.Context;

public class AppContext extends Application
{
    private static AppContext mApp = null;
    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        mApp = this;
    }
    public static Context context()
    {
        return mApp.getApplicationContext();
    }
}


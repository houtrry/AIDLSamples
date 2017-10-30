package com.houtrry.aidlsamples;

import android.app.Application;
import android.util.Log;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: progressId: "+AndroidTools.getProcessId()+", progressName: "+AndroidTools.getProcessName(this));
    }
}

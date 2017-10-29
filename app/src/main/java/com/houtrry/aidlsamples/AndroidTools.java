package com.houtrry.aidlsamples;

import android.app.ActivityManager;
import android.content.Context;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public class AndroidTools {

    /**
     * 获取当前进程Id
     *
     * @return 当前进程Id
     */
    public static int getProgressId() {
        return android.os.Process.myPid();
    }

    /**
     * 获取当前进程名称
     *
     * @param context 上下文
     * @return 当前进程名称
     */
    public static String getProgressName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}

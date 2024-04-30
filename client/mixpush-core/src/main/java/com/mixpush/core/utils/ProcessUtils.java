package com.mixpush.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

public class ProcessUtils {

    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getProcessName(context));
    }

    /**
     * 获取当前进程名
     */
    public static String getProcessName(Context context) {
        int pid = Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }
}

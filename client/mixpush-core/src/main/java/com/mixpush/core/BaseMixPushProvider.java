package com.mixpush.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public abstract class BaseMixPushProvider {
    public abstract void register(Context context, RegisterType type);

    public abstract void unRegister(Context context);

    public abstract String getRegisterId(Context context);

    /**
     * @return 推送SDK是否支持当前设备
     */
    public abstract boolean isSupport(Context context);


    protected String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = appInfo.metaData.getString(name);
            if (value != null) {
                return value.toString().replace("UNIFIEDPUSH-", "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract String getPlatformName();
}
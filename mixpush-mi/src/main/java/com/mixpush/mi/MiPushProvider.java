package com.mixpush.mi;

import android.content.Context;

import com.mixpush.core.BaseMixPushProvider;
import com.mixpush.core.RegisterType;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

public class MiPushProvider extends BaseMixPushProvider {
    public static final String MI = "mi";
    public static String TAG = "MiPushProvider";
    MixPushHandler handler = MixPushClient.getInstance().getHandler();
    static RegisterType registerType;

    @Override
    public void register(Context context, RegisterType type) {
        MiPushProvider.registerType = type;
        String appId = getMetaData(context, "MI_APP_ID");
        String appKey = getMetaData(context, "MI_APP_KEY");
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable throwable) {
                handler.getLogger().log(TAG, content, throwable);
            }

            @Override
            public void log(String content) {
                handler.getLogger().log(TAG, content);
            }
        };
        Logger.setLogger(context, newLogger);
        MiPushClient.registerPush(context.getApplicationContext(), appId, appKey);
    }

    @Override
    public void unRegister(Context context) {
        MiPushClient.unregisterPush(context.getApplicationContext());
    }

    @Override
    public String getRegisterId(Context context) {
        return MiPushClient.getRegId(context);
    }


    @Override
    public boolean isSupport(Context context) {
        return true;
    }

//    @Override
//    public void setAlias(Context context, String alias) {
//        MiPushClient.setAlias(context, alias, null);
//    }

    @Override
    public String getPlatformName() {
        return MiPushProvider.MI;
    }
}

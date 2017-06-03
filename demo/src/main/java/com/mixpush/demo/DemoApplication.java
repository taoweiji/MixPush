package com.mixpush.demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;

import com.mixpush.client.core.MixPushClient;
import com.mixpush.client.core.MixPushManager;
import com.mixpush.client.getui.GeTuiManager;
import com.mixpush.client.meizu.MeizuPushManager;
import com.mixpush.client.mipush.MiPushManager;

import java.util.Map;

/**
 * Created by Wiki on 2017/6/1.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }

    private void initPush() {
        MixPushClient.addPushAdapter(new MeizuPushManager("110697", "b39d231fc8a14043a556f56881f56e3b"));
        MixPushClient.addPushAdapter(new MiPushManager("2882303761517582575", "5841758274575"));
        MixPushClient.addPushAdapter(new GeTuiManager());
        MixPushClient.setPushIntentService(PushIntentService.class);
        MixPushClient.setSelector(new MixPushClient.Selector() {
            @Override
            public String select(Map<String, MixPushManager> pushAdapterMap, String brand) {
                // TODO 底层已经做了小米推送、魅族推送、个推判断，也可以按照自己的需求来实现
//                return GeTuiManager.NAME;
                return super.select(pushAdapterMap, brand);
            }
        });

        // start - 下面代码在正式使用不用设置，这里仅仅用于测试
        String usePushName = getUsePushName(this);
        if (usePushName != null) {
            MixPushClient.setUsePushName(usePushName);
        }
        // - end

        MixPushClient.registerPush(this);

        // 绑定别名，以方便推送
        MixPushClient.bindAlias(this, "100");
    }


    public static String getUsePushName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("usePushName", null);
    }

    public static void setUsePushName(Context context, String usePushName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("usePushName", usePushName).commit();
//        throw new RuntimeException();
        Process.killProcess(Process.myPid());
    }
}

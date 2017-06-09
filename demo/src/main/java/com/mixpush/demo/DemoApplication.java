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
    public static final String MEIZU_APP_ID = "110697";
    public static final String MEIZU_APP_KEY = "b39d231fc8a14043a556f56881f56e3b";
    public static final String MIPUSH_APP_ID = "2882303761517582575";
    public static final String MIPUSH_APP_KEY = "5841758274575";

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }
    private void initPush() {
        MixPushClient.addPushManager(new MeizuPushManager(MEIZU_APP_ID,MEIZU_APP_KEY));
        MixPushClient.addPushManager(new MiPushManager(MIPUSH_APP_ID, MIPUSH_APP_KEY));
        MixPushClient.addPushManager(new GeTuiManager());
        MixPushClient.setPushIntentService(PushIntentService.class);
        MixPushClient.setSelector(new MixPushClient.Selector() {
            @Override
            public String select(Map<String, MixPushManager> pushAdapterMap, String brand) {
                // return GeTuiManager.NAME;
                //底层已经做了小米推送、魅族推送、个推判断，也可以按照自己的需求来选择推送
                return super.select(pushAdapterMap, brand);
            }
        });
        // 配置接收推送消息的服务类
        MixPushClient.setPushIntentService(PushIntentService.class);

        // start - 下面代码在正式使用不用设置，这里仅仅用于测试
        String usePushName = getUsePushName(this);
        if (usePushName != null) {
            MixPushClient.setUsePushName(usePushName);
        }
        // - end


        // 注册推送
        MixPushClient.registerPush(this);
        // 绑定别名，一般是填写用户的ID，便于定向推送
        MixPushClient.setAlias(this, getUserId());
        // 设置标签
        MixPushClient.setTags(this,"广东");
    }
    private String getUserId(){
        return "103";
    }


    public static String getUsePushName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("usePushName", null);
    }

    public static void setUsePushName(Context context, String usePushName) {
        MixPushClient.unsetAlias(context,"103");
        MixPushClient.unRegisterPush(context);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("usePushName", usePushName).commit();
//        throw new RuntimeException();
        Process.killProcess(Process.myPid());
    }
}

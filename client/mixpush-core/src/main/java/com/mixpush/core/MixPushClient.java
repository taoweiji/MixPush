package com.mixpush.core;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.mixpush.core.utils.ProcessUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MixPushClient {
    public static String TAG = "MixPush";
    public static final String MI = "mi";
    public static boolean debug = true;

    protected Map<String, BaseMixPushProvider> pushManagerMap = new HashMap<>();

    protected static volatile MixPushClient mixPushClient;
    protected MixPushHandler handler = new MixPushHandler();
    protected BaseMixPushProvider notificationPushProvider;
    protected BaseMixPushProvider passThroughPushProvider;


    public static MixPushClient getInstance() {
        if (mixPushClient == null) {
            synchronized (MixPushClient.class) {
                if (mixPushClient == null) {
                    mixPushClient = new MixPushClient();
                }
            }
        }
        return mixPushClient;
    }

    public MixPushClient() {

    }

    public void addPlatformProvider(BaseMixPushProvider provider) {
        String platformName = provider.getPlatformName();
        if (pushManagerMap.containsKey(platformName)) {
            return;
        }
        pushManagerMap.put(platformName, provider);
    }

    protected void addPlatformProviderByClassName(String className) {
        try {
            Class<?> pushManager = Class.forName(className);
            addPlatformProvider((BaseMixPushProvider) pushManager.newInstance());
        } catch (Exception e) {
            handler.getLogger().log(TAG, "addPlatformProviderByClassName", e);
        }
    }

    /**
     * 默认初始化方式
     * 1. 根据用户的手机型号优先注册厂家的推送平台。
     * 2. 不支持手机厂商推送平台的手机使用小米推送。
     * 3. 全平台使用小米实现透传功能。
     */
    public void register(Context context) {
        register(context, MI, null);
    }

    public void register(Context context, String defaultPlatform) {
        register(context, defaultPlatform, null);
    }

    /**
     * @param defaultPlatform 默认的推送平台
     */
    public void register(Context context, String defaultPlatform, String passThroughPlatform) {
        if (!ProcessUtils.isMainProcess(context)) {
            handler.getLogger().log(TAG, "只允许在主进程初始化");
            return;
        }
        addPlatformProviderByClassName("com.mixpush.mi.MiPushProvider");
        addPlatformProviderByClassName("com.mixpush.meizu.MeizuPushProvider");
        addPlatformProviderByClassName("com.mixpush.huawei.HuaweiPushProvider");
        addPlatformProviderByClassName("com.mixpush.oppo.OppoPushProvider");
        addPlatformProviderByClassName("com.mixpush.vivo.VivoPushProvider");

        BaseMixPushProvider pushProvider = null;
        // 获取厂商推送
        Set<String> keys = pushManagerMap.keySet();
        for (String key : keys) {
            // 除开默认的推送
            if (!key.equals(defaultPlatform)) {
                BaseMixPushProvider tmp = pushManagerMap.get(key);
                if (tmp != null && tmp.isSupport(context)) {
                    pushProvider = tmp;
                }
            }
        }
        BaseMixPushProvider defaultProvider = pushManagerMap.get(defaultPlatform);
        if (defaultProvider == null) {
            handler.getLogger().log(TAG, "no support push sdk", new Exception("no support push sdk"));
            return;
        }
        if (pushProvider == null) {
            handler.getLogger().log(TAG, "register all " + defaultProvider.getPlatformName());
            if (defaultPlatform.equals(passThroughPlatform)) {
                defaultProvider.register(context, RegisterType.all);
                passThroughPushProvider = defaultProvider;
            } else {
                defaultProvider.register(context, RegisterType.notification);
            }
            notificationPushProvider = defaultProvider;
        } else {
            handler.getLogger().log(TAG, "register notification " + pushProvider.getPlatformName());
            pushProvider.register(context, RegisterType.notification);
            notificationPushProvider = pushProvider;
        }
        if (passThroughPushProvider == null && passThroughPlatform != null) {
            passThroughPushProvider = pushManagerMap.get(passThroughPlatform);
            handler.getLogger().log(TAG, "register passThrough " + passThroughPushProvider.getPlatformName());
            passThroughPushProvider.register(context, RegisterType.passThrough);
        }
    }

    public void setLogger(MixPushLogger logger) {
        handler.callLogger = logger;
    }

    public void setPushReceiver(MixPushReceiver receiver) {
        handler.callPushReceiver = receiver;
    }

    public void setPassThroughReceiver(MixPushPassThroughReceiver receiver) {
        handler.callPassThroughReceiver = receiver;
    }

    public MixPushHandler getHandler() {
        return handler;
    }

    /**
     * 60 秒超时
     */
    public void getRegisterId(Context context, final GetRegisterIdCallback callback) {
        getRegisterId(context, callback, false);
    }

    /**
     * 60 秒超时
     */
    public void getRegisterId(Context context, final GetRegisterIdCallback callback, boolean isPassThrough) {
        final Context appContext = context.getApplicationContext();
        Handler handler = new Handler(Looper.getMainLooper());
        long startTime = System.currentTimeMillis();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (Math.abs(System.currentTimeMillis() - startTime) > 60000) {
                    callback.callback(null);
                    getHandler().getLogger().log("getRegisterId", "超时");
                    return;
                }
                final BaseMixPushProvider pushProvider;
                if (isPassThrough) {
                    pushProvider = passThroughPushProvider;
                } else {
                    pushProvider = notificationPushProvider;
                }
                if (pushProvider == null) {
                    handler.postDelayed(this, 2000);
                    getHandler().getLogger().log("getRegisterId", "pushProvider == null");
                    return;
                }
                if (isPassThrough) {
                    if (DefaultPassThroughReceiver.passThroughPlatform != null) {
                        callback.callback(DefaultPassThroughReceiver.passThroughPlatform);
                        return;
                    }
                } else {
                    if (DefaultMixPushReceiver.notificationPlatform != null) {
                        callback.callback(DefaultMixPushReceiver.notificationPlatform);
                        return;
                    }
                }
                String regId = pushProvider.getRegisterId(appContext);
                if (regId != null && !regId.isEmpty()) {
                    callback.callback(new MixPushPlatform(pushProvider.getPlatformName(), regId));
                    return;
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(runnable);
    }

    public void openApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        context.startActivity(intent);
    }

}

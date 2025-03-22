package com.mixpush.core;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class MixPushHandler {
    private final MixPushLogger logger;
    final DefaultMixPushReceiver pushReceiver;
    final DefaultPassThroughReceiver passThroughReceiver;
    public MixPushLogger callLogger;
    public MixPushReceiver callPushReceiver;
    public MixPushPassThroughReceiver callPassThroughReceiver;

    public MixPushHandler() {
        logger = new DefaultMixPushLogger(this);
        pushReceiver = new DefaultMixPushReceiver(this, logger);
        passThroughReceiver = new DefaultPassThroughReceiver(this, logger);
    }

    public MixPushLogger getLogger() {
        return logger;
    }

    public MixPushReceiver getPushReceiver() {
        return pushReceiver;
    }

    public MixPushPassThroughReceiver getPassThroughReceiver() {
        return passThroughReceiver;
    }
}

class DefaultPassThroughReceiver implements MixPushPassThroughReceiver {
    private final MixPushLogger logger;
    private MixPushHandler handler;
    public static String TAG = "MixPush";
    static MixPushPlatform passThroughPlatform = null;

    public DefaultPassThroughReceiver(MixPushHandler handler, MixPushLogger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    @Override
    public void onRegisterSucceed(final Context context, final MixPushPlatform pushPlatform) {
        if (passThroughPlatform != null) {
            logger.log(TAG, "已经响应onRegisterSucceed,不再重复调用");
            return;
        }
        passThroughPlatform = pushPlatform;
        logger.log(TAG, "onRegisterSucceed " + pushPlatform.toString());
        if (handler.callPassThroughReceiver == null) {
            Exception exception = new Exception("必须要在 register() 之前实现 setPassThroughReceiver()");
            logger.log(TAG, exception.getMessage(), exception);
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            // 在异步进程回调,避免阻塞主进程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.callPassThroughReceiver.onRegisterSucceed(context, pushPlatform);
                }
            }).start();
        } else {
            handler.callPassThroughReceiver.onRegisterSucceed(context, pushPlatform);
        }
    }

    @Override
    public void onReceiveMessage(Context context, MixPushMessage message) {
        logger.log(TAG, "PassThroughReceiver.onReceiveMessage " + message.toString());
        if (handler.callPassThroughReceiver == null) {
            logger.log(TAG, "你必须设置 setPassThroughReceiver() 才能正常工作");
            return;
        }
        handler.callPassThroughReceiver.onReceiveMessage(context, message);
    }
}

class DefaultMixPushReceiver extends MixPushReceiver {
    private final MixPushLogger logger;
    private MixPushHandler handler;
    public static String TAG = "MixPush";
    static MixPushPlatform notificationPlatform = null;

    public DefaultMixPushReceiver(MixPushHandler handler, MixPushLogger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    @Override
    public void onRegisterSucceed(final Context context, final MixPushPlatform platform) {
        if (platform == null) {
            return;
        }
        if (notificationPlatform != null) {
            logger.log(TAG, "已经响应 onRegisterSucceed，不再重复调用");
            return;
        }
        notificationPlatform = platform;
        logger.log(TAG, "onRegisterSucceed " + platform.toString());
        if (handler.callPushReceiver == null) {
            Exception exception = new Exception("必须要在 register() 之前注册 setPushReceiver()");
            logger.log(TAG, exception.getMessage(), exception);
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            // 在异步进程回调,避免阻塞主进程
            new Thread(() -> handler.callPushReceiver.onRegisterSucceed(context, platform)).start();
        } else {
            handler.callPushReceiver.onRegisterSucceed(context, platform);
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MixPushMessage message) {
        logger.log(TAG, "onNotificationMessageClicked " + message.toString());
        if (handler.callPushReceiver == null) {
            Exception exception = new Exception("必须设置 setPushReceiver() 才能正常工作");
            logger.log(TAG, exception.getMessage(), exception);
            return;
        }
        if (message.getPayload() == null || message.getPayload().length() < 5) {
            MixPushClient.getInstance().openApp(context);
            handler.callPushReceiver.openAppCallback(context);
        } else {
            handler.callPushReceiver.onNotificationMessageClicked(context, message);
        }
    }


    @Override
    public void onNotificationMessageArrived(Context context, MixPushMessage message) {
        logger.log(TAG, "onNotificationMessageArrived " + message.toString());
        if (handler.callPushReceiver == null) {
            Exception exception = new Exception("必须设置 setPushReceiver() 才能正常工作");
            logger.log(TAG, exception.getMessage(), exception);
            return;
        }
        handler.callPushReceiver.onNotificationMessageArrived(context, message);
    }
}

class DefaultMixPushLogger implements MixPushLogger {

    private MixPushHandler handler;

    public DefaultMixPushLogger(MixPushHandler handler) {
        this.handler = handler;
    }

    @Override
    public void log(String tag, String content, Throwable throwable) {
        if (!tag.contains(MixPushClient.TAG)) {
            tag = MixPushClient.TAG + "-" + tag;
        }
        if (handler.callLogger != null) {
            handler.callLogger.log(tag, content, throwable);
        } else if (MixPushClient.debug) {
            Log.e(tag, content);
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void log(String tag, String content) {
        if (!tag.contains(MixPushClient.TAG)) {
            tag = MixPushClient.TAG + "-" + tag;
        }
        if (handler.callLogger != null) {
            handler.callLogger.log(tag, content);
        } else if (MixPushClient.debug) {
            Log.e(tag, content);
        }
    }
}

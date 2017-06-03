package com.mixpush.client.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 透传服务类
 */
public abstract class MixPushMessageReceiver extends BroadcastReceiver {

    public static final String MESSAGE = "message";
    /**
     * 收到透传消息
     */
    public static final String RECEIVE_THROUGH_MESSAGE = "com.mixpush.RECEIVE_THROUGH_MESSAGE";
    /**
     * 通知栏消息到达
     */
    public static final String NOTIFICATION_ARRIVED = "com.mixpush.NOTIFICATION_ARRIVED";
    /**
     * 通知栏消息被点击
     */
    public static final String NOTIFICATION_CLICKED = "com.mixpush.NOTIFICATION_CLICKED";
    /**
     * 出现异常
     */
    public static final String ERROR = "com.mixpush.ERROR";

    /**
     * 用来备用，用户自定义处理一些事情
     */
    public void onReceive2(Context context, Intent intent) {

    }
    @Override
    public final void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            try {
                MixPushMessage message = (MixPushMessage) intent.getSerializableExtra(MESSAGE);
                if (RECEIVE_THROUGH_MESSAGE.equals(action)) {
                    onReceivePassThroughMessage(context,message);
                } else if (NOTIFICATION_ARRIVED.equals(action)) {
                    onNotificationMessageArrived(context,message);
                } else if (NOTIFICATION_CLICKED.equals(action)) {
                    onNotificationMessageClicked(context,message);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        onReceive2(context, intent);
    }

    /**
     * 透传
     */
    public abstract void onReceivePassThroughMessage(Context context, MixPushMessage message);

    /**
     * 通知栏消息点击
     */
    public abstract void onNotificationMessageClicked(Context context, MixPushMessage message);

    /**
     * 通知栏消息到达,
     * flyme6基于android6.0以上不再回调，
     * MIUI基于小米推送，在APP被杀死不会回调，
     * 在个推不会回调，所以不建议使用，
     */
    @Deprecated
    public void onNotificationMessageArrived(Context context, MixPushMessage message) {

    }
}
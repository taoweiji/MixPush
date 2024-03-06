package com.mixpush.meizu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushMessage;


/**
 * Created by liaojinlong on 15-6-28.
 */
public class MeizuPushMsgReceiver extends MzPushMessageReceiver {

    private static final String TAG = MeizuPushProvider.MEIZU;
    private int mipush_notification;
    private int mipush_small_notification;
    MixPushHandler handler = MixPushClient.getInstance().getHandler();

    @Override
    @Deprecated
    public void onRegister(Context context, String pushId) {
        Log.i(TAG, "onRegister " + pushId);
        MixPushPlatform mixPushPlatform = new MixPushPlatform(MeizuPushProvider.MEIZU, pushId);
        handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        mipush_notification = context.getResources().getIdentifier("mipush_notification", "drawable", context.getPackageName());
        mipush_small_notification = context.getResources().getIdentifier("mipush_small_notification", "drawable", context.getPackageName());
        super.onReceive(context, intent);
    }

    @Override
    public void onMessage(Context context, String content) {
        //接收服务器推送的透传消息
        MixPushMessage message = new MixPushMessage();
        message.setPlatform(MeizuPushProvider.MEIZU);
        message.setPayload(content);
        message.setPassThrough(true);
        handler.getPassThroughReceiver().onReceiveMessage(context, message);
    }

    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
        //调用PushManager.unRegister(context）方法后，会在此回调反注册状态
    }

    //设置通知栏小图标
    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        if (mipush_notification > 0) {
            pushNotificationBuilder.setLargeIcon(mipush_notification);
            Log.d(TAG, "设置通知栏大图标");
        } else {
            Log.e(TAG, "缺少drawable/mipush_notification.png");
        }
        if (mipush_small_notification > 0) {
            pushNotificationBuilder.setStatusBarIcon(mipush_small_notification);
            Log.d(TAG, "设置通知栏小图标");
            Log.e(TAG, "缺少drawable/mipush_small_notification.png");
        }
    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
        //检查通知栏和透传消息开关状态回调
    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        Log.i(TAG, "onRegisterStatus " + registerStatus);
        MixPushPlatform mixPushPlatform = new MixPushPlatform(MeizuPushProvider.MEIZU, registerStatus.getPushId());
        handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
        Log.i(TAG, "onUnRegisterStatus " + unRegisterStatus);
        //新版反订阅回调
    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
        Log.i(TAG, "onSubTagsStatus " + subTagsStatus);
        //标签回调
    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
        Log.i(TAG, "onSubAliasStatus " + subAliasStatus);
        //别名回调
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationClicked(context, mzPushMessage);
        MixPushMessage message = new MixPushMessage();
        message.setPlatform(MeizuPushProvider.MEIZU);
//        message.setMsgId(mzPushMessage.getTaskId());
        message.setTitle(mzPushMessage.getTitle());
        message.setDescription(mzPushMessage.getContent());
        message.setPayload(mzPushMessage.getSelfDefineContentString());
        handler.getPushReceiver().onNotificationMessageClicked(context, message);
    }

    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
        MixPushMessage message = new MixPushMessage();
        message.setPlatform(MeizuPushProvider.MEIZU);
//        message.setMsgId(mzPushMessage.getTaskId());
        message.setTitle(mzPushMessage.getTitle());
        message.setDescription(mzPushMessage.getContent());
        message.setPayload(mzPushMessage.getSelfDefineContentString());
        handler.getPushReceiver().onNotificationMessageArrived(context, message);
    }
}
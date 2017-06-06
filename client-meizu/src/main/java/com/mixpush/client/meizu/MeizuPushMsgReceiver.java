package com.mixpush.client.meizu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meizu.cloud.pushinternal.DebugLogger;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.mixpush.client.core.MixPushMessage;


/**
 * Created by liaojinlong on 15-6-28.
 */
public class MeizuPushMsgReceiver extends MzPushMessageReceiver {
    private static final String TAG = "MeizuPushMsgReceiver";
    private int mipush_notification;
    private int mipush_small_notification;

    @Override
    @Deprecated
    public void onRegister(Context context, String pushid) {
        //应用在接受返回的pushid

    }

    @Override
    public void onHandleIntent(Context context, Intent intent) {
        mipush_notification = context.getResources().getIdentifier("mipush_notification", "drawable", context.getPackageName());
        mipush_small_notification = context.getResources().getIdentifier("mipush_small_notification", "drawable", context.getPackageName());


        super.onHandleIntent(context, intent);
    }

    @Override
    public void onMessage(Context context, String s) {
        //接收服务器推送的透传消息
        if (MeizuPushManager.sMixMessageProvider != null){
            MixPushMessage message = new MixPushMessage();
            message.setContent(s);
            message.setPlatform(MeizuPushManager.NAME);
            MeizuPushManager.sMixMessageProvider.onReceivePassThroughMessage(context,message);
        }
    }

    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
        //调用PushManager.unRegister(context）方法后，会在此回调反注册状态
    }

    //设置通知栏小图标
    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        if (mipush_notification > 0){
            pushNotificationBuilder.setmLargIcon(mipush_notification);
            Log.d(TAG,"设置通知栏大图标");
        }else {
            Log.e(TAG,"缺少drawable/mipush_notification.png");
        }
        if (mipush_small_notification > 0){
            pushNotificationBuilder.setmStatusbarIcon(mipush_small_notification);
            Log.d(TAG,"设置通知栏小图标");
            Log.e(TAG,"缺少drawable/mipush_small_notification.png");
        }
    }

    @Override
    public void onPushStatus(Context context,PushSwitchStatus pushSwitchStatus) {
        //检查通知栏和透传消息开关状态回调
    }

    @Override
    public void onRegisterStatus(Context context,RegisterStatus registerStatus) {
        Log.i(TAG, "onRegisterStatus " + registerStatus);
        //新版订阅回调
    }

    @Override
    public void onUnRegisterStatus(Context context,UnRegisterStatus unRegisterStatus) {
        Log.i(TAG,"onUnRegisterStatus "+unRegisterStatus);
        //新版反订阅回调
    }

    @Override
    public void onSubTagsStatus(Context context,SubTagsStatus subTagsStatus) {
        Log.i(TAG, "onSubTagsStatus " + subTagsStatus);
        //标签回调
    }

    @Override
    public void onSubAliasStatus(Context context,SubAliasStatus subAliasStatus) {
        Log.i(TAG, "onSubAliasStatus " + subAliasStatus);
        //别名回调
    }
    @Override
    public void onNotificationArrived(Context context, String title, String content, String selfDefineContentString) {
        //通知栏消息到达回调，flyme6基于android6.0以上不再回调
        DebugLogger.i(TAG,"onNotificationArrived title "+title + "content "+content + " selfDefineContentString "+selfDefineContentString);

        if (MeizuPushManager.sMixMessageProvider != null){
            MixPushMessage message = new MixPushMessage();
            message.setPlatform(MeizuPushManager.NAME);
            message.setTitle(title);
            message.setDescription(content);
            message.setContent(selfDefineContentString);
            MeizuPushManager.sMixMessageProvider.onNotificationMessageArrived(context,message);
        }
    }

    @Override
    public void onNotificationClicked(Context context, String title, String content, String selfDefineContentString) {
        //通知栏消息点击回调
        DebugLogger.i(TAG,"onNotificationClicked title "+title + "content "+content + " selfDefineContentString "+selfDefineContentString);

        if (MeizuPushManager.sMixMessageProvider != null){
            MixPushMessage message = new MixPushMessage();
            message.setPlatform(MeizuPushManager.NAME);
            message.setTitle(title);
            message.setDescription(content);
            message.setContent(selfDefineContentString);
            MeizuPushManager.sMixMessageProvider.onNotificationMessageClicked(context,message);
        }
    }

    @Override
    public void onNotificationDeleted(Context context, String title, String content, String selfDefineContentString) {
        //通知栏消息删除回调；flyme6基于android6.0以上不再回调
        DebugLogger.i(TAG,"onNotificationDeleted title "+title + "content "+content + " selfDefineContentString "+selfDefineContentString);
    }

}
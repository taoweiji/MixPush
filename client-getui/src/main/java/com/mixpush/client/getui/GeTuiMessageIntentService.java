package com.mixpush.client.getui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.mixpush.client.core.MixPushMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class GeTuiMessageIntentService extends GTIntentService {

    public GeTuiMessageIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        if (msg.getPayload() != null) {
            Log.e(TAG, "onReceiveMessageData -> " + "payload = " + new String(msg.getPayload()));
        } else {
            Log.e(TAG, "onReceiveMessageData -> " + "payload=null");
        }
        if (msg.getPayload() != null) {
            String data = new String(msg.getPayload());
            Log.d("GetuiSdkDemo", "receiver payload : " + data);
            try {
                JSONObject jsonObject = new JSONObject(data);
                if (jsonObject.optInt("notify", 0) == 1) {
                    showNotify(context, jsonObject);
                    if (GeTuiManager.sMixMessageProvider != null) {
                        GeTuiManager.sMixMessageProvider.onNotificationMessageArrived(context, new MixPushMessage(data));
                    }
                } else {
                    if (GeTuiManager.sMixMessageProvider != null) {
                        GeTuiManager.sMixMessageProvider.onReceivePassThroughMessage(context, new MixPushMessage(data));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showNotify(Context context, JSONObject data) {
        Log.e(TAG, "showNotify -> " + "data = " + data);
        String title = data.optString("title");
        String description = data.optString("description");

        data.remove("notify");
        data.remove("title");
        data.remove("description");

        //全局通知管理者，通过获取系统服务获取
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//通知栏构造器,创建通知栏样式
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
// 将来意图，用于点击通知之后的操作,内部的new intent()可用于跳转等操作
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context,0,new Intent().putExtra("payload",data.toString()),0);


        //设置通知栏标题

        mBuilder.setContentTitle(title)
                //设置通知栏显示内容
                .setContentText(description)
                //设置通知栏点击意图
                .setContentIntent(mPendingIntent)
                //通知首次出现在通知栏，带上升动画效果的
                .setTicker(title)
                //通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setWhen(System.currentTimeMillis())
                //设置该通知优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //设置这个标志当用户单击面板就可以让通知将自动取消
                .setAutoCancel(true)
                //使用当前的用户默认设置
                .setDefaults(Notification.DEFAULT_VIBRATE)
                //设置通知小ICON(应用默认图标)
                .setSmallIcon(R.drawable.ic_launcher);
        mNotificationManager.notify(1234, mBuilder.build());
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e(TAG, "onReceiveCommandResult -> " + "action = " + cmdMessage.getAction());
    }
}
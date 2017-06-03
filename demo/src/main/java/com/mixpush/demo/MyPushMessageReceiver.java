package com.mixpush.demo;

import android.content.Context;
import android.util.Log;

import com.mixpush.client.core.MixPushMessage;
import com.mixpush.client.core.MixPushMessageReceiver;

/**
 * 需要定义一个receiver 或 Service 来接收透传和通知栏点击的信息，建议使用Service，更加简单
 * Created by Wiki on 2017/6/1.
 */

public class MyPushMessageReceiver extends MixPushMessageReceiver {

    private static final String TAG = "MyPushMessageReceiver";

    @Override
    public void onReceivePassThroughMessage(Context context, MixPushMessage message) {
        Log.d(TAG,"收到透传消息 -> "+message.getContent());
    }

    @Override
    public void onNotificationMessageClicked(Context context, MixPushMessage message) {
        Log.d(TAG,"通知栏消息点击 -> "+message.getContent());
    }

    @Override
    public void onNotificationMessageArrived(Context context, MixPushMessage message) {
        Log.d(TAG,"通知栏消息到达 -> "+message.getContent());
    }
}

package com.mixpush.demo;

import android.content.Context;

import com.mixpush.client.core.MixPushMessage;
import com.mixpush.client.core.MixPushMessageReceiver;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MyPushMessageReceiver extends MixPushMessageReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//    }

    @Override
    public void onReceivePassThroughMessage(Context context, MixPushMessage message) {
        super.onReceivePassThroughMessage(context, message);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MixPushMessage message) {
        super.onNotificationMessageClicked(context, message);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MixPushMessage message) {
        super.onNotificationMessageArrived(context, message);
    }
}

package com.mixpush.client.core;

import android.content.Context;

/**
 * Created by Wiki on 2017/6/1.
 */

public interface MixMessageProvider {
    /**
     * 透传
     */
    public void onReceivePassThroughMessage(Context context, MixPushMessage message);

    /**
     * 通知栏消息点击
     */
    public void onNotificationMessageClicked(Context context, MixPushMessage message);

    /**
     * 通知栏消息到达
     */
    public void onNotificationMessageArrived(Context context, MixPushMessage message);

//    public void onError(Context context, MixPushMessage message);
}

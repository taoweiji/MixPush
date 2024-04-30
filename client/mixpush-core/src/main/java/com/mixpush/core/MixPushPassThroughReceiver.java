package com.mixpush.core;

import android.content.Context;

public interface MixPushPassThroughReceiver {

    /**
     * 透传推送SDK注册成功回调
     */
  public   void onRegisterSucceed(Context context, MixPushPlatform platform);

    /**
     * 透传消息到达回调
     */
  public   void onReceiveMessage(Context context, MixPushMessage message);
}

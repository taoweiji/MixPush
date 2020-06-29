package com.mixpush.vivo;

import android.content.Context;

import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushMessage;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    MixPushHandler handler = MixPushClient.getInstance().getHandler();

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage message) {
        MixPushMessage pushMessage = new MixPushMessage();
        pushMessage.setPlatform(VivoPushProvider.VIVO);
//        pushMessage.setMsgId(String.valueOf(message.getMsgId()));
        pushMessage.setTitle(message.getTitle());
        pushMessage.setDescription(message.getContent());
        pushMessage.setPayload(message.getSkipContent());
        handler.getPushReceiver().onNotificationMessageClicked(context, pushMessage);
    }


    @Override
    public void onReceiveRegId(Context context, String regId) {
        MixPushPlatform mixPushPlatform = new MixPushPlatform(VivoPushProvider.VIVO, regId);
        handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
    }
}

package com.mixpush.sender.provider;

import com.oppo.push.server.Notification;
import com.mixpush.sender.MixPushMessage;
import com.mixpush.sender.MixPushProvider;
import com.mixpush.sender.MixPushResult;

import java.io.FileOutputStream;
import java.util.List;

import cn.teaey.apns4j.Apns4j;
import cn.teaey.apns4j.keystore.KeyStoreType;
import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.network.async.ApnsService;



public class ApnsPushProvider extends MixPushProvider {


    public ApnsPushProvider(FileOutputStream masterSecret, String password) {


    }


    @Override
    protected MixPushResult sendMessageToSingle(MixPushMessage mixPushMessage, String regId) {

        return null;
    }

    @Override
    protected MixPushResult sendMessageToList(MixPushMessage mixPushMessage, List<String> regIds) {

        return null;
    }

    @Override
    public MixPushResult broadcastMessageToAll(MixPushMessage mixPushMessage) {
        return new MixPushResult.Builder().provider(this)
                .reason(platformName() + " 不支持全局推送")
                .statusCode(MixPushResult.NOT_SUPPORT_BROADCAST)
                .build();
    }

    private Notification toMessage(MixPushMessage mixPushMessage) {
        return null;
    }

    @Override
    protected String platformName() {
        return "apns";
    }

    @Override
    public boolean isSupportBroadcastAll(boolean isPassThrough) {
        return false;
    }

    @Override
    public boolean isSupportPassThrough() {
        return false;
    }
}
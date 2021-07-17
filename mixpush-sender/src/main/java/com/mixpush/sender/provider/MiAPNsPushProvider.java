package com.mixpush.sender.provider;

import com.mixpush.sender.MixPushMessage;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;


public class MiAPNsPushProvider extends MiPushProvider {

    public MiAPNsPushProvider(String appSecretKey, boolean test) {
        super("", appSecretKey, false,test);
        if (test) {
            Constants.useSandbox();
        } else {
            Constants.useOfficial();
        }
    }

    @Override
    protected Message toMessage(MixPushMessage mixPushMessage) {
        Message.IOSBuilder builder = new Message.IOSBuilder()
                .title(mixPushMessage.getTitle())
                .body(mixPushMessage.getDescription())
                .extra("payload", mixPushMessage.isJustOpenApp() ? "{}" : mixPushMessage.getPayload())
                .timeToLive(mixPushMessage.getConfig().getTimeToLive())
                .timeToSend(mixPushMessage.getTimeToSend());
        return builder.build();
    }

    @Override
    public boolean isSupportPassThrough() {
        return false;
    }

    @Override
    protected String platformName() {
        return "mi_apns";
    }
}
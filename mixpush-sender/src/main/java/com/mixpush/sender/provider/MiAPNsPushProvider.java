package com.mixpush.sender.provider;

import com.mixpush.sender.MixPushMessage;
import com.mixpush.sender.MixPushProvider;
import com.mixpush.sender.MixPushResult;
import com.mixpush.sender.MixPushSender;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;


public class MiAPNsPushProvider extends MiPushProvider {

    public MiAPNsPushProvider(String appSecretKey, boolean test) {
        super("", appSecretKey, false);
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
                .subtitle(mixPushMessage.getDescription())
                .extra("payload", mixPushMessage.getPayload())
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
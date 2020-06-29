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

/**
 * 如果使用了小米作为默认的透传,那么就不能使用 broadcastNotificationMessageToAll
 */
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
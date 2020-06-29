package com.mixpush.sender.provider;

import com.mixpush.sender.MixPushMessage;
import com.mixpush.sender.MixPushSender;
import com.mixpush.sender.MixPushProvider;
import com.mixpush.sender.MixPushResult;
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
public class MiPushProvider extends MixPushProvider {

    //    private final String appSecretKey;
    private final String packageName;
    private final Sender sender;
    private final boolean usePassThrough;

    public MiPushProvider(String packageName, String appSecretKey, boolean usePassThrough) {
//        this.appSecretKey = appSecretKey;
        this.usePassThrough = usePassThrough;
        this.packageName = packageName;
        this.sender = new Sender(appSecretKey);
    }

    @Override
    protected MixPushResult sendMessageToSingle(MixPushMessage mixPushMessage, String regId) {
        try {
            Result result = sender.send(toMessage(mixPushMessage), regId, 3);
            return toMixPushResult(mixPushMessage, result);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    protected MixPushResult sendMessageToList(MixPushMessage mixPushMessage, List<String> regIds) {
        try {
            Result tmp = sender.send(toMessage(mixPushMessage), regIds, 3);
            return toMixPushResult(mixPushMessage, tmp);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    public MixPushResult broadcastMessageToAll(MixPushMessage mixPushMessage) {
        if (!isSupportBroadcastAll(mixPushMessage.isPassThrough())) {
            // 如果是通知栏消息,需要检查是否支持全局推送
            return new MixPushResult.Builder().provider(this)
                    .reason(platformName() + " 不支持全局推送")
                    .statusCode(MixPushResult.NOT_SUPPORT_BROADCAST)
                    .message(mixPushMessage)
                    .build();
        }
        try {
            Result result = sender.broadcastAll(toMessage(mixPushMessage), 3);
            return toMixPushResult(mixPushMessage, result);
        } catch (IOException | ParseException e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }


    @Override
    protected String platformName() {
        return MixPushSender.MI;
    }

    @Override
    public boolean isSupportBroadcastAll(boolean isPassThrough) {
        if (isPassThrough) {
            return true;
        }
        // 如果把小米推送作为默认的透传,那么所有的平台都必须注册小米推送,一旦使用broadcastAll,会导致非小米手机重复收到推送
        return !usePassThrough;
    }

    @Override
    public boolean isSupportPassThrough() {
        return true;
    }

    private MixPushResult toMixPushResult(MixPushMessage message, Result result) {
        String errorCode = String.valueOf(result.getErrorCode().getValue());
        String reason = result.getReason();
        return new MixPushResult.Builder()
                .provider(this)
                .message(message)
                .statusCode(errorCode)
                .reason(reason)
                .extra(result)
                .taskId(result.getMessageId())
                .succeed(ErrorCode.Success == result.getErrorCode())
                .build();
    }

    protected Message toMessage(MixPushMessage mixPushMessage) {
        Message.Builder builder = new Message.Builder()
                .title(mixPushMessage.getTitle())
                .description(mixPushMessage.getDescription())
                .payload(mixPushMessage.isJustOpenApp() ? null : mixPushMessage.getPayload())
                .restrictedPackageName(packageName)
                .timeToLive(mixPushMessage.getConfig().getTimeToLive())
                .notifyType(1)     // 使用默认提示音提示
                .passThrough(mixPushMessage.isPassThrough() ? 1 : 0) // 1表示透传消息, 0表示通知栏消息(默认是通知栏消息)
                .timeToSend(mixPushMessage.getTimeToSend());
        if (mixPushMessage.getConfig().getMiPushChannelId() != null) {
            builder.extra("channel_id", mixPushMessage.getConfig().getMiPushChannelId());
        }
        return builder.build();
    }
}
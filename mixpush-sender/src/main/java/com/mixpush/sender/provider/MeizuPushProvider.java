package com.mixpush.sender.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meizu.push.sdk.constant.PushType;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;
import com.mixpush.sender.MixPushMessage;
import com.mixpush.sender.MixPushProvider;
import com.mixpush.sender.MixPushResult;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MeizuPushProvider extends MixPushProvider {
    public static final String MEIZU = "meizu";
    private final IFlymePush sender;
    private String appId;
//    private String appSecretKey;

    public MeizuPushProvider(String appId, String appSecretKey) {
        this.appId = appId;
//        this.appSecretKey = appSecretKey;
        this.sender = new IFlymePush(appSecretKey);
    }

    @Override
    protected MixPushResult sendMessageToSingle(MixPushMessage mixPushMessage, String regId) {
        return sendMessageToList(mixPushMessage, Collections.singletonList(regId));
    }

    @Override
    protected MixPushResult sendMessageToList(MixPushMessage mixPushMessage, List<String> regIds) {
        try {
            ResultPack<PushResult> result = sender.pushMessage(toMessage(mixPushMessage), regIds);
            return toMixPushResult(mixPushMessage, result);
        } catch (IOException e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    public MixPushResult broadcastMessageToAll(MixPushMessage mixPushMessage) {
        try {
            ResultPack<Long> result = sender.pushToApp(PushType.STATUSBAR, toMessage(mixPushMessage));
            return toMixPushResult(mixPushMessage, result);
        } catch (IOException e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    protected String platformName() {
        return MeizuPushProvider.MEIZU;
    }

    @Override
    public boolean isSupportBroadcastAll(boolean isPassThrough) {
        return !isPassThrough;
    }

    @Override
    public boolean isSupportPassThrough() {
        return false;
    }


    private MixPushResult toMixPushResult(MixPushMessage message, ResultPack<?> result) {
        String errorCode = String.valueOf(result.code());
        String reason = result.comment();
        String taskId = null;
        if(result.value() instanceof PushResult){
            taskId = ((PushResult) result.value()).getMsgId();
        }
        return new MixPushResult.Builder()
                .provider(this)
                .message(message)
                .statusCode(errorCode)
                .reason(reason)
                .extra(result)
                .taskId(taskId)
                .succeed(result.isSucceed())
                .build();
    }

    private VarnishedMessage toMessage(MixPushMessage mixPushMessage) {
        int hour = (int) (mixPushMessage.getConfig().getTimeToLive() / 1000 / 3600);
        return new VarnishedMessage.Builder()
                .appId(Long.valueOf(appId))
                .title(mixPushMessage.getTitle())
                .content(mixPushMessage.getDescription())
                .parameters(mixPushMessage.isJustOpenApp() ? new JSONObject() : JSON.parseObject(mixPushMessage.getPayload()))
                .validTime(hour)
                .pushTimeType(0) // 定时推送 (0, "即时"),(1, "定时"), 【只对全部用户推送生效】
                .build();
    }
}
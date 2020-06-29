package com.mixpush.sender.provider;

import com.mixpush.sender.MixPushMessage;
import com.mixpush.sender.MixPushSender;
import com.mixpush.sender.MixPushProvider;
import com.mixpush.sender.MixPushResult;
import com.vivo.push.sdk.notofication.Message;
import com.vivo.push.sdk.notofication.Result;
import com.vivo.push.sdk.notofication.TargetMessage;
import com.vivo.push.sdk.server.Sender;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class VivoPushProvider extends MixPushProvider {
    private final boolean test;
    private Sender sender;
    private int appId;
    private String appKey;
    //    private String appSecret;
    private long refreshTokenTime = 0;
    // token 有效期是 1天
    final int tokenValidTime = 22 * 3600 * 1000;

    public VivoPushProvider(String appId, String appKey, String appSecret, boolean test) {
        this.appId = Integer.parseInt(appId);
        this.appKey = appKey;
        this.test = false;
        try {
            this.sender = new Sender(appSecret);//注册登录开发平台网站获取到的appSecret
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshToken() throws Exception {
        if (Math.abs(System.currentTimeMillis() - refreshTokenTime) > tokenValidTime) {
            Result result = sender.getToken(appId, appKey);
            sender.setAuthToken(result.getAuthToken());
            refreshTokenTime = System.currentTimeMillis();
        }
    }

    @Override
    protected MixPushResult sendMessageToSingle(MixPushMessage mixPushMessage, String regId) {
        try {
            refreshToken();
            Result result = sender.sendSingle(toMessage(mixPushMessage).regId(regId).build());
            return toMixPushResult(mixPushMessage, result);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    protected MixPushResult sendMessageToList(MixPushMessage mixPushMessage, List<String> regIds) {
        try {
            refreshToken();
            // 分组发送的时候,尽量使用同一个taskId
            if (mixPushMessage.getVivoTaskId() == null) {
                Result tmp = sender.saveListPayLoad(toMessage(mixPushMessage).build());
                if (tmp.getTaskId() == null) {
                    return toMixPushResult(mixPushMessage, tmp);
                }
                mixPushMessage.setVivoTaskId(tmp.getTaskId());
            }
            TargetMessage targetMessage = new TargetMessage.Builder()
                    .taskId(mixPushMessage.getVivoTaskId())
                    .requestId(UUID.randomUUID().toString())
                    .regIds(new HashSet<>(regIds))
                    .build();
            Result result = sender.sendToList(targetMessage);
            return toMixPushResult(mixPushMessage, result);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    public MixPushResult broadcastMessageToAll(MixPushMessage mixPushMessage) {
        try {
            refreshToken();
            Result result = sender.sendToAll(toMessage(mixPushMessage).build());
            return toMixPushResult(mixPushMessage, result);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    private Message.Builder toMessage(MixPushMessage mixPushMessage) {
        return new Message.Builder()
                //该测试手机设备订阅推送所得的regid，且已添加为测试设备
                .notifyType(3) // 1：无 2：响铃 3：振动 4：响铃和振动
                .title(mixPushMessage.getTitle())
                .content(mixPushMessage.getDescription())
                .timeToLive((int) (mixPushMessage.getConfig().getTimeToLive() / 1000))
                .skipType(3)// 1：打开APP首页2：打开链接3：自定义4：打开app内指定页面
                .skipContent(mixPushMessage.isJustOpenApp() ? "{}" : mixPushMessage.getPayload())
                .requestId(mixPushMessage.getMessageId()) // 必填项，用户请求唯一标识 最大64字符
                .pushMode(test ? 1 : 0) // 推送模式 0：正式推送；1：测试推送，不填默认为0（测试推送，只能给web界面录入的测试用户推送；审核中应用，只能用测试推送）
                .classification(mixPushMessage.getConfig().isSystemMessage() ? 1 : 0); // 消息类型 0：运营类消息，1：系统类消息
    }

    @Override
    protected String platformName() {
        return MixPushSender.VIVO;
    }

    @Override
    public boolean isSupportBroadcastAll(boolean isPassThrough) {
        return !isPassThrough;
    }

    @Override
    public boolean isSupportPassThrough() {
        return false;
    }

    private MixPushResult toMixPushResult(MixPushMessage message, Result result) {
        String errorCode = String.valueOf(result.getResult());
        String reason = result.getDesc();
        return new MixPushResult.Builder()
                .provider(this)
                .message(message)
                .statusCode(errorCode)
                .reason(reason)
                .extra(result)
                .succeed(result.getResult() == 0)
                .build();
    }
}
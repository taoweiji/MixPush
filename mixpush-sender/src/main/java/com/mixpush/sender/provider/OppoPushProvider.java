package com.mixpush.sender.provider;

import com.oppo.push.server.Notification;
import com.oppo.push.server.Result;
import com.oppo.push.server.Sender;
import com.oppo.push.server.Target;
import com.mixpush.sender.MixPushMessage;
import com.mixpush.sender.MixPushProvider;
import com.mixpush.sender.MixPushResult;
import com.oppo.push.server.TargetType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OppoPushProvider extends MixPushProvider {
    public static final String OPPO = "oppo";
    private Sender sender;

//    private String appKey;
//    private String masterSecret;

    public OppoPushProvider(String appKey, String masterSecret) {
//        this.appKey = appKey;
//        this.masterSecret = masterSecret;
        try {
            this.sender = new Sender(appKey, masterSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int groupSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected MixPushResult sendMessageToSingle(MixPushMessage mixPushMessage, String regId) {
        return sendMessageToList(mixPushMessage, Collections.singletonList(regId));
    }

    @Override
    protected MixPushResult sendMessageToList(MixPushMessage mixPushMessage, List<String> regIds) {
        Notification notification = toMessage(mixPushMessage);
        try {
            if (mixPushMessage.getConfig().getOppoPushChannelId() == null) {
                throw new Exception("必须在“通道配置 → 新建通道”模块中登记 channelId，再在发送消息时选择通道发送 https://open.oppomobile.com/wiki/doc#id=10289");
            }
            if (mixPushMessage.getOppoTaskId() == null) {
                Result result = sender.saveNotification(notification);
                if (result.getMessageId() == null) {
                    return toMixPushResult(mixPushMessage, result);
                }
                mixPushMessage.setOppoTaskId(result.getMessageId());
            }
            List<MixPushResult> results = new ArrayList<>();
            for (String regId : regIds) {
                Result tmp = sender.broadcastNotification(mixPushMessage.getOppoTaskId(), Target.build(regId));
                results.add(toMixPushResult(mixPushMessage, tmp));
            }
            return MixPushResult.build(results);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    @Override
    public MixPushResult broadcastMessageToAll(MixPushMessage mixPushMessage) {
        try {
            Notification notification = toMessage(mixPushMessage);
            Result result = sender.saveNotification(notification);
            if (result.getMessageId() == null) {
                return toMixPushResult(mixPushMessage, result);
            }
            Target target = new Target();
            target.setTargetType(TargetType.ALL);
            result = sender.broadcastNotification(result.getMessageId(), target);
            return toMixPushResult(mixPushMessage, result);
        } catch (Exception e) {
            return new MixPushResult.Builder().provider(this).message(mixPushMessage).error(e).build();
        }
    }

    private Notification toMessage(MixPushMessage mixPushMessage) {
        Notification notification = new Notification();
        notification.setTitle(mixPushMessage.getTitle());
        notification.setContent(mixPushMessage.getDescription());
        notification.setAppMessageId(mixPushMessage.getMessageId());
        notification.setOffLineTtl((int) (mixPushMessage.getConfig().getTimeToLive() / 1000));
        notification.setChannelId(mixPushMessage.getConfig().getOppoPushChannelId());
//        notification.setContent();

//        notification.setCallBackUrl();
//        notification.setCallBackParameter();
        String url = "mixpush://com.mixpush.oppo/message?";
        if (!mixPushMessage.isJustOpenApp()) {
            try {
                url += "title=" + URLEncoder.encode(mixPushMessage.getTitle(), "utf-8");
                url += "&description=" + URLEncoder.encode(mixPushMessage.getDescription(), "utf-8");
                url += "&payload=" + URLEncoder.encode(mixPushMessage.getPayload(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        notification.setClickActionUrl(url);
//        // 点击动作类型0，启动应用；1，打开应用内页（activity的intent action）；2，打开网页；4，打开应用内页（activity）；【非必填，默认值为0】;5,Intent scheme URL
        notification.setClickActionType(5);


        // TODO
//        notification.setClickActionType(1);
//        notification.setClickActionActivity("com.mixpush.oppo.message");
//        notification.setActionParameters(mixPushMessage.getPayload());
        return notification;
    }

    @Override
    protected String platformName() {
        return OppoPushProvider.OPPO;
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
        String errorCode = String.valueOf(result.getReturnCode().getCode());
        String reason = result.getReason();
        return new MixPushResult.Builder()
                .provider(this)
                .message(message)
                .statusCode(errorCode)
                .reason(reason)
                .extra(result)
                .taskId(message.getOppoTaskId())
                .succeed(result.getReturnCode().getCode() == 0)
                .build();
    }
}
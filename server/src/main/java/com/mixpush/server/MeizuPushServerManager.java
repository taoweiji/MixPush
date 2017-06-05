package com.mixpush.server;

import com.meizu.push.sdk.constant.PushType;
import com.meizu.push.sdk.constant.ScopeType;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.PushResponseCode;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.UnVarnishedMessage;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MeizuPushServerManager implements MixPushServerManager {
    private Long appId;
    private String appSecretKey;

    public MeizuPushServerManager(Long appId, String appSecretKey) {
        this.appId = appId;
        this.appSecretKey = appSecretKey;
    }

    private UnVarnishedMessage getUnVarnishedMessage(String messagePayload) {
        return new UnVarnishedMessage.Builder()
                .appId(appId)
                .title("透传推送标题用于方便查找")
                .content(messagePayload)
                .isOffLine(true)
                .validTime(10)
                .build();
    }

    private VarnishedMessage getVarnishedMessage(String title, String description, String messagePayload) {
        return new VarnishedMessage.Builder().appId(appId)
                .title(title).content(description)
                .customAttribute(messagePayload)
                .clickType(3)
                .offLine(true).validTime(12)
                .suspend(true).clearNoticeBar(true).vibrate(true).lights(true).sound(true)
                .build();
    }

    private VarnishedMessage getLinkNotify(String title, String description, String url) {
        return new VarnishedMessage.Builder().appId(appId)
                .title(title).content(description)
                .clickType(2).url(url)
                .offLine(true).validTime(12)
                .suspend(true).clearNoticeBar(true).vibrate(true).lights(true).sound(true)
                .build();
    }


    @Override
    public void sendMessageToAlias(List<String> alias, String messagePayload) throws IOException {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<PushResult> result = push.pushMessageByAlias(getUnVarnishedMessage(messagePayload), alias);
        handleResult(result);
    }

    @Override
    public void sendMessageToTags(List<String> tags, String messagePayload) throws IOException {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<Long> result = push.pushToTag(PushType.DIRECT, getUnVarnishedMessage(messagePayload), tags, ScopeType.UNION);
        System.out.println(result);
    }

    @Override
    public void sendMessageToAll(String messagePayload) throws IOException, ParseException {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<Long> result = push.pushToApp(PushType.DIRECT, getUnVarnishedMessage(messagePayload));
        System.out.println(result);
    }

    @Override
    public void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) throws IOException {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<PushResult> result = push.pushMessageByAlias(getVarnishedMessage(title, description, messagePayload), alias);
        handleResult(result);
    }

    @Override
    public void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) throws IOException {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<Long> result = push.pushToTag(PushType.STATUSBAR, getVarnishedMessage(title, description, messagePayload), tags, ScopeType.INTERSECTION);
        System.out.println(result);
    }

    @Override
    public void sendNotifyToAll(String title, String description, String messagePayload) throws IOException, ParseException {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<Long> result = push.pushToApp(PushType.STATUSBAR, getVarnishedMessage(title, description, messagePayload));
        System.out.println(result);
    }

    @Override
    public void sendLinkNotifyToAlias(List<String> alias, String title, String description, String url) throws Exception {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<PushResult> result = push.pushMessageByAlias(getLinkNotify(title, description, url), alias);
        handleResult(result);
    }

    @Override
    public void sendLinkNotifyToTags(List<String> tags, String title, String description, String url) throws Exception {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<Long> result = push.pushToTag(PushType.STATUSBAR, getLinkNotify(title, description, url), tags, ScopeType.INTERSECTION);
        System.out.println(result);
    }

    @Override
    public void sendLinkNotifyToAll(String title, String description, String url) throws Exception {
        //推送对象
        IFlymePush push = new IFlymePush(appSecretKey);
        ResultPack<Long> result = push.pushToApp(PushType.STATUSBAR, getLinkNotify(title, description, url));
        System.out.println(result);
    }


    /**
     * 处理推送结果
     *
     * @param result
     */
    private void handleResult(ResultPack<PushResult> result) {
        if (result.isSucceed()) {
            // 2 调用推送服务成功 （其中map为设备的具体推送结果，一般业务针对超速的code类型做处理）
            PushResult pushResult = result.value();
            String msgId = pushResult.getMsgId();//推送消息ID，用于推送流程明细排查
            Map<String, List<String>> targetResultMap = pushResult.getRespTarget();//推送结果，全部推送成功，则map为empty
            System.out.println("push msgId:" + msgId);
            System.out.println("push targetResultMap:" + targetResultMap);
            if (targetResultMap != null && !targetResultMap.isEmpty()) {
                // 3 判断是否有获取超速的target
                if (targetResultMap.containsKey(PushResponseCode.RSP_SPEED_LIMIT.getValue())) {
                    // 4 获取超速的target
                    List<String> rateLimitTarget = targetResultMap.get(PushResponseCode.RSP_SPEED_LIMIT.getValue());
                    System.out.println("rateLimitTarget is :" + rateLimitTarget);
                    //TODO 5 业务处理，重推......
                }
            }
        } else {
            // 调用推送接口服务异常 eg: appId、appKey非法、推送消息非法.....
            // result.code(); //服务异常码
            // result.comment();//服务异常描述
            System.out.println(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));
        }
    }
}

package com.mixpush.honor;

import com.hihonor.push.sdk.HonorMessageService;
import com.hihonor.push.sdk.HonorPushDataMsg;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushMessage;

public class UnifiedHonorMessageService extends HonorMessageService {
    public static final String TAG = "HonorPushProvider";
    MixPushHandler handler = MixPushClient.getInstance().getHandler();

    /**
     * 接收透传消息方法。
     */
    @Override
    public void onMessageReceived(HonorPushDataMsg message) {
        MixPushMessage mixPushMessage = new MixPushMessage();
        mixPushMessage.setPlatform(HonorPushProvider.HONOR);
        // TODO: TYPE_MSG_NOTIFICATION 通知栏消息
//        if (message.getType() == 1) {
//            mixPushMessage.setTitle(message.getNotification().getTitle());
//            mixPushMessage.setDescription(message.getNotification().getBody());
//        }
        mixPushMessage.setPayload(message.getData());
        mixPushMessage.setPassThrough(message.getType() == 0);
        handler.getPassThroughReceiver().onReceiveMessage(this, mixPushMessage);
    }

    /**
     * 服务端更新token回调方法。
     * APP调用getToken接口向服务端申请token，如果服务端当次没有返回token值，后续服务端返回token通过此接口返回。主要包含如下三种场景：
     * 1、申请Token如果当次调用失败，PUSH会自动重试申请，成功后则以onNewToken接口返回。
     * 2、如果服务端识别token过期，服务端刷新token也会以onNewToken方式返回。
     * 3、华为设备上EMUI版本低于10.0申请token时，以onNewToken方式返回。
     */
    @Override
    public void onNewToken(String token) {
        MixPushPlatform mixPushPlatform = new MixPushPlatform(HonorPushProvider.HONOR, token);
        handler.getPushReceiver().onRegisterSucceed(this, mixPushPlatform);
    }
}

package com.mixpush.honor;

import android.content.Context;

import com.hihonor.push.sdk.HonorPushCallback;
import com.hihonor.push.sdk.HonorPushClient;
import com.mixpush.core.BaseMixPushProvider;
import com.mixpush.core.RegisterType;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;

import static com.mixpush.honor.UnifiedHonorMessageService.TAG;

public class HonorPushProvider extends BaseMixPushProvider {
    public static final String HONOR = "honor";
    public static String regId;

    MixPushHandler handler = MixPushClient.getInstance().getHandler();


    @Override
    public void register(Context context, RegisterType type) {
        HonorPushClient.getInstance().init(context, true);
    }

    @Override
    public void unRegister(Context context) {
        //注销PushToken
        HonorPushClient.getInstance().deletePushToken(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // TODO: 注销PushToken成功
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                // TODO: 注销PushToken失败
                handler.getLogger().log(TAG, "注销PushToken失败. code: " + errorCode + ", message: " + errorString);
            }
        });
    }

    @Override
    public boolean isSupport(Context context) {
        return HonorPushClient.getInstance().checkSupportHonorPush(context);
    }

    @Override
    public String getPlatformName() {
        return HonorPushProvider.HONOR;
    }

    @Override
    public String getRegisterId(Context context) {
        // 获取PushToken
        HonorPushClient.getInstance().getPushToken(new HonorPushCallback<String>() {
            @Override
            public void onSuccess(String pushToken) {
                // TODO: 新Token处理
                regId = pushToken;
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                // TODO: 错误处理
                handler.getLogger().log(TAG, "申请token失败. code: "+errorCode + ", message: " + errorString);
            }
        });
        return regId;
    }
}

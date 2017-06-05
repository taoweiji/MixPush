package com.mixpush.client.meizu;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.meizu.cloud.pushsdk.PushManager;
import com.mixpush.client.core.MixMessageProvider;
import com.mixpush.client.core.MixPushManager;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MeizuPushManager implements MixPushManager {
    public static final String NAME = "meizuPush";
    private static final String TAG = "MeizuPushManager";
    public static MixMessageProvider sMixMessageProvider;
    public static boolean REGISTER_PUSH = false;

    private String appId;
    private String appKey;

    public MeizuPushManager(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    @Override
    public void registerPush(Context context) {
        Log.e(TAG,"registerPush");
        PushManager.register(context, appId, appKey);
        REGISTER_PUSH = true;

        String pushId = PushManager.getPushId(context);
        if (!TextUtils.isEmpty(pushId)) {
            // 需要确保，不能收到推送
            PushManager.switchPush(context, appId, appKey, pushId, true);
        }
    }

    @Override
    public void unRegisterPush(Context context) {
        Log.e(TAG,"unRegisterPush");
        REGISTER_PUSH = false;
        String pushId = PushManager.getPushId(context);
        if (!TextUtils.isEmpty(pushId)) {
            // 需要确保，不能收到推送
            PushManager.switchPush(context, appId, appKey, pushId, false);
//            PushManager.checkSubScribeAlias(context,appId,appKey,pushId);
        }
        PushManager.unRegister(context, appId, appKey);
    }

    @Override
    public void bindAlias(Context context, String alias) {
        Log.e(TAG,"bindAlias");
        PushManager.subScribeAlias(context, appId, appKey, PushManager.getPushId(context), alias);
    }

    @Override
    public void unBindAlias(Context context, String alias) {
        Log.e(TAG,"unBindAlias");
        PushManager.unSubScribeAlias(context, appId, appKey, PushManager.getPushId(context), alias);
    }

    @Override
    public void subscribeTags(Context context, String... tags) {
        for (String tag : tags) {
            PushManager.subScribeTags(context, appId, appKey, PushManager.getPushId(context), tag);
        }
    }

    @Override
    public void unSubscribeTags(Context context, String... tags) {
        for (String tag : tags) {
            PushManager.unSubScribeTags(context, appId, appKey, PushManager.getPushId(context), tag);
        }

    }


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setMessageProvider(MixMessageProvider provider) {
        sMixMessageProvider = provider;
    }

    @Override
    public void disable(Context context) {
        unRegisterPush(context);
    }

    public String getClientId(Context context) {
        return PushManager.getPushId(context);
    }
}

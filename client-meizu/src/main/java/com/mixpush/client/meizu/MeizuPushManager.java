package com.mixpush.client.meizu;

import android.content.Context;

import com.meizu.cloud.pushsdk.PushManager;
import com.mixpush.client.core.MixMessageProvider;
import com.mixpush.client.core.MixPushManager;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MeizuPushManager implements MixPushManager {
    public static final String NAME = "meizuPush";
    public static MixMessageProvider sMixMessageProvider;

    private String appId;
    private String appKey;

    public MeizuPushManager(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    @Override
    public void registerPush(Context context) {
        PushManager.register(context, appId, appKey);
    }

    @Override
    public void unRegisterPush(Context context) {
        PushManager.unRegister(context, appId, appKey);
    }

    @Override
    public void bindAlias(Context context, String alias) {
        PushManager.subScribeAlias(context, appId, appKey, PushManager.getPushId(context), alias);
    }

    @Override
    public void unBindAlias(Context context, String alias) {
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
}

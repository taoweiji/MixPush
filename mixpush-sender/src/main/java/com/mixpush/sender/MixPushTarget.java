package com.mixpush.sender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MixPushTarget {
    public boolean broadcastAll = false;
    public List<MixPushPlatform> platforms = new ArrayList<>();

    public static MixPushTarget single(String platformName, String regId) {
        return values(new MixPushPlatform(platformName, regId));
    }

    public static MixPushTarget values(MixPushPlatform... regIds) {
        MixPushTarget target = new MixPushTarget();
        target.platforms.addAll(Arrays.asList(regIds));
        return target;
    }

    public static MixPushTarget list(List<MixPushPlatform> regIds) {
        MixPushTarget target = new MixPushTarget();
        target.platforms.addAll(regIds);
        return target;
    }

    /**
     * 华为/oppo/apns 不支持该方法
     */
    public static MixPushTarget broadcastAll() {
        MixPushTarget target = new MixPushTarget();
        target.broadcastAll = true;
        return target;
    }

//    public boolean isSingle() {
//        return !this.broadcastAll && platforms.size() == 1;
//    }
}
package com.mixpush;

import android.util.Log;

import com.mixpush.core.GetRegisterIdCallback;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushPlatform;

public class Test {
    void test() {
        // 建议在首页的onCreate调用,并上报regId给服务端
//        MixPushClient.getInstance().getRegisterId(this, new GetRegisterIdCallback() {
//            public void callback(MixPushPlatform platform) {
//                if (platform != null) {
//                    Log.e("GetRegisterIdCallback", platform.toString());
//                    // TODO 上报regId给服务端
//                }
//            }
//        });
    }
}

package com.mixpush.huawei;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.adapter.internal.AvailableCode;
import com.huawei.hms.api.HuaweiMobileServicesUtil;
import com.huawei.hms.common.ApiException;
import com.mixpush.core.BaseMixPushProvider;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.RegisterType;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import static com.huawei.hms.adapter.internal.AvailableCode.SERVICE_VERSION_UPDATE_REQUIRED;
import static com.mixpush.huawei.UnifiedHmsMessageService.TAG;

public class HuaweiPushProvider extends BaseMixPushProvider {
    public static final String HUAWEI = "huawei";
    public static String regId;

    MixPushHandler handler = MixPushClient.getInstance().getHandler();


    @Override
    public void register(Context context, RegisterType type) {
        syncGetToken(context);
    }

    @Override
    public void unRegister(Context context) {

    }

    @Override
    public boolean isSupport(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 17) {
            return false;
        }
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (!manufacturer.equals("huawei")) {
            return false;
        }
        int available = HuaweiMobileServicesUtil.isHuaweiMobileServicesAvailable(context);
        if (available != AvailableCode.SUCCESS) {
            handler.getLogger().log(TAG, "华为推送不可用 ErrorCode = " + available);
            return false;
        }
        return true;
//        return canHuaWeiPush();
    }

    /**
     * 判断是否可以使用华为推送
     */
    public static Boolean canHuaWeiPush() {
        int emuiApiLevel = 0;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return emuiApiLevel >= 5.0;
    }

    @Override
    public String getPlatformName() {
        return HuaweiPushProvider.HUAWEI;
    }

    @Override
    public String getRegisterId(Context context) {
        syncGetToken(context);
//        try {
//            // read from agconnect-services.json
//            String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
//            regId = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
//            Log.e(TAG, "get token:" + regId);
//            return regId;
//        } catch (ApiException e) {
//            handler.getLogger().log(TAG, "hms get token failed " + e.toString() + " https://developer.huawei.com/consumer/cn/doc/development/HMSCore-References-V5/error-code-0000001050255690-V5", e);
//            e.printStackTrace();
//        }
        return null;
    }


    private void syncGetToken(final Context context) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
                    regId = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
                    Log.e(TAG, "get token:" + regId);
                } catch (ApiException e) {
                    handler.getLogger().log(TAG, "hms get token failed " + e.toString() + " https://developer.huawei.com/consumer/cn/doc/development/HMSCore-References-V5/error-code-0000001050255690-V5", e);
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(regId)) {
                    Log.e("regId", regId);
                    MixPushPlatform mixPushPlatform = new MixPushPlatform(HuaweiPushProvider.HUAWEI, regId);
                    MixPushClient.getInstance().getHandler().getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
                }
            }
        }.start();
    }
}

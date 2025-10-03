package com.mixpush.oppo;

import android.content.Context;
import android.os.Build;

import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.mixpush.core.BaseMixPushProvider;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.RegisterType;

public class OppoPushProvider extends BaseMixPushProvider {
    public static final String OPPO = "oppo";
    public static final String TAG = OPPO;


    @Override
    public void register(Context context, RegisterType type) {
        String appSecret = getMetaData(context, "OPPO_APP_SECRET");
        String appKey = getMetaData(context, "OPPO_APP_KEY");
        HeytapPushManager.init(context, MixPushClient.debug);
        HeytapPushManager.register(context, appKey, appSecret, new MyCallBackResultService(context.getApplicationContext()));
        String registerID = HeytapPushManager.getRegisterID();
        if (registerID != null) {
            MixPushPlatform mixPushPlatform = new MixPushPlatform(OppoPushProvider.OPPO, registerID);
            MixPushClient.getInstance().getHandler().getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
        }
    }

    @Override
    public void unRegister(Context context) {

    }

    @Override
    public String getRegisterId(final Context context) {
        return HeytapPushManager.getRegisterID();
    }

    @Override
    public boolean isSupport(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        String brand = Build.BRAND.toLowerCase();
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (manufacturer.equals("oneplus") || manufacturer.equals("oppo") || brand.equals("oppo") || brand.equals("realme")) {
            HeytapPushManager.init(context, true);
            return HeytapPushManager.isSupportPush(context);
        }
        return false;
    }

    @Override
    public String getPlatformName() {
        return OppoPushProvider.OPPO;
    }
}

class MyCallBackResultService implements ICallBackResultService {
    Context context;
    MixPushHandler handler = MixPushClient.getInstance().getHandler();


    public MyCallBackResultService(Context context) {
        this.context = context;
    }

    @Override
    public void onRegister(int responseCode, String registerID) {
        handler.getLogger().log(OppoPushProvider.TAG, "onRegister responseCode = " + responseCode + ", registerID = " + registerID);
        MixPushPlatform mixPushPlatform = new MixPushPlatform(OppoPushProvider.OPPO, registerID);
        handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
    }

    @Override
    public void onUnRegister(int responseCode) {

    }

    @Override
    public void onSetPushTime(int responseCode, String pushTime) {

    }

    @Override
    public void onGetPushStatus(int responseCode, int status) {
        handler.getLogger().log(OppoPushProvider.TAG, "onGetPushStatus responseCode = " + responseCode + ", status = " + status);
    }

    @Override
    public void onGetNotificationStatus(int responseCode, int status) {
        handler.getLogger().log(OppoPushProvider.TAG, "onGetNotificationStatus responseCode = " + responseCode + ", status = " + status);
    }

    @Override
    public void onError(int i, String s) {

    }
}
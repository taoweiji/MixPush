package com.mixpush.client.core;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MixPushClient {


    private static Map<String, MixPushManager> sPushManagerMap = new HashMap<>();
    private static String sUsePushName;
    private static Selector sSelector;
    private static String sReceiverPermission = null;// 避免被其它APP接收
    private static Class<? extends MixPushIntentService> sMixPushIntentServiceClass;


    private MixPushClient() {

    }

    public static void setPushIntentService(Class<? extends MixPushIntentService> mixPushIntentServiceClass) {
        MixPushClient.sMixPushIntentServiceClass = mixPushIntentServiceClass;
    }

    public static void setSelector(Selector selector) {
        sSelector = selector;
        sUsePushName = sSelector.select(sPushManagerMap, Build.BRAND);
    }

    public static String getUsePushName() {
        return sUsePushName;
    }

    public static void addPushAdapter(MixPushManager pushAdapter) {
        sPushManagerMap.put(pushAdapter.getName(), pushAdapter);
        pushAdapter.setMessageProvider(mMixMessageProvider);
    }

    public static void registerPush(Context context) {

        sReceiverPermission = context.getPackageName() + ".permission.MIXPUSH_RECEIVE";

        Set<String> keys = sPushManagerMap.keySet();
        for (String key : keys) {
            if (key.contains(sUsePushName)) {
                sPushManagerMap.get(key).registerPush(context);
            } else {
                sPushManagerMap.get(key).unRegisterPush(context);
            }
        }
    }
    private static MixPushManager getPushManager(){
        if (sUsePushName == null){
            throw new RuntimeException("you need setSelector or setUsePushName");
        }
        return sPushManagerMap.get(sUsePushName);
    }

    public static void unRegisterPush(Context context) {
        getPushManager().unRegisterPush(context);
    }

    public static void setUsePushName(String sUsePushName) {
        MixPushClient.sUsePushName = sUsePushName;
    }

    public static void bindAlias(Context context, String alias) {
        getPushManager().bindAlias(context, alias);
    }

    public static void unBindAlias(Context context, String alias) {
        getPushManager().unBindAlias(context, alias);
    }

    public static void subscribeTags(Context context, String... tags){
        getPushManager().subscribeTags(context, tags);
    }

    public static void unSubscribeTags(Context context, String... tags){
        getPushManager().unSubscribeTags(context, tags);
    }

    public static class Selector {
        public String select(Map<String, MixPushManager> pushAdapterMap, String brand) {
            if (pushAdapterMap.containsKey("meizuPush") && brand.equalsIgnoreCase("meizu")) {
                return "meizuPush";
            } else if (pushAdapterMap.containsKey("mipush") && brand.equalsIgnoreCase("xiaomi")) {
                return "mipush";
            } else if (pushAdapterMap.containsKey("getui")) {
                return "getui";
            }
            return null;
        }
    }

    private static MixMessageProvider mMixMessageProvider = new MixMessageProvider() {
        @Override
        public void onReceivePassThroughMessage(Context context, MixPushMessage message) {
            message.setNotify(0);
            Intent intent = new Intent(MixPushMessageReceiver.RECEIVE_THROUGH_MESSAGE);
            intent.putExtra("message", message);
            context.sendBroadcast(intent, sReceiverPermission);
            Log.d("onReceivePassThrough", message.getContent());

            if (sMixPushIntentServiceClass != null){
                intent.setClass(context,sMixPushIntentServiceClass);
                context.startService(intent);
            }
        }

        @Override
        public void onNotificationMessageClicked(Context context, MixPushMessage message) {
            message.setNotify(1);
            Intent intent = new Intent(MixPushMessageReceiver.NOTIFICATION_CLICKED);
            intent.putExtra(MixPushMessageReceiver.MESSAGE, message);
            context.sendBroadcast(intent, sReceiverPermission);
            Log.d("onNotificationClicked", message.getContent());

            if (sMixPushIntentServiceClass != null){
                intent.setClass(context,sMixPushIntentServiceClass);
                context.startService(intent);
            }
        }

        @Override
        public void onNotificationMessageArrived(Context context, MixPushMessage message) {
            Intent intent = new Intent(MixPushMessageReceiver.NOTIFICATION_ARRIVED);
            intent.putExtra("message", message);
            context.sendBroadcast(intent, sReceiverPermission);
            Log.d("onNotificationArrived", message.getContent());

            if (sMixPushIntentServiceClass != null){
                intent.setClass(context,sMixPushIntentServiceClass);
                context.startService(intent);
            }
        }
    };
}

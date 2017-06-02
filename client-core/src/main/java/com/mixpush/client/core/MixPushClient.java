package com.mixpush.client.core;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MixPushClient {


    private static Map<String, MixPushManager> sPushAdapterMap = new HashMap<>();
    private static String sUsePushAdapterName;
    private static Selector sSelector;
    private static String sReceiverPermission = null;// 避免被其它APP接收


    private MixPushClient() {

    }

    public static void setSelector(Selector selector) {
        sSelector = selector;
    }

    public static void addPushAdapter(MixPushManager pushAdapter) {
        sPushAdapterMap.put(pushAdapter.getName(), pushAdapter);
        pushAdapter.setMessageProvider(mMixMessageProvider);
    }

    public static void registerPush(Context context) {
        sUsePushAdapterName = sSelector.select(sPushAdapterMap, Build.BRAND);
        sReceiverPermission = context.getPackageName() + ".permission.MIXPUSH_RECEIVE";

        Set<String> keys = sPushAdapterMap.keySet();
        for (String key : keys) {
            if (key.contains(sUsePushAdapterName)) {
                sPushAdapterMap.get(key).registerPush(context);
            } else {
                sPushAdapterMap.get(key).unRegisterPush(context);
            }
        }
    }

    public static void unRegisterPush(Context context) {
        sPushAdapterMap.get(sUsePushAdapterName).unRegisterPush(context);
    }


    public static void bindAlias(Context context, String alias) {
        sPushAdapterMap.get(sUsePushAdapterName).bindAlias(context, alias);
    }

    public static void unBindAlias(Context context, String alias) {
        sPushAdapterMap.get(sUsePushAdapterName).unBindAlias(context, alias);
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
        }

        @Override
        public void onNotificationMessageClicked(Context context, MixPushMessage message) {
            message.setNotify(1);
            Intent intent = new Intent(MixPushMessageReceiver.NOTIFICATION_CLICKED);
            intent.putExtra(MixPushMessageReceiver.MESSAGE, message);
            context.sendBroadcast(intent, sReceiverPermission);
            Log.d("onNotificationClicked", message.getContent());
        }

        @Override
        public void onNotificationMessageArrived(Context context, MixPushMessage message) {
            Intent intent = new Intent(MixPushMessageReceiver.NOTIFICATION_ARRIVED);
            intent.putExtra("message", message);
            context.sendBroadcast(intent, sReceiverPermission);
            Log.d("onNotificationArrived", message.getContent());
        }
    };
}

//package com.mixpush.client;
//
//import android.content.Context;
//import android.os.Build;
//
//import com.mixpush.client.core.MixPushManager;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MixPushClient {
//
//    //    private static MixPushClient sInstance;
//    private static Map<String, MixPushManager> sPushAdapterMap = new HashMap<>();
//    private static String sUsePushAdapterName;
//    private static Selector sSelector;
//
//
////    public static MixPushClient getInstance() {
////        if (sInstance == null) {
////            sInstance = new MixPushClient();
////        }
////        return sInstance;
////    }
//
//    private MixPushClient() {
//
//    }
//
//    public static void setSelector(Selector selector) {
//        sSelector = selector;
//    }
//
//    public static void addPushAdapter(MixPushManager pushAdapter) {
//        sPushAdapterMap.put(pushAdapter.getName(), pushAdapter);
//    }
//
////    public static void setUsePushAdapterName(String name) {
////        sUsePushAdapterName = name;
////    }
//
//    public static void registerPush(Context context) {
//        sUsePushAdapterName = sSelector.select(sPushAdapterMap, Build.BRAND);
//    }
//
//    public static void unRegisterPush(Context context) {
//
//    }
//
//    public static String getToken(Context context) {
//        return null;
//    }
//
//    public static void bindAlias(Context context, String alias) {
//
//    }
//
//    public static void unBindAlias(Context context, String alias) {
//
//    }
//
//    public static void setToken(String token) {
//
//    }
//
//
////    private static MixPushManager getMiPushAdapter() {
////        Class aClass = null;
////        try {
////            aClass = Class.forName("com.mixpush.client.mipush.MiPushAdapter");
////            return (MixPushManager) aClass.newInstance();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////
////    public static MixPushManager getGeTuiAdapter() {
////        Class aClass = null;
////        try {
////            aClass = Class.forName("com.mixpush.client.getui.GeTuiAdapter");
////            return (MixPushManager) aClass.newInstance();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////
////    public static MixPushManager getMeizuPushAdapter() {
////        Class aClass = null;
////        try {
////            aClass = Class.forName("com.mixpush.client.meizu.MeizuPushAdapter");
////            return (MixPushManager) aClass.newInstance();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//    public static class Selector {
//        public String select(Map<String, MixPushManager> pushAdapterMap, String brand) {
//            if (pushAdapterMap.containsKey("meizuPush") && brand.equalsIgnoreCase("meizu")) {
//                return "meizuPush";
//            } else if (pushAdapterMap.containsKey("mipush") && brand.equalsIgnoreCase("xiaomi")) {
//                return "mipush";
//            } else if (pushAdapterMap.containsKey("getui")) {
//                return "getui";
//            }
//            return null;
//        }
//    }
//}

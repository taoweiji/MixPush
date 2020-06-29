//package com.mixpush.core.utils;
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.provider.Settings;
//
//import androidx.core.app.NotificationManagerCompat;
//
///**
// * Created by chenxiangxiang on 2019/1/16.
// */
//
//public class NotificationManagerUtils {
//
//    public static boolean isPermissionOpen(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return NotificationManagerCompat.from(context).getImportance() != NotificationManager.IMPORTANCE_NONE;
//        }
//        return NotificationManagerCompat.from(context).areNotificationsEnabled();
//    }
//
//    public static void openPermissionSetting(Context context) {
//        try {
//            Intent localIntent = new Intent();
//            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //直接跳转到应用通知设置的代码：
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
//                localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
//                context.startActivity(localIntent);
//                return;
//            }
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//                localIntent.putExtra("app_package", context.getPackageName());
//                localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
//                context.startActivity(localIntent);
//                return;
//            }
//            if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
//                localIntent.setData(Uri.parse("package:" + context.getPackageName()));
//                context.startActivity(localIntent);
//                return;
//            }
//
//            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
//
//            if (Build.VERSION.SDK_INT >= 9) {
//                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                context.startActivity(localIntent);
//                return;
//            }
//
//            localIntent.setAction(Intent.ACTION_VIEW);
//            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
//            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(" cxx   pushPermission 有问题");
//        }
//    }
//}
package com.mixpush.client.getui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.mixpush.client.core.MixPushMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用于实现通知栏功能
 */
public class GeTuiMessageReceiver extends BroadcastReceiver {
    public static final String TAG = "GeTuiMessageReceiver";

    public static final int ACTION_NOTIFICATION_CLICKED = 2364;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive() action=" + bundle.getInt(PushConsts.CMD_ACTION));//1001(通知)

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case ACTION_NOTIFICATION_CLICKED:

                MixPushMessage message = (MixPushMessage) intent.getSerializableExtra("message");
                if (GeTuiManager.sMixMessageProvider != null) {
                    GeTuiManager.sMixMessageProvider.onNotificationMessageClicked(context, message);
                }
                break;
            case PushConsts.GET_MSG_DATA:


                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Log.d(TAG, "receiver payload : " + data);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.optInt("notify", 0) == 1) {

                            if (GeTuiManager.sMixMessageProvider != null) {
                                MixPushMessage resultMessage = showNotify(context, jsonObject);
                                GeTuiManager.sMixMessageProvider.onNotificationMessageArrived(context, resultMessage);
                            }
                        } else {
                            if (GeTuiManager.sMixMessageProvider != null) {
                                GeTuiManager.sMixMessageProvider.onReceivePassThroughMessage(context, new MixPushMessage(data));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PushConsts.GET_CLIENTID:
                String clientid = bundle.getString("clientid");
                Log.d(TAG, "clientid = " + clientid);
                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                Log.d(TAG, "online = " + online);
                break;

            default:
                break;
        }
    }

    private MixPushMessage showNotify(Context context, JSONObject data) {
        Log.e(TAG, "showNotify -> " + "data = " + data);
        String title = data.optString("title");
        String description = data.optString("description");

        data.remove("notify");
        data.remove("title");
        data.remove("description");

        //全局通知管理者，通过获取系统服务获取
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //通知栏构造器,创建通知栏样式
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent intent = new Intent("com.igexin.sdk.action." + context.getPackageName());
        intent.putExtra(PushConsts.CMD_ACTION, ACTION_NOTIFICATION_CLICKED);
        MixPushMessage message = new MixPushMessage();
        message.setContent(data.toString());
        message.setTitle(title);
        message.setDescription(description);
        intent.putExtra("message", message);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //设置通知栏标题

        mBuilder.setContentTitle(title)
                //设置通知栏显示内容
                .setContentText(description)
                //设置通知栏点击意图
                .setContentIntent(mPendingIntent)
                //通知首次出现在通知栏，带上升动画效果的
                .setTicker(title)
                //通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setWhen(System.currentTimeMillis())
                //设置该通知优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //设置这个标志当用户单击面板就可以让通知将自动取消
                .setAutoCancel(true)
                //使用当前的用户默认设置
                .setDefaults(Notification.DEFAULT_VIBRATE)
                //设置通知小ICON(应用默认图标)
                .setSmallIcon(R.drawable.ic_launcher);
        mNotificationManager.notify(1234, mBuilder.build());

        return message;
    }

}
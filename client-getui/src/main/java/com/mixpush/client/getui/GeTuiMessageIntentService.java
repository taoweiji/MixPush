package com.mixpush.client.getui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.mixpush.client.core.MixPushMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class GeTuiMessageIntentService extends GTIntentService {

    public GeTuiMessageIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        if (msg.getPayload() == null) {
            Log.e(TAG, "onReceiveMessageData -> " + "payload=null");
            return;
        }
        String data = new String(msg.getPayload());
        Log.e(TAG, "onReceiveMessageData -> " + "payload = " + data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("notify", 0) == 1) {
                if (GeTuiManager.sMixMessageProvider != null) {
                    MixPushMessage message = new MixPushMessage();
                    message.setContent(data);
                    message.setPlatform(GeTuiManager.NAME);
                    GeTuiManager.sMixMessageProvider.onNotificationMessageClicked(context, message);
                }
            } else {
                if (GeTuiManager.sMixMessageProvider != null) {
                    MixPushMessage message = new MixPushMessage();
                    message.setContent(data);
                    message.setPlatform(GeTuiManager.NAME);
                    GeTuiManager.sMixMessageProvider.onReceivePassThroughMessage(context, message);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e(TAG, "onReceiveCommandResult -> " + "action = " + cmdMessage.getAction());
    }
}
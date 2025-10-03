package com.mixpush.oppo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushMessage;

public class OppoMessageReceiveActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        this.finish();
        if (data != null) {
            MixPushMessage message = new MixPushMessage();
            message.setPlatform(OppoPushProvider.OPPO);
            message.setTitle(data.getQueryParameter("title"));
            message.setDescription(data.getQueryParameter("description"));
            message.setPayload(data.getQueryParameter("payload"));
            MixPushClient.getInstance().getHandler().getLogger().log(OppoPushProvider.TAG, "url is " + data.toString());
            MixPushClient.getInstance().getHandler().getPushReceiver().onNotificationMessageClicked(this.getApplicationContext(), message);
        } else {
            MixPushClient.getInstance().openApp(this);
        }
    }
}
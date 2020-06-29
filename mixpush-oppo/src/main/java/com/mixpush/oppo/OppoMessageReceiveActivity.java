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
        if (data != null) {
            MixPushMessage message = new MixPushMessage();
            message.setPlatform(OppoPushProvider.OPPO);
            message.setTitle(data.getQueryParameter("title"));
            message.setDescription(data.getQueryParameter("description"));
            message.setPayload(data.getQueryParameter("payload"));
            MixPushClient.getInstance().getHandler().getLogger().log(OppoPushProvider.TAG, "url is " + data.toString());
            MixPushClient.getInstance().getHandler().getPushReceiver().onNotificationMessageClicked(this.getApplicationContext(), message);
        } else {
//            MixPushClient.getInstance().getHandler().getLogger().log(OppoPushProvider.TAG, "url is null");
            MixPushClient.getInstance().openApp(this);
        }
        // mixpush://com.mixpush.oppo/message?title=title&description=description&payload=%7b%22url%22%3a%22http%3a%2f%2fsoso.com%22%7d
        this.finish();
    }
}
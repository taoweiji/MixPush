package com.mixpush.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mixpush.client.core.MixPushMessage;
import com.mixpush.client.core.MixPushMessageReceiver;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;

public class DemoActivity extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MixPushMessageReceiver.RECEIVE_THROUGH_MESSAGE);
        filter.addAction(MixPushMessageReceiver.NOTIFICATION_ARRIVED);
        filter.addAction(MixPushMessageReceiver.NOTIFICATION_CLICKED);
        filter.addAction(MixPushMessageReceiver.ERROR);
        this.registerReceiver(mBroadcastReceiver, filter);



        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d("mipush", content, t);
            }
            @Override
            public void log(String content) {
                Log.d("mipush", content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private BroadcastReceiver mBroadcastReceiver = new MixPushMessageReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            onReceive(context, intent);
//            text.setText(text.getText().toString() + "\nonReceive");
//        }

        @Override
        public void onReceivePassThroughMessage(Context context, MixPushMessage message) {
            super.onReceivePassThroughMessage(context, message);
            text.setText(text.getText().toString() + "\nonReceivePassThroughMessage:" + message.getContent());
        }

        @Override
        public void onNotificationMessageClicked(Context context, MixPushMessage message) {
            super.onNotificationMessageClicked(context, message);
            text.setText(text.getText().toString() + "\nonNotificationMessageClicked:" + message.getContent());
        }

        @Override
        public void onNotificationMessageArrived(Context context, MixPushMessage message) {
            super.onNotificationMessageArrived(context, message);
            text.setText(text.getText().toString() + "\nonNotificationMessageArrived:" + message.getContent());
        }
    };
}

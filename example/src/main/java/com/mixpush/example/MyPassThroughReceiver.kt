package com.mixpush.example

import android.content.Context
import com.mixpush.core.MixPushMessage
import com.mixpush.core.MixPushPassThroughReceiver
import com.mixpush.core.MixPushPlatform

class MyPassThroughReceiver : MixPushPassThroughReceiver {
    override fun onRegisterSucceed(context: Context?, platform: MixPushPlatform?) {
        TODO("Not yet implemented")
    }

    override fun onReceiveMessage(context: Context?, message: MixPushMessage?) {
        TODO("Not yet implemented")
    }
}

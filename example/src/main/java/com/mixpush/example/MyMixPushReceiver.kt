package com.mixpush.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.util.Log
import com.mixpush.core.MixPushPlatform
import com.mixpush.core.MixPushMessage
import com.mixpush.core.MixPushReceiver
import org.json.JSONObject

class MyMixPushReceiver : MixPushReceiver() {
    override fun onRegisterSucceed(context: Context, platform: MixPushPlatform) {
        // TODO 上传token到服务端
        Log.e("onRegisterSucceed", "$platform")
    }

    override fun onNotificationMessageClicked(context: Context, message: MixPushMessage) {
        var intent: Intent? = null
        if (message.payload == null) {
            // 启动APP
            intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        } else {
            val json = JSONObject(message.payload)
            val url = json.optString("url", "")
            if (url == "") {
                // 启动APP
                intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            } else {
                val uri = Uri.parse(json.getString("url"))
                // 打开浏览器
                if (uri.scheme!!.contains("http")) {
                    intent = Intent(context, WebViewActivity::class.java).putExtra("url", url)
                } else if (uri.path == "/user") {
                    val userId = uri.getQueryParameter("userId")
                    intent = Intent(context, UserActivity::class.java).putExtra("userId", userId)
                }
            }
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
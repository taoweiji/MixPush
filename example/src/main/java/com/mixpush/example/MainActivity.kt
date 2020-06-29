package com.mixpush.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mixpush.core.GetRegisterIdCallback
import com.mixpush.core.MixPushPlatform
import com.mixpush.core.MixPushClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var notificationMixPushPlatform: MixPushPlatform? = null
    var passThroughMixPushPlatform: MixPushPlatform? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!NotificationManagerUtils.isPermissionOpen(this)) {
            NotificationManagerUtils.openPermissionSetting(this)
        }
        copy_reg_id.setOnClickListener {
            if (notificationMixPushPlatform != null) {
                copy(notificationMixPushPlatform!!.regId!!)
            } else {
                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show()
            }
        }
        copy_pass_through_reg_id.setOnClickListener {
            if (passThroughMixPushPlatform != null) {
                copy(passThroughMixPushPlatform!!.regId!!)
            } else {
                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show()
            }
        }
        MixPushClient.getInstance().getRegisterId(this, object : GetRegisterIdCallback() {
            override fun callback(platformMix: MixPushPlatform?) {
                notificationMixPushPlatform = platformMix
                updateRegId()
            }
        })

        updateRegId()
        onRequirePermissions()
    }

    private fun onRequirePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 101
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun copy(data: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, data)
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(this, "已经复制到粘贴板", Toast.LENGTH_SHORT).show()
    }

    fun updateRegId() {
        runOnUiThread {
            if (notificationMixPushPlatform != null) {
                text_reg_id.text = notificationMixPushPlatform.toString()
            }
            if (passThroughMixPushPlatform != null) {
                text_pass_through_reg_id.text = passThroughMixPushPlatform.toString()
            }
        }
    }
}

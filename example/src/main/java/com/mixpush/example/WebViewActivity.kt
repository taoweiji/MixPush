package com.mixpush.example

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private var web_view: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        web_view = findViewById<WebView>(R.id.web_view)
        web_view?.loadUrl(intent.getStringExtra("url")!!)
        web_view?.settings?.javaScriptEnabled = true
        web_view?.webViewClient = WebViewClient()
        web_view?.webChromeClient = WebChromeClient()
    }

    override fun onDestroy() {
        web_view?.destroy();
        super.onDestroy()
    }
}
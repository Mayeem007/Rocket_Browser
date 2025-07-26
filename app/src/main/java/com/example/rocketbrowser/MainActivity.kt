package com.example.rocketbrowser

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var goButton: Button
    private lateinit var urlRefresh: ImageButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        urlInput = findViewById(R.id.url_input)
        goButton = findViewById(R.id.go_button)
        urlRefresh = findViewById(R.id.url_refresh)

        // Request write external storage permission for older Android versions

        // Configure WebView settings
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            setSupportMultipleWindows(true)
            cacheMode = WebSettings.LOAD_DEFAULT
        }
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    urlRefresh.visibility = android.view.View.GONE
                    // You could show a spinner here if desired
                } else {
                    urlRefresh.visibility = android.view.View.VISIBLE
                }
            }
        }

        goButton.setOnClickListener {
            loadUrlFromInput()
        }

        urlInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                loadUrlFromInput()
                true
            } else {
                false
            }
        }

        urlRefresh.setOnClickListener {
            urlRefresh.animate()
                .rotationBy(360f)
                .setDuration(600)
                .withEndAction { urlRefresh.rotation = 0f }
                .start()
            webView.reload()
        }

        webView.loadUrl("https://www.google.com")
    }

    private fun loadUrlFromInput() {
        var url = urlInput.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL or search term", Toast.LENGTH_SHORT).show()
            return
        }
        if (!URLUtil.isNetworkUrl(url)) {
            url = if (url.contains(" ")) {
                "https://www.google.com/search?q=${Uri.encode(url)}"
            } else {
                "https://$url"
            }
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}

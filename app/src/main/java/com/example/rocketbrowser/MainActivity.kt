package com.example.rocketbrowser

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var urlLoadingSpinner: ProgressBar
    private lateinit var urlRefresh: ImageButton

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(this, "Permission denied. Downloads may fail.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        urlInput = findViewById(R.id.url_input)
        urlLoadingSpinner = findViewById(R.id.url_loading_spinner)
        urlRefresh = findViewById(R.id.url_refresh)

        // Permission for downloads
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

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
                // Show spinner while loading, refresh only if not loading
                if (newProgress < 100) {
                    urlLoadingSpinner.visibility = android.view.View.VISIBLE
                    urlRefresh.visibility = android.view.View.GONE
                } else {
                    urlLoadingSpinner.visibility = android.view.View.GONE
                    urlRefresh.visibility = android.view.View.VISIBLE
                }
            }
        }

        urlInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO
                || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
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
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            return
        }
        // Prepend “https://” if missing and not a search query
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

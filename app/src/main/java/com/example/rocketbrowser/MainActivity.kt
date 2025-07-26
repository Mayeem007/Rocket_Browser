package com.example.rocketbrowser

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var goButton: Button

    private lateinit var loadingContainer: FrameLayout
    private lateinit var loadingIcon: ImageView

    private lateinit var pulseScaleXAnimator: ObjectAnimator
    private lateinit var pulseScaleYAnimator: ObjectAnimator
    private lateinit var refreshRotateAnimator: ObjectAnimator

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(this, "Permission denied. Downloads may fail.", Toast.LENGTH_LONG).show()
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views
        webView = findViewById(R.id.webview)
        urlInput = findViewById(R.id.url_input)
        goButton = findViewById(R.id.go_button)
        loadingContainer = findViewById(R.id.loading_container)
        loadingIcon = findViewById(R.id.loading_icon)

        // Request storage permission for API ≤ 33
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        // Configure WebView
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
                if (newProgress < 100) showLoadingIndicator() else showRefreshIndicator()
            }
        }

        // Handle Go button and Enter key
        goButton.setOnClickListener { loadUrlFromInput() }
        urlInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                loadUrlFromInput()
                true
            } else false
        }

        // Set up animations
        setupPulseAnimation()
        setupRefreshAnimation()

        // Load default page
        webView.loadUrl("https://www.google.com")
    }

    private fun loadUrlFromInput() {
        var url = urlInput.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            return
        }
        if (!URLUtil.isNetworkUrl(url)) {
            url = if (url.contains(" ")) {
                // treat as search query
                "https://www.google.com/search?q=" + Uri.encode(url)
            } else {
                "https://$url"
            }
        }
        webView.loadUrl(url)
    }

    private fun setupPulseAnimation() {
        pulseScaleXAnimator = ObjectAnimator.ofFloat(loadingIcon, View.SCALE_X, 1f, 1.2f).apply {
            duration = 600
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
        pulseScaleYAnimator = ObjectAnimator.ofFloat(loadingIcon, View.SCALE_Y, 1f, 1.2f).apply {
            duration = 600
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
    }

    private fun setupRefreshAnimation() {
        refreshRotateAnimator = ObjectAnimator.ofFloat(loadingIcon, View.ROTATION, 0f, 360f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
        }
    }

    private fun showLoadingIndicator() {
        runOnUiThread {
            if (loadingContainer.visibility != View.VISIBLE) {
                loadingContainer.visibility = View.VISIBLE
                loadingIcon.setImageResource(R.drawable.ic_capsule)
                refreshRotateAnimator.cancel()
                loadingIcon.rotation = 0f
                AnimatorSet().apply {
                    playTogether(pulseScaleXAnimator, pulseScaleYAnimator)
                    start()
                }
            }
        }
    }

    private fun showRefreshIndicator() {
        runOnUiThread {
            loadingIcon.setImageResource(R.drawable.ic_refresh)
            pulseScaleXAnimator.cancel()
            pulseScaleYAnimator.cancel()
            refreshRotateAnimator.start()
        }
    }

    fun hideLoadingIndicator() {
        runOnUiThread {
            pulseScaleXAnimator.cancel()
            pulseScaleYAnimator.cancel()
            refreshRotateAnimator.cancel()
            loadingContainer.visibility = View.GONE
            loadingIcon.rotation = 0f
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}

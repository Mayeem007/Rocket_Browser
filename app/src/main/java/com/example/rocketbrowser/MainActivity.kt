package com.example.rocketbrowser

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
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
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // UI Views
    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var goFab: FloatingActionButton

    // Loading/Refresh Indicator Views & Animators
    private lateinit var loadingContainer: FrameLayout
    private lateinit var loadingIcon: ImageView
    private lateinit var pulseAnimation: AnimatorSet
    private lateinit var rotateAnimation: ObjectAnimator

    // Permission Handling
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

        // --- View Binding ---
        webView = findViewById(R.id.webview)
        urlInput = findViewById(R.id.url_input)
        goFab = findViewById(R.id.go_fab)
        loadingContainer = findViewById(R.id.loading_container)
        loadingIcon = findViewById(R.id.loading_icon)

        // --- Initial Setup ---
        setupPermissions()
        setupWebView()
        setupClickListeners()
        setupAnimations()

        // Load a default page
        webView.loadUrl("https://www.google.com")
    }

    private fun setupPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun setupWebView() {
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
                super.onProgressChanged(view, newProgress)
                if (newProgress < 100) {
                    showLoadingIndicator()
                } else {
                    showRefreshIndicator()
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Handle "Go" FAB click
        goFab.setOnClickListener { loadUrlFromInput() }

        // Handle "Enter" key in URL input
        urlInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO || (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                loadUrlFromInput()
                true
            } else {
                false
            }
        }

        // Handle tap-to-refresh on the loading icon
        loadingContainer.setOnClickListener {
            // Only allow refresh if the page is fully loaded
            if (webView.progress == 100) {
                rotateAnimation.start()
                webView.reload()
            }
        }
    }

    private fun setupAnimations() {
        // Pulsing scale animation for loading state
        pulseAnimation = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(loadingIcon, View.SCALE_X, 1f, 1.2f).apply { repeatCount = ValueAnimator.INFINITE; repeatMode = ValueAnimator.REVERSE },
                ObjectAnimator.ofFloat(loadingIcon, View.SCALE_Y, 1f, 1.2f).apply { repeatCount = ValueAnimator.INFINITE; repeatMode = ValueAnimator.REVERSE }
            )
            duration = 800
        }

        // Single rotation animation for tap-to-refresh
        rotateAnimation = ObjectAnimator.ofFloat(loadingIcon, View.ROTATION, 0f, 360f).apply {
            duration = 600
        }
    }

    private fun loadUrlFromInput() {
        var url = urlInput.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL or search term", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepend "https://" if it's not a full URL and not a search query
        if (!URLUtil.isNetworkUrl(url) && !url.contains(" ")) {
            url = "https://$url"
        }

        // If it looks like a search query, search on Google
        if (!URLUtil.isNetworkUrl(url)) {
            webView.loadUrl("https://www.google.com/search?q=${Uri.encode(url)}")
        } else {
            webView.loadUrl(url)
        }
    }

    private fun showLoadingIndicator() {
        loadingContainer.visibility = View.VISIBLE
        // Set the icon to a simple shape or a loading-specific icon
        loadingIcon.setImageResource(R.drawable.ic_capsule)
        if (!pulseAnimation.isRunning) {
            rotateAnimation.cancel()
            loadingIcon.rotation = 0f
            pulseAnimation.start()
        }
    }

    private fun showRefreshIndicator() {
        if (pulseAnimation.isRunning) {
            pulseAnimation.end()
            loadingIcon.scaleX = 1f
            loadingIcon.scaleY = 1f
        }
        loadingContainer.visibility = View.VISIBLE
        loadingIcon.setImageResource(R.drawable.ic_refresh)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}

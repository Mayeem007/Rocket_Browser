package com.example.rocketbrowser

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@SuppressLint("SourceLockedOrientationActivity")
class VideoPlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var videoUriString: String? = null

    private lateinit var downloadButton: Button
    private lateinit var fullscreenButton: ImageButton
    private var isFullscreen = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            } else {
                videoUriString?.let { startDownload(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Bind views
        val playerView = findViewById<PlayerView>(R.id.player_view)
        downloadButton = findViewById(R.id.download_button)
        fullscreenButton = findViewById(R.id.fullscreen_button)

        // Get and validate video URL
        videoUriString = intent.getStringExtra("video_uri")
        if (videoUriString.isNullOrBlank()) {
            Toast.makeText(this, "No video URL provided", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build().also { exo ->
            playerView.player = exo
            exo.setMediaItem(MediaItem.fromUri(Uri.parse(videoUriString!!)))
            exo.prepare()
            exo.play()
        }

        // Download button logic
        downloadButton.setOnClickListener {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    return@setOnClickListener
                }
            }
            startDownload(videoUriString!!)
        }

        // Full-screen toggle logic
        fullscreenButton.setOnClickListener { toggleFullScreen() }
    }

    private fun startDownload(url: String) {
        val fileName = URLUtil.guessFileName(url, null, null)
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle(fileName)
            setDescription("Downloading video…")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(this, "Download started: $fileName", Toast.LENGTH_SHORT).show()
    }

    private fun toggleFullScreen() {
        val container = findViewById<View>(R.id.video_container)
        val params = container.layoutParams

        if (isFullscreen) {
            // Exit full-screen
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            supportActionBar?.show()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            params.height = resources.getDimensionPixelSize(R.dimen.video_normal_height)
        } else {
            // Enter full-screen
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            supportActionBar?.hide()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        container.layoutParams = params
        isFullscreen = !isFullscreen
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}

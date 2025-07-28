package com.example.rocketbrowser

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Rational
import android.view.View
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
// Remove Media3 imports:
// import androidx.media3.common.MediaItem
// import androidx.media3.common.Player
// import androidx.media3.exoplayer.ExoPlayer
// import androidx.media3.ui.PlayerView

// Add these imports if not present:
import android.widget.VideoView
import android.widget.MediaController

@SuppressLint("SourceLockedOrientationActivity")
class VideoPlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var videoUriString: String? = null

    private lateinit var downloadButton: Button
    private lateinit var fullscreenButton: ImageButton
    private lateinit var pipButton: ImageButton
    private var isFullscreen = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            } else {
                videoUriString?.let { startDownload(it) }
            }
        }

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Bind views
        videoView = findViewById(R.id.video_view)
        downloadButton = findViewById(R.id.download_button)
        fullscreenButton = findViewById(R.id.fullscreen_button)
        pipButton = findViewById(R.id.pip_button)

        // Get and validate video URL
        videoUriString = intent.getStringExtra("video_uri")
        if (videoUriString.isNullOrBlank()) {
            Toast.makeText(this, "No video URL provided", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize VideoView
        videoView.setVideoURI(Uri.parse(videoUriString!!))
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()

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
        
        // PiP button logic
        pipButton.setOnClickListener { enterPictureInPictureMode() }
    }
    
    private fun updatePictureInPictureParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(16, 9)
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            setPictureInPictureParams(params)
        }
    }
    
    private fun enterPictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updatePictureInPictureParams()
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
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
        if (isFullscreen) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            if (supportActionBar != null) {
                supportActionBar!!.show()
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isFullscreen = false
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            )
            if (supportActionBar != null) {
                supportActionBar!!.hide()
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            isFullscreen = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInPictureInPictureMode) {
            // Continue playback in PiP mode
        } else {
            player?.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    // Update onDestroy to stop VideoView
    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}

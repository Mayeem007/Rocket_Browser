@file:Suppress("DEPRECATION")

package com.example.rocketbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import android.webkit.URLUtil

private var VideoPlayerActivity.player: ExoPlayer

class VideoPlayerActivity : AppCompatActivity() {
    // existing vars ...
    private lateinit var downloadButton: Button
    private var videoUriString: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        downloadButton = findViewById(R.id.download_button)
        val playerView = findViewById<PlayerView>(R.id.player_view)
        player = ExoPlayer.Builder(this).build().also { exo ->
            playerView.player = exo
            videoUriString = intent.getStringExtra("video_uri")
            videoUriString?.let {
                val mediaItem = MediaItem.fromUri(Uri.parse(it))
                exo.setMediaItem(mediaItem)
                exo.prepare()
                exo.play()
            }
        }

        downloadButton.setOnClickListener {
            videoUriString?.let { url ->
                startDownload(url)
            } ?: Toast.makeText(this, "No video URL to download", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startDownload(url: String) {
        val fileName = URLUtil.guessFileName(url, null, null)
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle(fileName)
            setDescription("Downloading video...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(this, "Download started: $fileName", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}
private var isFullscreen = false
private lateinit var fullscreenButton: ImageButton

override fun onCreate(savedInstanceState: Bundle?) {
    // ... existing code ...

    fullscreenButton = findViewById(R.id.fullscreen_button)
    fullscreenButton.setOnClickListener {
        toggleFullScreen()
    }
}

private fun toggleFullScreen() {
    if (isFullscreen) {
        // Exit fullscreen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        supportActionBar?.show()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportFragmentManager.findFragmentById(R.id.video_fragment)?.view?.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.video_normal_height))
        isFullscreen = false
    } else {
        // Enter fullscreen
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        supportFragmentManager.findFragmentById(R.id.video_fragment)?.view?.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        isFullscreen = true
    }
}


package com.example.rocketbrowser.presentation.player.ui

// Remove Media3 imports:
// import androidx.media3.common.MediaItem
// import androidx.media3.exoplayer.ExoPlayer
// import androidx.media3.ui.PlayerView

// Add imports:
import android.widget.VideoView
import android.widget.MediaController

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Modifier

@Composable
fun PlayerScreen(uri: Uri) {
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(uri)
                val controller = MediaController(ctx)
                controller.setAnchorView(this)
                setMediaController(controller)
                start()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

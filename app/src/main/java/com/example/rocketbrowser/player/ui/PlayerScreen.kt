package com.example.rocketbrowser.player.ui

@Composable
fun PlayerScreen(uri: Uri) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare(); play()
        }
    }
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
    AndroidView(factory = { context ->
        PlayerView(context).apply {
            player = this@apply; useController = true
        }
    }, modifier = Modifier.fillMaxSize())
}

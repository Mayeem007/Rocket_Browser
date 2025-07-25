package com.example.rocketbrowser

import android.app.Service
import android.content.Intent
import android.os.IBinder

// This service is a stub, since DownloadManager (in MainActivity) manages all downloads.
class MyDownloadService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}

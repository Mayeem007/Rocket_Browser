package com.example.rocketbrowser.presentation.browser.ui

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.web.*
import com.example.rocketbrowser.presentation.browser.viewmodel.BrowserEvent
import com.example.rocketbrowser.presentation.browser.viewmodel.BrowserState
import com.example.rocketbrowser.presentation.browser.viewmodel.TabInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    state: BrowserState,
    onEvent: (BrowserEvent) -> Unit
) {
    val currentTab = if (state.tabs.isNotEmpty() && state.currentTabIndex in state.tabs.indices) {
        state.tabs[state.currentTabIndex]
    } else {
        null
    }
    
    val webViewState = rememberWebViewState(url = currentTab?.url ?: state.url)
    val webViewNavigator = rememberWebViewNavigator()
    
    Scaffold(
        topBar = {
            Column {
                // Tab row
                TabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = state.currentTabIndex
                ) {
                    state.tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = index == state.currentTabIndex,
                            onClick = { onEvent(BrowserEvent.SwitchTab(index)) },
                            text = { Text(tab.title.ifEmpty { "New Tab" }) }
                        )
                    }
                    // Add tab button
                    IconButton(onClick = { onEvent(BrowserEvent.AddTab()) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Tab")
                    }
                }
                
                // URL bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { 
                        if (webViewNavigator.canGoBack) {
                            webViewNavigator.navigateBack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    
                    IconButton(onClick = { 
                        if (webViewNavigator.canGoForward) {
                            webViewNavigator.navigateForward()
                        }
                    }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
                    }
                    
                    TextField(
                        value = state.url,
                        onValueChange = { onEvent(BrowserEvent.LoadUrl(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Enter URL or search") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            onEvent(BrowserEvent.LoadUrl(state.url))
                        })
                    )
                    
                    IconButton(onClick = { onEvent(BrowserEvent.Refresh) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    
                    // Bookmark button
                    val isBookmarked = state.bookmarks.any { it.url == state.url }
                    IconButton(onClick = { 
                        if (isBookmarked) {
                            onEvent(BrowserEvent.RemoveBookmark(state.url))
                        } else {
                            onEvent(BrowserEvent.AddBookmark(state.url, webViewState.pageTitle ?: ""))
                        }
                    }) {
                        Icon(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Remove Bookmark" else "Add Bookmark"
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            WebView(
                state = webViewState,
                navigator = webViewNavigator,
                onCreated = { webView ->
                    webView.settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        setSupportMultipleWindows(true)
                    }
                },
                client = remember {
                    object : AccompanistWebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            onEvent(BrowserEvent.UpdateTabInfo(
                                state.currentTabIndex,
                                view.title ?: "",
                                null // You could implement favicon loading here
                            ))
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            
            // Loading indicator
            if (webViewState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

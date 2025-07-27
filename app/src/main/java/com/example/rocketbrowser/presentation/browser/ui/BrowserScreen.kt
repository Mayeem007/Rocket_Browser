package com.example.rocketbrowser.presentation.browser.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.web.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    state: BrowserState,
    onEvent: (BrowserEvent) -> Unit
) {
    val webState = rememberWebViewState(state.url)
    Scaffold(
        topBar = {
            TextField(
                value = state.url,
                onValueChange = { onEvent(BrowserEvent.LoadUrl(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter URL") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {
                    onEvent(BrowserEvent.LoadUrl(state.url))
                })
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            WebView(
                state = webState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

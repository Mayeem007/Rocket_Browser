package com.example.rocketbrowser.presentation.browser.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rocketbrowser.presentation.browser.viewmodel.BrowserViewModel

@Composable
fun BrowserApp(viewModel: BrowserViewModel = hiltViewModel()) {
    BrowserScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

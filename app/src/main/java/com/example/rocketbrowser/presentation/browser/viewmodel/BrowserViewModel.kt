package com.example.rocketbrowser.presentation.browser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BrowserState(
    val url: String = "https://www.google.com",
    val canGoBack: Boolean = false
)

sealed class BrowserEvent {
    data class LoadUrl(val url: String): BrowserEvent()
    object NavigateBack: BrowserEvent()
}

@HiltViewModel
class BrowserViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(BrowserState())
    val state: StateFlow<BrowserState> = _state

    fun onEvent(event: BrowserEvent) {
        when(event) {
            is BrowserEvent.LoadUrl -> _state.value = _state.value.copy(url = event.url)
            BrowserEvent.NavigateBack -> { /* handle back */ }
        }
    }
}

package com.example.rocketbrowser.presentation.browser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowserState(
    val url: String = "https://www.google.com",
    val canGoBack: Boolean = false,
    val isLoading: Boolean = false,
    val tabs: List<TabInfo> = listOf(TabInfo("https://www.google.com")),
    val currentTabIndex: Int = 0,
    val bookmarks: List<BookmarkInfo> = emptyList()
)

data class TabInfo(
    val url: String,
    val title: String = "",
    val favicon: String? = null
)

data class BookmarkInfo(
    val url: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis()
)

sealed class BrowserEvent {
    data class LoadUrl(val url: String): BrowserEvent()
    object NavigateBack: BrowserEvent()
    object NavigateForward: BrowserEvent()
    object Refresh: BrowserEvent()
    data class AddBookmark(val url: String, val title: String): BrowserEvent()
    data class RemoveBookmark(val url: String): BrowserEvent()
    data class AddTab(val url: String = "https://www.google.com"): BrowserEvent()
    data class SwitchTab(val index: Int): BrowserEvent()
    data class CloseTab(val index: Int): BrowserEvent()
    data class UpdateTabInfo(val index: Int, val title: String, val favicon: String? = null): BrowserEvent()
}

@HiltViewModel
class BrowserViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(BrowserState())
    val state: StateFlow<BrowserState> = _state

    fun onEvent(event: BrowserEvent) {
        when(event) {
            is BrowserEvent.LoadUrl -> {
                _state.update { it.copy(
                    url = event.url,
                    isLoading = true
                )}
            }
            is BrowserEvent.NavigateBack -> {
                // Handle back navigation
            }
            is BrowserEvent.NavigateForward -> {
                // Handle forward navigation
            }
            is BrowserEvent.Refresh -> {
                _state.update { it.copy(isLoading = true) }
            }
            is BrowserEvent.AddBookmark -> {
                val newBookmark = BookmarkInfo(event.url, event.title)
                _state.update { it.copy(
                    bookmarks = it.bookmarks + newBookmark
                )}
            }
            is BrowserEvent.RemoveBookmark -> {
                _state.update { it.copy(
                    bookmarks = it.bookmarks.filterNot { bookmark -> bookmark.url == event.url }
                )}
            }
            is BrowserEvent.AddTab -> {
                val newTab = TabInfo(event.url)
                _state.update { it.copy(
                    tabs = it.tabs + newTab,
                    currentTabIndex = it.tabs.size
                )}
            }
            is BrowserEvent.SwitchTab -> {
                if (event.index in _state.value.tabs.indices) {
                    _state.update { it.copy(
                        currentTabIndex = event.index,
                        url = it.tabs[event.index].url
                    )}
                }
            }
            is BrowserEvent.CloseTab -> {
                if (_state.value.tabs.size > 1 && event.index in _state.value.tabs.indices) {
                    val newTabs = _state.value.tabs.toMutableList().apply {
                        removeAt(event.index)
                    }
                    val newIndex = when {
                        event.index < _state.value.currentTabIndex -> _state.value.currentTabIndex - 1
                        event.index == _state.value.currentTabIndex -> minOf(_state.value.currentTabIndex, newTabs.size - 1)
                        else -> _state.value.currentTabIndex
                    }
                    _state.update { it.copy(
                        tabs = newTabs,
                        currentTabIndex = newIndex,
                        url = newTabs[newIndex].url
                    )}
                }
            }
            is BrowserEvent.UpdateTabInfo -> {
                if (event.index in _state.value.tabs.indices) {
                    val updatedTabs = _state.value.tabs.toMutableList().apply {
                        this[event.index] = this[event.index].copy(
                            title = event.title,
                            favicon = event.favicon
                        )
                    }
                    _state.update { it.copy(tabs = updatedTabs) }
                }
            }
        }
    }
}

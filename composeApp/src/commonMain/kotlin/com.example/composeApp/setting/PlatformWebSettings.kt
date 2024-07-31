package com.example.composeApp.setting

import androidx.compose.ui.graphics.Color

sealed class PlatformWebSettings {
    data class AndroidWebSettings(
        var userAgentString: String? = null,
        var javaScriptEnabled: Boolean = true,
        var javaScriptCanOpenWindowsAutomatically: Boolean = false,
        var setSupportMultipleWindows: Boolean = true,
        var domStorageEnabled: Boolean = true,
        var allowFileAccess: Boolean = true,
        var useWideViewPort: Boolean = true,
        var loadWithOverviewMode: Boolean = true,
        var loadsImagesAutomatically: Boolean = true,
        var databaseEnabled: Boolean = true,
        var mediaPlaybackRequiresUserGesture: Boolean = false,
        var textZoom: Int = 100,
        var safeBrowsingEnabled: Boolean = true,
        var layerType: Int = LayerType.HARDWARE,
    ) : PlatformWebSettings() {
        object LayerType {
            const val NONE = 0
            const val SOFTWARE = 1
            const val HARDWARE = 2
        }
    }

    data class IOSWebSettings(
        var opaque: Boolean = false,
        var backgroundColor: Color? = null,
        var underPageBackgroundColor: Color? = null,
        var bounces: Boolean = true,
        var scrollEnabled: Boolean = true,
        var showHorizontalScrollIndicator: Boolean = true,
        var showVerticalScrollIndicator: Boolean = true,
        var isInspectable: Boolean = true
    ) : PlatformWebSettings()
}

package com.example.composeApp.setting

import androidx.compose.ui.graphics.Color

class WebSettings {
    var isJavaScriptEnabled = true
    var customUserAgentString: String? = null
    var zoomLevel: Double = 1.0
    var supportZoom: Boolean = true
    var allowFileAccessFromFileURLs: Boolean = false
    var allowUniversalAccessFromFileURLs: Boolean = false
    var backgroundColor = Color.Transparent

    val androidWebSettings = PlatformWebSettings.AndroidWebSettings()
    val iOSWebSettings = PlatformWebSettings.IOSWebSettings()
}

package com.example.composeApp.web

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composeApp.jsbridge.WebViewJsBridge
import com.example.composeApp.presentation.CustomWebView

actual data class WebViewFactoryParam(val context: Context)

actual fun defaultWebViewFactory(param: WebViewFactoryParam) = android.webkit.WebView(param.context)

@Composable
actual fun ActualWebView(
    state: WebViewState,
    modifier: Modifier,
    captureBackPresses: Boolean,
    navigator: WebViewNavigator,
    webViewJsBridge: WebViewJsBridge?,
    onCreated: (NativeWebView) -> Unit,
    onDispose: (NativeWebView) -> Unit,
    factory: (WebViewFactoryParam) -> NativeWebView
) {
    CustomWebView(
        state,
        modifier,
        captureBackPresses,
        navigator,
        webViewJsBridge,
        onCreated = onCreated,
        onDispose = onDispose,
        factory = { factory(WebViewFactoryParam(it)) },
    )

}
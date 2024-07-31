package com.example.composeApp.presentation

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.composeApp.jsbridge.WebViewJsBridge
import com.example.composeApp.presentation.webview.CustomWebChromeClient
import com.example.composeApp.presentation.webview.CustomWebViewClient
import com.example.composeApp.web.AndroidWebView
import com.example.composeApp.web.WebViewNavigator
import com.example.composeApp.web.WebViewState
import com.example.composeApp.web.rememberWebViewNavigator

@Composable
fun CustomWebView(
    state: WebViewState,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    webViewJsBridge: WebViewJsBridge? = null,
    onCreated: (WebView) -> Unit = {},
    onDispose: (WebView) -> Unit = {},
    client: CustomWebViewClient = remember { CustomWebViewClient() },
    chromeClient: CustomWebChromeClient = remember { CustomWebChromeClient() },
    factory: ((Context) -> WebView)? = null,
) {
    BoxWithConstraints(modifier) {
        val width = if (constraints.hasFixedWidth) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }
        val height = if (constraints.hasFixedHeight) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        val layoutParams = FrameLayout.LayoutParams(width, height)

        CustomWebView(
            state,
            layoutParams,
            Modifier,
            captureBackPresses,
            navigator,
            webViewJsBridge,
            onCreated,
            onDispose,
            client,
            chromeClient,
            factory,
        )
    }
}

@Composable
fun CustomWebView(
    state: WebViewState,
    layoutParams: FrameLayout.LayoutParams,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    webViewJsBridge: WebViewJsBridge? = null,
    onCreated: (WebView) -> Unit = {},
    onDispose: (WebView) -> Unit = {},
    client: CustomWebViewClient = remember { CustomWebViewClient() },
    chromeClient: CustomWebChromeClient = remember { CustomWebChromeClient() },
    factory: ((Context) -> WebView)? = null,
) {
    val webView = state.webView
    val scope = rememberCoroutineScope()

    BackHandler(captureBackPresses && navigator.canGoBack) {
        webView?.goBack()
    }

    client.state = state
    client.navigator = navigator
    chromeClient.state = state

    AndroidView(
        factory = { context ->
            (factory?.invoke(context) ?: WebView(context)).apply {
                onCreated(this)

                this.layoutParams = layoutParams

                state.viewState?.let {
                    this.restoreState(it)
                }

                webChromeClient = chromeClient
                webViewClient = client


                this.setLayerType(state.webSettings.androidWebSettings.layerType, null)

                settings.apply {
                    state.webSettings.let {
                        javaScriptEnabled = it.isJavaScriptEnabled
                        userAgentString = it.customUserAgentString
                        allowFileAccessFromFileURLs = it.allowFileAccessFromFileURLs
                        allowUniversalAccessFromFileURLs = it.allowUniversalAccessFromFileURLs
                        setSupportZoom(it.supportZoom)
                    }

                    state.webSettings.androidWebSettings.let {
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                }

            }.also {
                val androidWebView = AndroidWebView(it, scope, webViewJsBridge)
                state.webView = androidWebView
                webViewJsBridge?.webView = androidWebView
            }
        },
        modifier = modifier,
        onReset = {},
        onRelease = {
            onDispose(it)
        },
    )
}
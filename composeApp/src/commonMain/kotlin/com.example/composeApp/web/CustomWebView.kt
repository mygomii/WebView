package com.example.composeApp.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.composeApp.jsbridge.WebViewJsBridge
import com.example.composeApp.util.getPlatform
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebContent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.merge


@Composable
fun CustomWebView(
    state: WebViewState,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    webViewJsBridge: WebViewJsBridge? = null,
    onCreated: () -> Unit = {},
    onDispose: () -> Unit = {},
) {
    CustomWebView(
        state = state,
        modifier = modifier,
        captureBackPresses = captureBackPresses,
        navigator = navigator,
        webViewJsBridge = webViewJsBridge,
        onCreated = { _ -> onCreated() },
        onDispose = { _ -> onDispose() },
    )
}


@Composable
fun CustomWebView(
    state: WebViewState,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    webViewJsBridge: WebViewJsBridge? = null,
    onCreated: (NativeWebView) -> Unit = {},
    onDispose: (NativeWebView) -> Unit = {},
    factory: ((WebViewFactoryParam) -> NativeWebView)? = null,
) {
    val webView = state.webView

    webView?.let { wv ->
        LaunchedEffect(wv, navigator) {
            with(navigator) {
                println("####### wv.handleNavigationEvents()")
                wv.handleNavigationEvents()
            }
        }

        LaunchedEffect(wv, state) {
            snapshotFlow { state.content }.collect { content ->
                print("####### content : $content")

                when (content) {
                    is WebContent.Url -> {
                        state.lastLoadedUrl = content.url
                        wv.loadUrl(content.url, content.additionalHttpHeaders)
                    }

                    is WebContent.Data,
                    is WebContent.File,
                    is WebContent.Post,
                    is WebContent.NavigatorOnly -> {
                        // do nothing
                    }
                }
            }
        }

        if (webViewJsBridge != null && !getPlatform().isIOS()) {
            LaunchedEffect(wv, state) {
                val loadingStateFlow = snapshotFlow { state.loadingState }.filter { it is LoadingState.Finished }
                val lastLoadedUrFlow = snapshotFlow { state.lastLoadedUrl }.filter { !it.isNullOrEmpty() }

                merge(loadingStateFlow, lastLoadedUrFlow).collect {
                    println("######## $loadingStateFlow, $lastLoadedUrFlow")
                    if (state.loadingState is LoadingState.Finished) {
                        wv.initJsBridge(webViewJsBridge)
                    }
                }
            }
        }
    }

    ActualWebView(
        state = state,
        modifier = modifier,
        captureBackPresses = captureBackPresses,
        navigator = navigator,
        webViewJsBridge = webViewJsBridge,
        onCreated = onCreated,
        onDispose = onDispose,
        factory = factory ?: ::defaultWebViewFactory,
    )

    DisposableEffect(Unit) {
        onDispose {
            println("####### WebView DisposableEffect")
            webViewJsBridge?.clear()
        }
    }
}


expect class WebViewFactoryParam


expect fun defaultWebViewFactory(param: WebViewFactoryParam): NativeWebView


@Composable
expect fun ActualWebView(
    state: WebViewState,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    webViewJsBridge: WebViewJsBridge? = null,
    onCreated: (NativeWebView) -> Unit = {},
    onDispose: (NativeWebView) -> Unit = {},
    factory: (WebViewFactoryParam) -> NativeWebView = ::defaultWebViewFactory,
)

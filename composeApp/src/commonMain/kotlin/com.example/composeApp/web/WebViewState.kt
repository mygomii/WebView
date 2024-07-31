package com.example.composeApp.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.composeApp.setting.WebSettings
import com.multiplatform.webview.cookie.CookieManager
import com.multiplatform.webview.cookie.WebViewCookieManager
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebViewError

class WebViewState(webContent: WebContent) {
    var lastLoadedUrl: String? by mutableStateOf(null)
        internal set

    var content: WebContent by mutableStateOf(webContent)
    var loadingState: LoadingState by mutableStateOf(LoadingState.Initializing)
        internal set

    val isLoading: Boolean
        get() = loadingState !is LoadingState.Finished

    var pageTitle: String? by mutableStateOf(null)
        internal set
    val errorsForCurrentRequest: SnapshotStateList<WebViewError> = mutableStateListOf()

    val webSettings: WebSettings by mutableStateOf(WebSettings())

    internal var webView by mutableStateOf<IWebView?>(null)

    val nativeWebView get() = webView?.webView ?: error("WebView is not initialized")

    var viewState: WebViewBundle? = null
        internal set

    var scrollOffset: Pair<Int, Int> = 0 to 0
        internal set

    val cookieManager: CookieManager by mutableStateOf(WebViewCookieManager())
}


@Composable
fun rememberWebViewState(
    url: String,
    additionalHttpHeaders: Map<String, String> = emptyMap(),
): WebViewState = remember {
    WebViewState(
        WebContent.Url(
            url = url,
            additionalHttpHeaders = additionalHttpHeaders,
        ),
    )
}.apply {
    this.content = WebContent.Url(
        url = url,
        additionalHttpHeaders = additionalHttpHeaders,
    )
}
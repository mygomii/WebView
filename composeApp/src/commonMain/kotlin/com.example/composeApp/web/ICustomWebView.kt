package com.example.composeApp.web

import com.example.composeApp.jsbridge.WebViewJsBridge
import kotlinx.coroutines.CoroutineScope

expect class NativeWebView

interface IWebView {
    val webView: NativeWebView
    val scope: CoroutineScope
    val webViewJsBridge: WebViewJsBridge?

    fun canGoBack(): Boolean

    fun canGoForward(): Boolean

    fun loadUrl(url: String, additionalHttpHeaders: Map<String, String> = emptyMap())

    fun goBack()

    fun goForward()

    fun reload()

    fun stopLoading()

    fun evaluateJavaScript(script: String, callback: ((String) -> Unit)? = null)

    fun initJsBridge(webViewJsBridge: WebViewJsBridge)

    fun initWebView() {
        webViewJsBridge?.apply {
            initJsBridge(this)
        }
    }

    fun saveState(): WebViewBundle?

    fun scrollOffset(): Pair<Int, Int>
}

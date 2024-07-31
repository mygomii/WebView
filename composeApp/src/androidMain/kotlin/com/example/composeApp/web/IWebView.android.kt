package com.example.composeApp.web

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.example.composeApp.jsbridge.BRIDGE_NAME
import com.example.composeApp.jsbridge.WebViewJsBridge
import com.multiplatform.webview.jsbridge.JsMessage
import kotlinx.coroutines.CoroutineScope

actual typealias NativeWebView = WebView

class AndroidWebView(
    override val webView: WebView,
    override val scope: CoroutineScope,
    override val webViewJsBridge: WebViewJsBridge?,
) : IWebView {
    init {
        initWebView()
    }

    override fun canGoBack() = webView.canGoBack()

    override fun canGoForward() = webView.canGoForward()

    override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
        webView.loadUrl(url, additionalHttpHeaders)
    }

    override fun goBack() {
        webView.goBack()
    }

    override fun goForward() {
        webView.goForward()
    }

    override fun reload() {
        webView.reload()
    }

    override fun stopLoading() {
        webView.stopLoading()
    }

    override fun evaluateJavaScript(script: String, callback: ((String) -> Unit)?) {
        val androidScript = "javascript:$script"
        println("####### evaluateJavaScript: $androidScript")
        webView.post {
            webView.evaluateJavascript(androidScript, callback)
        }
    }


    override fun initJsBridge(webViewJsBridge: WebViewJsBridge) {
        println("####### initJsBridge $webViewJsBridge")
        webView.addJavascriptInterface(this, BRIDGE_NAME)
    }

    @JavascriptInterface
    fun callAndroid(id: Int, method: String, params: String) {
        println("####### callAndroid call from JS: $id, $method, $params")
        webViewJsBridge?.dispatch(JsMessage(id, method, params))
    }

    override fun scrollOffset(): Pair<Int, Int> {
        return Pair(webView.scrollX, webView.scrollY)
    }

    override fun saveState(): WebViewBundle? {
        val bundle = WebViewBundle()
        return if (webView.saveState(bundle) != null) {
            bundle
        } else {
            null
        }
    }
}



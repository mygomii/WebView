package com.example.composeApp.web

import com.example.composeApp.jsbridge.BRIDGE_NAME
import com.example.composeApp.jsbridge.IosJsMessageHandler
import com.example.composeApp.jsbridge.WebViewJsBridge
import com.example.composeApp.util.getPlatformVersionDouble
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSData
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.setValue
import platform.WebKit.WKWebView
import platform.darwin.NSObject
import platform.darwin.NSObjectMeta


actual typealias NativeWebView = WKWebView

class IOSWebView(
    override val webView: WKWebView,
    override val scope: CoroutineScope,
    override val webViewJsBridge: WebViewJsBridge?,
) : IWebView {
    init {
        initWebView()
    }

    override fun canGoBack() = webView.canGoBack

    override fun canGoForward() = webView.canGoForward

    override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
        val request = NSMutableURLRequest.requestWithURL(URL = NSURL(string = url))
        additionalHttpHeaders.all { (key, value) ->
            request.setValue(value = value, forHTTPHeaderField = key)
            true
        }

        webView.loadRequest(request = request)
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
        webView.evaluateJavaScript(script) { result, error ->
            if (callback == null) {
                return@evaluateJavaScript
            }

            if (error != null) {
                println("evaluateJavaScript error: $error")
                callback.invoke(error.localizedDescription())
            } else {
                println("evaluateJavaScript result: $result")
                callback.invoke(result?.toString() ?: "")
            }
        }
    }


    override fun initJsBridge(webViewJsBridge: WebViewJsBridge) {
        println("initJsBridge")

        val jsMessageHandler = IosJsMessageHandler(webViewJsBridge)

        webView.configuration.userContentController.apply {
            addScriptMessageHandler(jsMessageHandler, BRIDGE_NAME)
        }
    }

    override fun saveState(): WebViewBundle? {
        if (getPlatformVersionDouble() < 15.0) {
            return null
        }
        val data = webView.interactionState as NSData?
        return data
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun scrollOffset(): Pair<Int, Int> {
        val offset = webView.scrollView.contentOffset
        offset.useContents {
            return Pair(x.toInt(), y.toInt())
        }
    }

    private class BundleMarker : NSObject() {
        companion object : NSObjectMeta()
    }
}

package com.example.composeApp.jsbridge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.example.composeApp.web.IWebView
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

const val BRIDGE_NAME = "" // TODO;

@Immutable
open class WebViewJsBridge(
    val navigator: WebViewNavigator? = null,
    private val jsBridgeName: String = BRIDGE_NAME
) {
    private val jsMessageDispatcher = CustomJsMessageDispatcher()
    var webView: IWebView? = null

    fun register(handler: IJsMessageHandler) {
        print("##### register!!!")
        jsMessageDispatcher.registerJSHandler(handler)
    }

    fun unregister(handler: IJsMessageHandler) {
        jsMessageDispatcher.unregisterJSHandler(handler)
    }

    fun clear() {
        jsMessageDispatcher.clear()
    }

    fun dispatch(message: JsMessage) {
        println("##### message!!! ${message.callbackId}, ${message.methodName}, ${message.params}")

        jsMessageDispatcher.dispatch(message, navigator) {
            onCallback(it, message.callbackId)
        }
    }

    private fun onCallback(data: String, callbackId: Int) {
        webView?.evaluateJavaScript("window.$jsBridgeName.onCallback($callbackId, '$data')")
    }
}


@Composable
fun rememberWebViewJsBridge(navigator: WebViewNavigator? = null): WebViewJsBridge = remember { WebViewJsBridge(navigator) }

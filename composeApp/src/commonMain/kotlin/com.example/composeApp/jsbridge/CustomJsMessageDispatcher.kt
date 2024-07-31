package com.example.composeApp.jsbridge

import androidx.compose.runtime.Immutable

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

@Immutable
internal class CustomJsMessageDispatcher {
    private val jsHandlerMap = mutableMapOf<String, IJsMessageHandler>()

    fun registerJSHandler(handler: IJsMessageHandler) {
        jsHandlerMap[handler.methodName()] = handler
    }

    fun dispatch(message: JsMessage, navigator: WebViewNavigator? = null, callback: (String) -> Unit) {
        print("####### dispatch $message")
        jsHandlerMap[message.methodName]?.handle(message, navigator, callback)
    }

    fun canHandle(id: String) = jsHandlerMap.containsKey(id)

    fun unregisterJSHandler(handler: IJsMessageHandler) {
        jsHandlerMap.remove(handler.methodName())
    }

    fun clear() {
        jsHandlerMap.clear()
    }
}

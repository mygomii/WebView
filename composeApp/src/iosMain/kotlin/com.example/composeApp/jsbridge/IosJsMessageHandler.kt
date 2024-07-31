package com.example.composeApp.jsbridge

import com.multiplatform.webview.jsbridge.JsMessage
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.darwin.NSObject

class IosJsMessageHandler(private val webViewJsBridge: WebViewJsBridge) : WKScriptMessageHandlerProtocol, NSObject() {
    override fun userContentController(userContentController: WKUserContentController, didReceiveScriptMessage: WKScriptMessage) {
        println("##### didReceiveScriptMessage: $didReceiveScriptMessage")

        val body = didReceiveScriptMessage.body
        val method = didReceiveScriptMessage.name

        println("##### didReceiveScriptMessage: $body, $method")

        (body as String).apply {
            println("JsMessageHandler: $body")
            webViewJsBridge.dispatch(JsMessage(1, "ios", body))
        }
    }


}

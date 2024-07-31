package com.example.composeApp.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class CustomJsMessageHandler : IJsMessageHandler {
    override fun methodName(): String {
        return "Greet"
    }

    override fun handle(message: JsMessage, navigator: WebViewNavigator?, callback: (String) -> Unit) {
        println(
            "Greet Handler Get Message: $message"
        )
//        val param = processParams<GreetModel>(message)
//        val data = GreetModel("KMM Received ${param.message}")
//        callback(dataToJsonString(data))
////        EventBus.post(NavigationEvent())
//        navigator?.coroutineScope?.launch {
//            FlowEventBus.publishEvent(NavigationEvent())
//        }
    }
}

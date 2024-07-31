package com.example.composeApp.presentations

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.composeApp.jsbridge.CustomJsMessageHandler
import com.example.composeApp.jsbridge.WebViewJsBridge
import com.example.composeApp.jsbridge.rememberWebViewJsBridge
import com.example.composeApp.web.CustomWebView
import com.example.composeApp.web.rememberWebViewState

const val BASE_URL = "" // TODO; insert base url

@Composable
fun Main() {
    val webViewState = rememberWebViewState(url = BASE_URL)
    val jsBridge = rememberWebViewJsBridge()

    LaunchedEffect(Unit) {
        initJsBridge(jsBridge)
    }

    CustomWebView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        state = webViewState,
        webViewJsBridge = jsBridge,
    )
}

fun initJsBridge(webViewJsBridge: WebViewJsBridge) {
    println("##### initJsBridge $webViewJsBridge")
    webViewJsBridge.register(CustomJsMessageHandler())
}


//fun initWebView(webViewState: WebViewState) {
//    webViewState.webSettings.androidWebSettings.apply {
//        userAgentString = ""
//        javaScriptEnabled = true
//        javaScriptCanOpenWindowsAutomatically = false
////        setSupportMultipleWindows(true)
//        domStorageEnabled = true
//        allowFileAccess = true
//        useWideViewPort = true
//        loadWithOverviewMode = true
//        loadsImagesAutomatically = true
//        databaseEnabled = true
//        mediaPlaybackRequiresUserGesture = false
//        textZoom = 100
////        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//        safeBrowsingEnabled = true
//
//    }
//
//    webViewState.webSettings.iOSWebSettings.apply {
//        isInspectable = true
//    }

//}

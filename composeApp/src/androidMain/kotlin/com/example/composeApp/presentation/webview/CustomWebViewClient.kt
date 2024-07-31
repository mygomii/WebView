package com.example.composeApp.presentation.webview

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.composeApp.web.WebViewNavigator
import com.example.composeApp.web.WebViewState
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebViewError

open class CustomWebViewClient : WebViewClient() {
    open lateinit var state: WebViewState
        internal set
    open lateinit var navigator: WebViewNavigator
        internal set
    private var isRedirect = false

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        println("####### shouldOverrideUrlLoading: ${request?.url} ${request?.isForMainFrame} ${request?.isRedirect} ${request?.method}")
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        println("####### onPageStarted: $url")
        state.loadingState = LoadingState.Loading(0.0f)
        state.errorsForCurrentRequest.clear()
        state.pageTitle = null
        state.lastLoadedUrl = url
    }

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
        println("####### onPageFinished: $url")
        state.loadingState = LoadingState.Finished
        state.lastLoadedUrl = url
    }

    override fun onReceivedError(view: WebView, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        println("onReceivedError: ${error?.description}")

        if (error != null) {
            state.errorsForCurrentRequest.add(WebViewError(error.errorCode, error.description.toString()))
        }
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        // TODO;
    }
}

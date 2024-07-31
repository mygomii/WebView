package com.example.composeApp.presentation.webview

import android.net.Uri
import android.os.Message
import android.view.View
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.composeApp.web.WebViewState
import com.multiplatform.webview.web.LoadingState

open class CustomWebChromeClient : WebChromeClient() {
    open lateinit var state: WebViewState
        internal set
    private var lastLoadedUrl = ""

    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        println("####### $message")
        return true
    }

    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        super.onPermissionRequest(request)
    }

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        super.onHideCustomView()
    }

    override fun onReceivedTitle(view: WebView, title: String?) {
        super.onReceivedTitle(view, title)
        println("####### onReceivedTitle: $title url:${view.url}")
        state.pageTitle = title
        state.lastLoadedUrl = view.url ?: ""
    }


    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)

        if (state.loadingState == LoadingState.Finished && view.url == lastLoadedUrl) {
            return
        }

        state.loadingState = if (newProgress == 100) {
            LoadingState.Finished
        } else {
            LoadingState.Loading(newProgress / 100.0f)
        }

        lastLoadedUrl = view.url ?: ""
    }
}

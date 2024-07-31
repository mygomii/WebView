package com.example.composeApp.request

import com.example.composeApp.web.WebViewNavigator


interface RequestInterceptor {
    fun onInterceptUrlRequest(request: WebRequest, navigator: WebViewNavigator): WebRequestInterceptResult
}

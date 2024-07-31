package com.example.composeApp.request

sealed interface WebRequestInterceptResult {
    data object Allow : WebRequestInterceptResult
    data object Reject : WebRequestInterceptResult
    class Modify(val request: WebRequest) : WebRequestInterceptResult
}

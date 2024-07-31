package com.example.composeApp.web

import com.multiplatform.webview.web.LoadingState
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import observer.ObserverProtocol
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.darwin.NSObject

@ExperimentalForeignApi
class WKWebViewObserver(private val state: WebViewState, private val navigator: WebViewNavigator) : NSObject(), ObserverProtocol {
    override fun observeValueForKeyPath(keyPath: String?, ofObject: Any?, change: Map<Any?, *>?, context: COpaquePointer?) {
        if (keyPath.isNullOrEmpty()) {
            return
        }

        when (KeyPath.getKey(keyPath)) {
            KeyPath.EstimatedProgress -> {
                val progress = change?.get("new") as? NSNumber
                println("###### Observe estimatedProgress Changed $progress")
                if (progress != null) {
                    state.loadingState = LoadingState.Loading(progress.floatValue)
                    if (progress.floatValue >= 1.0f) {
                        state.loadingState = LoadingState.Finished
                    }
                }
            }

            KeyPath.Title -> {
                val title = change?.get("new") as? String
                println("###### Observe title Changed $title")
                if (title != null) {
                    state.pageTitle = title
                }
            }

            KeyPath.Url -> {
                val url = change?.get("new") as? NSURL
                println("###### Observe URL Changed ${url?.absoluteString}")
                if (url != null) {
                    state.lastLoadedUrl = url.absoluteString
                }
            }

            KeyPath.CanGoBack -> {
                val canGoBack = change?.get("new") as? NSNumber
                println("######Observe canGoBack Changed $canGoBack")
                if (canGoBack != null) {
                    navigator.canGoBack = canGoBack.boolValue
                }
            }

            KeyPath.CanGoForward -> {
                val canGoForward = change?.get("new") as? NSNumber
                println("###### Observe canGoForward Changed $canGoForward")
                if (canGoForward != null) {
                    navigator.canGoForward = canGoForward.boolValue
                }
            }

            null -> {
                // do nothing
            }
        }
    }
}

enum class KeyPath(val value: String) {
    EstimatedProgress("estimatedProgress"),
    Title("title"),
    Url("URL"),
    CanGoBack("canGoBack"),
    CanGoForward("canGoForward");

    companion object {
        fun getKey(value: String): KeyPath? {
            return entries.firstOrNull { it.value == value }
        }

        var keyPaths = entries.map {
            it.value
        }
    }
}





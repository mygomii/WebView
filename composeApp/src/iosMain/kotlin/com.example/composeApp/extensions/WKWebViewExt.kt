package com.example.composeApp.extensions

import com.example.composeApp.web.KeyPath.Companion.keyPaths
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.WebKit.WKWebView
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
fun WKWebView.addObservers(observer: NSObject, properties: List<String>) {
    properties.forEach {
        this.addObserver(
            observer,
            forKeyPath = it,
            options = platform.Foundation.NSKeyValueObservingOptionNew,
            context = null,
        )
    }
}


fun WKWebView.removeObservers(observer: NSObject, properties: List<String>) {
    properties.forEach {
        this.removeObserver(observer, forKeyPath = it)
    }
}

fun WKWebView.addProgressObservers(observer: NSObject) {
    this.addObservers(
        observer = observer,
        properties = keyPaths,
    )

}

fun WKWebView.removeProgressObservers(observer: NSObject) {
    this.removeObservers(
        observer = observer,
        properties = keyPaths,
    )
}
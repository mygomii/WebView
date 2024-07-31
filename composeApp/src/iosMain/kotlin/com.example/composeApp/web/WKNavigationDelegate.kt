package com.example.composeApp.web

import com.example.composeApp.request.WebRequest
import com.example.composeApp.request.WebRequestInterceptResult
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebViewError
import platform.Foundation.HTTPMethod
import platform.Foundation.NSError
import platform.Foundation.allHTTPHeaderFields
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

@Suppress("CONFLICTING_OVERLOADS")
class WKNavigationDelegate(
    private val state: WebViewState,
    private val navigator: WebViewNavigator,
) : NSObject(), WKNavigationDelegateProtocol {
    private var isRedirect = false

    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        state.loadingState = LoadingState.Loading(0f)
        state.lastLoadedUrl = webView.URL.toString()
        state.errorsForCurrentRequest.clear()
    }

    override fun webView(webView: WKWebView, didFailProvisionalNavigation: WKNavigation?, withError: NSError) {
        state.errorsForCurrentRequest.add(
            WebViewError(
                withError.code.toInt(),
                withError.localizedDescription,
            ),
        )

    }

    override fun webView(webView: WKWebView, decidePolicyForNavigationAction: WKNavigationAction, decisionHandler: (WKNavigationActionPolicy) -> Unit) {
        val url = decidePolicyForNavigationAction.request.URL?.absoluteString

        if (url != null && !isRedirect && navigator.requestInterceptor != null && decidePolicyForNavigationAction.targetFrame?.mainFrame == true) {
            navigator.requestInterceptor.apply {
                val request = decidePolicyForNavigationAction.request
                val headerMap = mutableMapOf<String, String>()
                request.allHTTPHeaderFields?.forEach {
                    headerMap[it.key.toString()] = it.value.toString()
                }

                val webRequest = WebRequest(
                    request.URL?.absoluteString ?: "",
                    headerMap,
                    decidePolicyForNavigationAction.targetFrame?.mainFrame ?: false,
                    isRedirect,
                    request.HTTPMethod ?: "GET",
                )
                val interceptResult =
                    navigator.requestInterceptor.onInterceptUrlRequest(
                        webRequest,
                        navigator,
                    )
                when (interceptResult) {
                    is WebRequestInterceptResult.Allow -> {
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
                    }

                    is WebRequestInterceptResult.Reject -> {
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                    }

                    is WebRequestInterceptResult.Modify -> {
                        isRedirect = true
                        interceptResult.request.apply {
                            navigator.stopLoading()
                            navigator.loadUrl(this.url, this.headers)
                        }
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                    }
                }
            }
        } else {
            isRedirect = false
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
        }
    }
}

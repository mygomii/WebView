package com.example.composeApp.cookie

import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.CookieManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSDate
import platform.Foundation.NSHTTPCookie
import platform.Foundation.NSHTTPCookieDomain
import platform.Foundation.NSHTTPCookieExpires
import platform.Foundation.NSHTTPCookieName
import platform.Foundation.NSHTTPCookiePath
import platform.Foundation.NSHTTPCookieValue
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.WebKit.WKHTTPCookieStore
import platform.WebKit.WKWebsiteDataStore
import kotlin.coroutines.resumeWithException

object IOSCookieManager : CookieManager {
    private val cookieStore: WKHTTPCookieStore =
        WKWebsiteDataStore.defaultDataStore().httpCookieStore

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCookies(url: String): List<Cookie> = suspendCancellableCoroutine {
        val cookieList = mutableListOf<Cookie>()
        cookieStore.getAllCookies { cookies ->
            cookies?.forEach { cookie ->
                if (cookie is NSHTTPCookie) {
                    println("######## IOSCookieManager getCookies: name: ${cookie.name}, value: ${cookie.value} url: $url, domain: ${cookie.domain}")
                    if (url.contains(cookie.domain.removePrefix("."))) {
                        cookieList.add(
                            Cookie(
                                name = cookie.name,
                                value = cookie.value,
                                domain = cookie.domain,
                                path = cookie.path,
                                expiresDate = cookie.expiresDate?.timeIntervalSince1970?.toLong(),
                                isSessionOnly = cookie.isSessionOnly(),
                                sameSite = null,
                                isSecure = cookie.isSecure(),
                                isHttpOnly = cookie.isHTTPOnly(),
                                maxAge = null,
                            ),
                        )
                    }
                }
            }
            it.resume(cookieList, {})
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun removeAllCookies() = suspendCancellableCoroutine {
        cookieStore.getAllCookies { cookies ->
            cookies?.forEach { cookie ->
                cookieStore.deleteCookie(cookie as NSHTTPCookie) {}
            }
            println("##### IOSCookieManager removeAllCookies: $cookies")
            it.resume(Unit, {})
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun removeCookies(url: String) = suspendCancellableCoroutine {
        cookieStore.getAllCookies { cookies ->
            cookies?.filter { cookie ->
                cookie is NSHTTPCookie && url.contains(cookie.domain)
            }?.forEach { cookie ->
                cookieStore.deleteCookie(cookie as NSHTTPCookie) {}
            }
            it.resume(Unit, {})
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun setCookie(url: String, cookie: Cookie) = suspendCancellableCoroutine {
        val iCookie = NSHTTPCookie.cookieWithProperties(
            mapOf(
                NSHTTPCookieName to cookie.name,
                NSHTTPCookieValue to cookie.value,
                NSHTTPCookieDomain to (cookie.domain ?: ""),
                NSHTTPCookiePath to (cookie.path ?: "/"),
                NSHTTPCookieExpires to (
                        cookie.expiresDate?.let {
                            NSDate.dateWithTimeIntervalSince1970(
                                it.toDouble(),
                            )
                        }
                        ),
            ).filterValues { it != null },
        )
        if (iCookie == null) {
            it.resumeWithException(Exception("Cookie properties are invalid."))
        }
        cookieStore.setCookie(
            iCookie!!,
            completionHandler = {
                it.resume(Unit, {})
                println("iOSCookieManager IOSCookieManager setCookie: $cookie")
            },
        )
    }
}

//@Suppress("FunctionName")
//actual fun WebViewCookieManager(): CookieManager = IOSCookieManager
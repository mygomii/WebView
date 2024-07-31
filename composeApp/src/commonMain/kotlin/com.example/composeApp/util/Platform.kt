package com.example.composeApp.util

internal sealed class Platform {
    data object Android : Platform()
    data object IOS : Platform()

    fun isAndroid() = this is Android
    fun isIOS() = this is IOS
}

internal expect fun getPlatform(): Platform

internal expect fun getPlatformVersion(): String

internal expect fun getPlatformVersionDouble(): Double

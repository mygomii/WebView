package com.example.composeApp.cookie

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeZoneForSecondsFromGMT
import platform.Foundation.timeZoneWithName
//
//actual fun getCookieExpirationDate(expiresDate: Long): String {
//    val date = NSDate.dateWithTimeIntervalSince1970(expiresDate.toDouble())
//    val dateFormatter =
//        NSDateFormatter().apply {
//            dateFormat = "EEE, dd MMM yyyy hh:mm:ss z"
//            locale = NSLocale.currentLocale()
//            timeZone = NSTimeZone.timeZoneWithName("GMT") ?: NSTimeZone.timeZoneForSecondsFromGMT(0)
//        }
//    return dateFormatter.stringFromDate(date)
//}

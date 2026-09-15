package com.shurajcodx.appratingdialog

import android.content.Context
import android.net.Uri

/**
 * Supported app store targets for rating redirection.
 */
enum class StoreType {
    /**
     * Standard Google Play Store (market://details?id=...)
     */
    GOOGLE_PLAY,

    /**
     * Amazon Appstore (amzn://apps/android?p=...)
     */
    AMAZON,

    /**
     * Huawei AppGallery (appmarket://details?id=...)
     */
    HUAWEI,

    /**
     * Samsung Galaxy Store (samsungapps://ProductDetail/...)
     */
    SAMSUNG,

    /**
     * Custom store URI or URL provided via setStoreLink(...)
     */
    CUSTOM;

    /**
     * Builds the primary deep-link Uri to open the store application directly.
     */
    fun getStoreUri(context: Context, customUrl: String? = null): Uri {
        val packageName = context.packageName
        return when (this) {
            GOOGLE_PLAY -> Uri.parse("market://details?id=$packageName")
            AMAZON -> Uri.parse("amzn://apps/android?p=$packageName")
            HUAWEI -> Uri.parse("appmarket://details?id=$packageName")
            SAMSUNG -> Uri.parse("samsungapps://ProductDetail/$packageName")
            CUSTOM -> Uri.parse(customUrl ?: "market://details?id=$packageName")
        }
    }

    /**
     * Builds the web fallback URL if the store app is not installed on the device.
     */
    fun getWebFallbackUrl(context: Context, customUrl: String? = null): String {
        val packageName = context.packageName
        return when (this) {
            GOOGLE_PLAY -> "https://play.google.com/store/apps/details?id=$packageName"
            AMAZON -> "https://www.amazon.com/gp/mas/dl/android?p=$packageName"
            HUAWEI -> "https://appgallery.huawei.com/app/C$packageName"
            SAMSUNG -> "https://galaxy.store/$packageName"
            CUSTOM -> customUrl ?: "https://play.google.com/store/apps/details?id=$packageName"
        }
    }
}

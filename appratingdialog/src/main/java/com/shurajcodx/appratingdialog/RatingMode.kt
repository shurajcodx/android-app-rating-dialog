package com.shurajcodx.appratingdialog

/**
 * Operating mode for the rating prompt.
 */
enum class RatingMode {
    /**
     * Attempts the Google Play In-App Review API first.
     * If the API is unavailable, the device lacks Google Play Services, or
     * request fails, gracefully falls back to the custom dialog / store redirect.
     */
    HYBRID,

    /**
     * Strictly uses the native Google Play In-App Review API.
     * Reviews happen completely in-app without redirection.
     */
    IN_APP_REVIEW_ONLY,

    /**
     * Uses the fully customizable in-app dialog with buttons
     * to redirect to the configured store, remind later, or decline.
     */
    CUSTOM_DIALOG
}

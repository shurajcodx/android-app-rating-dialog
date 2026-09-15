package com.shurajcodx.appratingdialog

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

fun interface ReviewCallback {
    fun onComplete(success: Boolean)
}

/**
 * Helper class to encapsulate Google Play In-App Review API interactions.
 */
internal class PlayReviewHelper(private val context: Context) {

    private val reviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(context)
    }

    /**
     * Attempts to find an Activity from the given context.
     */
    private fun findActivity(ctx: Context): Activity? {
        var currentContext = ctx
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }

    /**
     * Requests and launches the Google Play In-App Review flow.
     *
     * @param callback Callback invoked with `true` if review flow launched/completed successfully,
     *                 or `false` if request failed (e.g. no Play Services, quota exceeded, or network error).
     */
    fun startReview(callback: ReviewCallback) {
        val activity = findActivity(context)
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            Log.w(TAG, "Cannot launch Play Review: Context is not an active Activity.")
            callback.onComplete(false)
            return
        }

        try {
            val request = reviewManager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // Google Play In-App review flow has finished (or was dismissed).
                        callback.onComplete(true)
                    }
                } else {
                    val error = task.exception?.message ?: "Unknown error"
                    Log.w(TAG, "Play In-App Review request failed: $error")
                    callback.onComplete(false)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while initiating Play In-App Review", e)
            callback.onComplete(false)
        }
    }

    companion object {
        private const val TAG = "AppRatingDialog"
    }
}

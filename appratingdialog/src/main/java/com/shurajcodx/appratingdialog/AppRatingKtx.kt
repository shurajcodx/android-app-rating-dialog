package com.shurajcodx.appratingdialog

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.shurajcodx.appratingdialog.listener.RatingDialog

/**
 * Kotlin DSL builder for creating and customizing an [AppRatingDialog].
 */
class AppRatingDsl(private val context: Context) {
    var triggerCount: Int = 2
    var repeatCount: Int = 5
    var mode: RatingMode = RatingMode.HYBRID
    var storeType: StoreType = StoreType.GOOGLE_PLAY
    var customStoreUrl: String? = null

    var sentimentFilterEnabled: Boolean = false
    var sentimentTitle: String? = null
    var sentimentMessage: String? = null
    var sentimentPositiveText: String? = null
    var sentimentNegativeText: String? = null

    var titleText: String? = null
    var messageText: String? = null
    var rateButtonText: String? = null
    var rateLaterButtonText: String? = null
    var neverRateButtonText: String? = null

    @ColorRes var layoutBackgroundColor: Int = 0
    @DrawableRes var layoutBackgroundResource: Int = 0
    @ColorRes var titleTextColor: Int = 0
    @ColorRes var messageTextColor: Int = 0
    @DimenRes var messageTextSize: Int = 0

    @ColorRes var rateButtonTextColor: Int = 0
    @DrawableRes var rateButtonBackground: Int = 0

    @ColorRes var rateLaterButtonTextColor: Int = 0
    @DrawableRes var rateLaterButtonBackground: Int = 0

    @ColorRes var neverRateButtonTextColor: Int = 0
    @DrawableRes var neverRateButtonBackground: Int = 0

    private var onRateListener: RatingDialog.onRate? = null
    private var onRemindMeLaterListener: RatingDialog.onRemindMeLater? = null
    private var onNeverListener: RatingDialog.onNever? = null
    private var onFeedbackListener: RatingDialog.onFeedback? = null
    private var onInAppReviewCompleteListener: RatingDialog.onInAppReviewComplete? = null

    fun onRate(action: () -> Unit) {
        onRateListener = RatingDialog.onRate { action() }
    }

    fun onRemindMeLater(action: () -> Unit) {
        onRemindMeLaterListener = RatingDialog.onRemindMeLater { action() }
    }

    fun onNever(action: () -> Unit) {
        onNeverListener = RatingDialog.onNever { action() }
    }

    fun onFeedback(action: () -> Unit) {
        onFeedbackListener = RatingDialog.onFeedback { action() }
    }

    fun onInAppReviewComplete(action: (Boolean) -> Unit) {
        onInAppReviewCompleteListener = RatingDialog.onInAppReviewComplete { success -> action(success) }
    }

    fun build(): AppRatingDialog {
        val builder = AppRatingDialog.Builder(context)
            .setTriggerCount(triggerCount)
            .setRepeatCount(repeatCount)
            .setRatingMode(mode)
            .setStoreType(storeType)
            .setSentimentFilterEnabled(sentimentFilterEnabled)

        customStoreUrl?.let { builder.setStoreLink(it) }
        sentimentTitle?.let { builder.setSentimentTitle(it) }
        sentimentMessage?.let { builder.setSentimentMessage(it) }
        sentimentPositiveText?.let { builder.setSentimentPositiveText(it) }
        sentimentNegativeText?.let { builder.setSentimentNegativeText(it) }

        titleText?.let { builder.setTitleText(it) }
        messageText?.let { builder.setMessageText(it) }
        if (titleTextColor != 0) builder.setTitleTextColor(titleTextColor)
        if (messageTextColor != 0) builder.setMessageTextColor(messageTextColor)
        if (messageTextSize != 0) builder.setMessageTextSize(messageTextSize)

        rateButtonText?.let { builder.setRateButtonText(it, onRateListener) } ?: run {
            if (onRateListener != null) {
                builder.setRateButtonText(context.getString(R.string.shurajcodx_rating_dialog_ok), onRateListener)
            }
        }
        if (rateButtonTextColor != 0) builder.setRateButtonTextColor(rateButtonTextColor)
        if (rateButtonBackground != 0) builder.setRateButtonBackground(rateButtonBackground)

        rateLaterButtonText?.let { builder.setRateLaterButtonText(it, onRemindMeLaterListener) } ?: run {
            if (onRemindMeLaterListener != null) {
                builder.setRateLaterButtonText(context.getString(R.string.shurajcodx_rating_dialog_cancel), onRemindMeLaterListener)
            }
        }
        if (rateLaterButtonTextColor != 0) builder.setRateLaterButtonTextColor(rateLaterButtonTextColor)
        if (rateLaterButtonBackground != 0) builder.setRateLaterButtonBackground(rateLaterButtonBackground)

        neverRateButtonText?.let { builder.setNeverRateButtonText(it, onNeverListener) } ?: run {
            if (onNeverListener != null) {
                builder.setNeverRateButtonText(context.getString(R.string.shurajcodx_rating_dialog_never), onNeverListener)
            }
        }
        if (neverRateButtonTextColor != 0) builder.setNeverRateButtonTextColor(neverRateButtonTextColor)
        if (neverRateButtonBackground != 0) builder.setNeverRateButtonBackground(neverRateButtonBackground)

        if (layoutBackgroundColor != 0) builder.setLayoutBackgroundColor(layoutBackgroundColor)
        if (layoutBackgroundResource != 0) builder.setLayoutBackgroundResource(layoutBackgroundResource)

        onFeedbackListener?.let { builder.setFeedbackListener(it) }
        onInAppReviewCompleteListener?.let { builder.setInAppReviewListener(it) }

        return builder.build()
    }
}

/**
 * Idiomatic Kotlin extension function to build and configure [AppRatingDialog].
 *
 * Example:
 * ```kotlin
 * appRating(context) {
 *     triggerCount = 3
 *     repeatCount = 5
 *     mode = RatingMode.HYBRID
 *     sentimentFilterEnabled = true
 *     onFeedback {
 *         // Send email or open support
 *     }
 * }.show()
 * ```
 */
inline fun appRating(context: Context, configure: AppRatingDsl.() -> Unit): AppRatingDialog {
    return AppRatingDsl(context).apply(configure).build()
}

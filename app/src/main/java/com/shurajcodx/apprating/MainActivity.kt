package com.shurajcodx.apprating

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.shurajcodx.appratingdialog.AppRatingDialog
import com.shurajcodx.appratingdialog.RatingMode
import com.shurajcodx.appratingdialog.StoreType
import com.shurajcodx.appratingdialog.appRating

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Test Google Play In-App Review (Hybrid)
        findViewById<Button>(R.id.btn_hybrid_review).setOnClickListener {
            testHybridReview()
        }

        // 2. Test Sentiment Feedback Flow (Two-Step triage)
        findViewById<Button>(R.id.btn_sentiment_flow).setOnClickListener {
            testSentimentFlow()
        }

        // 3. Test Custom Dialog (Classic Mode)
        findViewById<Button>(R.id.btn_custom_dialog).setOnClickListener {
            testCustomDialog()
        }

        // 4. Test Kotlin DSL Builder
        findViewById<Button>(R.id.btn_kotlin_dsl).setOnClickListener {
            testKotlinDsl()
        }
    }

    /**
     * 1. Smart Hybrid Mode:
     * Attempts Google Play In-App Review first.
     * If on a device without GMS or if the quota is exhausted, falls back to custom dialog.
     */
    private fun testHybridReview() {
        val dialog = AppRatingDialog.Builder(this)
            .setRatingMode(RatingMode.HYBRID)
            .setStoreType(StoreType.GOOGLE_PLAY)
            .setInAppReviewListener { success ->
                Log.d("AppRating", "In-App review complete: $success")
                Toast.makeText(this, "In-App Review flow finished (success: $success)", Toast.LENGTH_SHORT).show()
            }
            .build()

        dialog.forceShow()
    }

    /**
     * 2. Two-Step Sentiment Feedback Flow:
     * Step 1: "Enjoying the app?"
     * - Positive: Launches Play Review / Store.
     * - Negative: Routes user to feedback listener instead of the public Play Store.
     */
    private fun testSentimentFlow() {
        val dialog = AppRatingDialog.Builder(this)
            .setRatingMode(RatingMode.HYBRID)
            .setSentimentFilterEnabled(true)
            .setFeedbackListener {
                Log.d("AppRating", "Feedback requested by user")
                Toast.makeText(
                    this,
                    "Negative feedback intercepted! You can now open a feedback form or email intent.",
                    Toast.LENGTH_LONG
                ).show()
            }
            .build()

        dialog.forceShow()
    }

    /**
     * 3. Classic Custom Dialog:
     * Fully customizable dialog layout with custom icons, colors, and direct store link.
     */
    private fun testCustomDialog() {
        val dialog = AppRatingDialog.Builder(this)
            .setRatingMode(RatingMode.CUSTOM_DIALOG)
            .setLayoutBackgroundColor(R.color.colorDialogBox)
            .setIconDrawable(true, ContextCompat.getDrawable(this, R.drawable.love))
            .setTitleText("Do you like LoveTester?")
            .setTitleTextColor(R.color.white)
            .setMessageText("Please take a moment to rate it and help support new features.")
            .setMessageTextColor(R.color.white)
            .setRateButtonBackground(R.color.colorPrimaryDark)
            .setRateLaterButtonText("Remind me later") {
                Log.d("AppRating", "Remind me later clicked")
            }
            .setNeverRateButtonText("No, Thanks") {
                Log.d("AppRating", "Never rate clicked")
            }
            .build()

        dialog.forceShow()
    }

    /**
     * 4. Modern Kotlin DSL:
     * Declarative and concise syntax using `appRating(context) { ... }`.
     */
    private fun testKotlinDsl() {
        appRating(this) {
            mode = RatingMode.HYBRID
            sentimentFilterEnabled = true
            sentimentTitle = "Having fun?"
            sentimentMessage = "Let us know your thoughts!"
            sentimentPositiveText = "Absolutely! 🎉"
            sentimentNegativeText = "Needs work 🛠️"
            onFeedback {
                Toast.makeText(this@MainActivity, "Kotlin DSL feedback listener called!", Toast.LENGTH_SHORT).show()
            }
        }.forceShow()
    }
}

package com.shurajcodx.appratingdialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.shurajcodx.appratingdialog.listener.RatingDialog;

public class AppRatingDialog extends AppCompatDialog implements View.OnClickListener {
    private static final String PREFS_NAME = "rating_dialog";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String SHOW_NEVER = "show_never";
    private static final int DEFAULT_COUNT = 2;
    private static final int DEFAULT_REPEAT_COUNT = 5;

    private final Context mContext;
    private Drawable mIconDrawable;
    private boolean mActiveDialogIcon = false;
    private final SharedPreferences mSharedPrefs;
    private int mTriggerCount = DEFAULT_COUNT;
    private int mRepeatCount = DEFAULT_REPEAT_COUNT;
    private String mTitleText, mMessageText, mNeverRateButtonText, mRateLaterButtonText, mRateButtonText, mStoreLink;
    private int mLayoutBackgroundColor;
    private int mLayoutResource;
    private int mNeverRateButtonTextColor, mRateLaterButtonTextColor, mTitleTextColor, mMessageTextColor, mRateButtonTextColor;
    private float mMessageTextSize;
    private int mNeverRateButtonBackground, mRateLaterButtonBackground, mRateButtonBackground;

    // v2.0 additions: RatingMode, StoreType, Sentiment triage
    private RatingMode mRatingMode = RatingMode.HYBRID;
    private StoreType mStoreType = StoreType.GOOGLE_PLAY;
    private boolean mSentimentFilterEnabled = false;
    private String mSentimentTitleText, mSentimentMessageText, mSentimentPositiveText, mSentimentNegativeText;

    private TextView txtTitleDialog;
    private TextView txtMessageDialog;
    private TextView btnNeverRate;
    private TextView btnRateLater;
    private TextView btnRate;
    private ImageView iconDialog;
    private RelativeLayout layoutDialogRating;

    private LinearLayout layoutSentimentButtons;
    private TextView btnSentimentPositive;
    private TextView btnSentimentNegative;
    private TextView btnSentimentDismiss;
    private LinearLayout layoutRatingButtons;

    private RatingDialog.onRemindMeLater onRemindMeLater;
    private RatingDialog.onNever onNever;
    private RatingDialog.onRate onRate;
    private RatingDialog.onFeedback onFeedback;
    private RatingDialog.onInAppReviewComplete onInAppReviewComplete;

    private AppRatingDialog(Context context) {
        super(context);

        mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mTitleText = context.getString(R.string.shurajcodx_rating_dialog_title);
        mMessageText = context.getString(R.string.shurajcodx_rating_dialog_subtitle);
        mNeverRateButtonText = context.getString(R.string.shurajcodx_rating_dialog_never);
        mRateLaterButtonText = context.getString(R.string.shurajcodx_rating_dialog_cancel);
        mRateButtonText = context.getString(R.string.shurajcodx_rating_dialog_ok);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setContentView(R.layout.shurajcodx_dialog_rating);

        initUI();
        setListener();
    }

    private void initUI() {
        btnNeverRate = findViewById(R.id.dialog_rating_button_never_rate);
        btnRateLater = findViewById(R.id.dialog_rating_button_rate_later);
        btnRate = findViewById(R.id.dialog_rating_button_rate);
        iconDialog = findViewById(R.id.dialog_rating_icon);
        layoutDialogRating = findViewById(R.id.dialog_rating_layout);
        txtTitleDialog = findViewById(R.id.dialog_rating_title);
        txtMessageDialog = findViewById(R.id.dialog_rating_subtitle);

        layoutSentimentButtons = findViewById(R.id.dialog_sentiment_buttons);
        btnSentimentPositive = findViewById(R.id.dialog_button_sentiment_positive);
        btnSentimentNegative = findViewById(R.id.dialog_button_sentiment_negative);
        btnSentimentDismiss = findViewById(R.id.dialog_button_sentiment_dismiss);
        layoutRatingButtons = findViewById(R.id.dialog_rating_buttons);

        initLayout();
    }

    private void initLayout() {
        if (mSentimentFilterEnabled) {
            if (layoutSentimentButtons != null) layoutSentimentButtons.setVisibility(View.VISIBLE);
            if (btnRate != null) btnRate.setVisibility(View.GONE);
            if (layoutRatingButtons != null) layoutRatingButtons.setVisibility(View.GONE);

            txtTitleDialog.setText(mSentimentTitleText != null ? mSentimentTitleText : mContext.getString(R.string.shurajcodx_sentiment_title));
            txtMessageDialog.setText(mSentimentMessageText != null ? mSentimentMessageText : mContext.getString(R.string.shurajcodx_sentiment_subtitle));

            if (btnSentimentPositive != null && mSentimentPositiveText != null) {
                btnSentimentPositive.setText(mSentimentPositiveText);
            }
            if (btnSentimentNegative != null && mSentimentNegativeText != null) {
                btnSentimentNegative.setText(mSentimentNegativeText);
            }
        } else {
            if (layoutSentimentButtons != null) layoutSentimentButtons.setVisibility(View.GONE);
            if (btnRate != null) btnRate.setVisibility(View.VISIBLE);
            if (layoutRatingButtons != null) layoutRatingButtons.setVisibility(View.VISIBLE);

            txtTitleDialog.setText(mTitleText);
            txtMessageDialog.setText(mMessageText);
            btnRateLater.setText(mRateLaterButtonText);
            btnNeverRate.setText(mNeverRateButtonText);
            btnRate.setText(mRateButtonText);
        }

        if (mActiveDialogIcon && iconDialog != null) {
            Drawable drawable = mContext.getPackageManager().getApplicationIcon(mContext.getApplicationInfo());
            iconDialog.setImageDrawable(mIconDrawable != null ? mIconDrawable : drawable);
            iconDialog.setVisibility(View.VISIBLE);
        }

        /* set text color */
        if (btnRateLater != null) {
            btnRateLater.setTextColor(mRateLaterButtonTextColor != 0 ? ContextCompat.getColor(mContext, mRateLaterButtonTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
        }
        if (btnNeverRate != null) {
            btnNeverRate.setTextColor(mNeverRateButtonTextColor != 0 ? ContextCompat.getColor(mContext, mNeverRateButtonTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
        }
        if (btnRate != null) {
            btnRate.setTextColor(mRateButtonTextColor != 0 ? ContextCompat.getColor(mContext, mRateButtonTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_white));
        }

        if (txtTitleDialog != null) {
            txtTitleDialog.setTextColor(mTitleTextColor != 0 ? ContextCompat.getColor(mContext, mTitleTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
        }
        if (txtMessageDialog != null) {
            txtMessageDialog.setTextColor(mMessageTextColor != 0 ? ContextCompat.getColor(mContext, mMessageTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
            txtMessageDialog.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMessageTextSize != 0 ? mMessageTextSize : 50f);
        }

        /* set background color */
        if (layoutDialogRating != null) {
            if (mLayoutResource != 0) {
                layoutDialogRating.setBackgroundResource(mLayoutResource);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (layoutDialogRating.getBackground() != null) {
                    layoutDialogRating.getBackground().setTint(mLayoutBackgroundColor != 0 ? ContextCompat.getColor(mContext, mLayoutBackgroundColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_white));
                }
                if (btnRate != null && btnRate.getBackground() != null) {
                    btnRate.getBackground().setTint(mRateButtonBackground != 0 ? ContextCompat.getColor(mContext, mRateButtonBackground) : ContextCompat.getColor(mContext, R.color.shurajcodx_skyblue));
                }
            } else {
                layoutDialogRating.setBackgroundColor(mLayoutBackgroundColor != 0 ? ContextCompat.getColor(mContext, mLayoutBackgroundColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_white));
                if (btnRate != null) {
                    btnRate.setBackgroundColor(mRateButtonBackground != 0 ? ContextCompat.getColor(mContext, mRateButtonBackground) : ContextCompat.getColor(mContext, R.color.shurajcodx_skyblue));
                }
            }
        }

        if (btnRateLater != null && mRateLaterButtonBackground != 0) {
            btnRateLater.setBackgroundResource(mRateLaterButtonBackground);
        }

        if (btnNeverRate != null && mNeverRateButtonBackground != 0) {
            btnNeverRate.setBackgroundResource(mNeverRateButtonBackground);
        }
    }

    private void setListener() {
        if (btnRateLater != null) btnRateLater.setOnClickListener(this);
        if (btnNeverRate != null) btnNeverRate.setOnClickListener(this);
        if (btnRate != null) btnRate.setOnClickListener(this);

        if (btnSentimentPositive != null) btnSentimentPositive.setOnClickListener(this);
        if (btnSentimentNegative != null) btnSentimentNegative.setOnClickListener(this);
        if (btnSentimentDismiss != null) btnSentimentDismiss.setOnClickListener(this);
    }

    private void incrementLaunchCount(final boolean force) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int count = getCount();
        final boolean isAtLaunchPoint = getRemainingCount() == 0;

        if (force || !isAtLaunchPoint) {
            count++;
        }

        if (!mSharedPrefs.getBoolean(SHOW_NEVER, false)) {
            editor.putInt(LAUNCH_COUNT, count).apply();
        }

        editor.apply();
    }

    private int getCount() {
        return mSharedPrefs.getInt(LAUNCH_COUNT, 0);
    }

    private int getRemainingCount() {
        int count = getCount();

        if (count < mTriggerCount) {
            return mTriggerCount - count;
        } else {
            return (mRepeatCount - ((count - mTriggerCount) % mRepeatCount)) % mRepeatCount;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog_rating_button_never_rate) {
            savedNeverShow();
            dismiss();

            if (onNever != null) {
                onNever.onClick();
            }
        } else if (id == R.id.dialog_rating_button_rate_later) {
            dismiss();
            incrementLaunchCount(true);

            if (onRemindMeLater != null) {
                onRemindMeLater.onClick();
            }
        } else if (id == R.id.dialog_rating_button_rate) {
            savedNeverShow();
            dismiss();

            if (onRate != null) {
                onRate.onClick();
            } else {
                if (mRatingMode == RatingMode.HYBRID) {
                    launchPlayReviewFlow(new Runnable() {
                        @Override
                        public void run() {
                            openStore();
                        }
                    });
                } else {
                    openStore();
                }
            }
        } else if (id == R.id.dialog_button_sentiment_positive) {
            dismiss();
            if (mRatingMode == RatingMode.HYBRID || mRatingMode == RatingMode.IN_APP_REVIEW_ONLY) {
                launchPlayReviewFlow(new Runnable() {
                    @Override
                    public void run() {
                        openStore();
                    }
                });
            } else {
                openStore();
            }
        } else if (id == R.id.dialog_button_sentiment_negative) {
            savedNeverShow();
            dismiss();

            if (onFeedback != null) {
                onFeedback.onFeedbackRequested();
            } else {
                Toast.makeText(mContext, R.string.shurajcodx_feedback_subtitle, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.dialog_button_sentiment_dismiss) {
            dismiss();
            incrementLaunchCount(true);

            if (onRemindMeLater != null) {
                onRemindMeLater.onClick();
            }
        }
    }

    /**
     * Shows the rating prompt if the trigger/repeat count conditions are met.
     */
    @Override
    public void show() {
        show(false);
    }

    /**
     * Bypasses trigger count and shows the rating dialog/flow immediately.
     */
    public void forceShow() {
        show(true);
    }

    /**
     * Shows the rating prompt, optionally forcing display regardless of launch counts.
     *
     * @param force if true, ignores launch count and SHOW_NEVER checks.
     */
    public void show(boolean force) {
        if (force || showRequest()) {
            if (mRatingMode == RatingMode.IN_APP_REVIEW_ONLY) {
                launchPlayReviewFlow(null);
            } else if (mRatingMode == RatingMode.HYBRID) {
                if (mSentimentFilterEnabled) {
                    super.show();
                } else {
                    // Try Google Play In-App Review first. If unavailable, fall back to dialog.
                    launchPlayReviewFlow(new Runnable() {
                        @Override
                        public void run() {
                            AppRatingDialog.super.show();
                        }
                    });
                }
            } else {
                // CUSTOM_DIALOG
                super.show();
            }
        }
    }

    private void launchPlayReviewFlow(@Nullable final Runnable fallback) {
        PlayReviewHelper helper = new PlayReviewHelper(mContext);
        helper.startReview(new ReviewCallback() {
            @Override
            public void onComplete(boolean success) {
                if (success) {
                    savedNeverShow();
                    if (onInAppReviewComplete != null) {
                        onInAppReviewComplete.onComplete(true);
                    }
                } else {
                    if (fallback != null) {
                        fallback.run();
                    } else if (onInAppReviewComplete != null) {
                        onInAppReviewComplete.onComplete(false);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unused")
    private void openPlayStore() {
        openStore();
    }

    private void openStore() {
        Uri marketUri = (mStoreLink != null && !mStoreLink.isEmpty())
                ? Uri.parse(mStoreLink)
                : mStoreType.getStoreUri(mContext, null);

        Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            String fallbackUrl = mStoreType.getWebFallbackUrl(mContext, mStoreLink);
            try {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(webIntent);
            } catch (Exception e) {
                Toast.makeText(mContext, "Could not open app store on this device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean showRequest() {
        final boolean showNever = mSharedPrefs.getBoolean(SHOW_NEVER, false);
        final boolean shouldShowRequest = getRemainingCount() == 0 && !showNever;

        if (!shouldShowRequest) {
            incrementLaunchCount(true);
        }

        return shouldShowRequest;
    }

    private void savedNeverShow() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(SHOW_NEVER, true);
        editor.apply();
    }

    public static class Builder {
        private final AppRatingDialog appRatingDialog;

        public Builder(Context context) {
            appRatingDialog = new AppRatingDialog(context);
        }

        @NonNull
        public Builder setTriggerCount(int triggerCount) {
            appRatingDialog.mTriggerCount = triggerCount;
            return this;
        }

        @NonNull
        public Builder setRepeatCount(int repeatCount) {
            appRatingDialog.mRepeatCount = repeatCount;
            return this;
        }

        @NonNull
        public Builder setRatingMode(RatingMode ratingMode) {
            appRatingDialog.mRatingMode = ratingMode;
            return this;
        }

        @NonNull
        public Builder setStoreType(StoreType storeType) {
            appRatingDialog.mStoreType = storeType;
            return this;
        }

        @NonNull
        public Builder setSentimentFilterEnabled(boolean enabled) {
            appRatingDialog.mSentimentFilterEnabled = enabled;
            return this;
        }

        @NonNull
        public Builder setSentimentTitle(String title) {
            appRatingDialog.mSentimentTitleText = title;
            return this;
        }

        @NonNull
        public Builder setSentimentTitle(@StringRes int titleRes) {
            return setSentimentTitle(appRatingDialog.mContext.getString(titleRes));
        }

        @NonNull
        public Builder setSentimentMessage(String message) {
            appRatingDialog.mSentimentMessageText = message;
            return this;
        }

        @NonNull
        public Builder setSentimentMessage(@StringRes int messageRes) {
            return setSentimentMessage(appRatingDialog.mContext.getString(messageRes));
        }

        @NonNull
        public Builder setSentimentPositiveText(String text) {
            appRatingDialog.mSentimentPositiveText = text;
            return this;
        }

        @NonNull
        public Builder setSentimentPositiveText(@StringRes int textRes) {
            return setSentimentPositiveText(appRatingDialog.mContext.getString(textRes));
        }

        @NonNull
        public Builder setSentimentNegativeText(String text) {
            appRatingDialog.mSentimentNegativeText = text;
            return this;
        }

        @NonNull
        public Builder setSentimentNegativeText(@StringRes int textRes) {
            return setSentimentNegativeText(appRatingDialog.mContext.getString(textRes));
        }

        @NonNull
        public Builder setFeedbackListener(RatingDialog.onFeedback listener) {
            appRatingDialog.onFeedback = listener;
            return this;
        }

        @NonNull
        public Builder setInAppReviewListener(RatingDialog.onInAppReviewComplete listener) {
            appRatingDialog.onInAppReviewComplete = listener;
            return this;
        }

        @NonNull
        public Builder setIconDrawable(boolean showIconDrawable, @Nullable Drawable drawable) {
            appRatingDialog.mIconDrawable = drawable;
            appRatingDialog.mActiveDialogIcon = showIconDrawable;
            return this;
        }

        @NonNull
        public Builder setTitleText(String titleText) {
            appRatingDialog.mTitleText = titleText;
            return this;
        }

        @NonNull
        public Builder setTitleText(@StringRes int titleText) {
            return setTitleText(appRatingDialog.mContext.getString(titleText));
        }

        @NonNull
        public Builder setTitleTextColor(@ColorRes int textColor) {
            appRatingDialog.mTitleTextColor = textColor;
            return this;
        }

        @NonNull
        public Builder setMessageText(String messageText) {
            appRatingDialog.mMessageText = messageText;
            return this;
        }

        @NonNull
        public Builder setMessageText(@StringRes int messageText) {
            return setMessageText(appRatingDialog.mContext.getString(messageText));
        }

        @NonNull
        public Builder setMessageTextColor(@ColorRes int textColor) {
            appRatingDialog.mMessageTextColor = textColor;
            return this;
        }

        public Builder setMessageTextSize(@DimenRes int textSize) {
            appRatingDialog.mMessageTextSize = appRatingDialog.mContext.getResources().getDimension(textSize);
            return this;
        }

        @NonNull
        public Builder setRateLaterButtonText(String rateLaterButtonText, @Nullable RatingDialog.onRemindMeLater onRateLaterClickListener) {
            appRatingDialog.mRateLaterButtonText = rateLaterButtonText;
            appRatingDialog.onRemindMeLater = onRateLaterClickListener;
            return this;
        }

        @NonNull
        public Builder setRateLaterButtonText(@StringRes int rateLaterButtonText, @Nullable RatingDialog.onRemindMeLater onRateLaterClickListener) {
            return setRateLaterButtonText(appRatingDialog.mContext.getString(rateLaterButtonText), onRateLaterClickListener);
        }

        @NonNull
        public Builder setRateLaterButtonTextColor(@ColorRes int buttonTextColor) {
            appRatingDialog.mRateLaterButtonTextColor = buttonTextColor;
            return this;
        }

        @NonNull
        public Builder setNeverRateButtonText(String neverRateButtonText, @Nullable RatingDialog.onNever onNeverRateClickListener) {
            appRatingDialog.mNeverRateButtonText = neverRateButtonText;
            appRatingDialog.onNever = onNeverRateClickListener;
            return this;
        }

        @NonNull
        public Builder setNeverRateButtonText(@StringRes int neverRateButtonText, @Nullable RatingDialog.onNever onNeverRateClickListener) {
            return setNeverRateButtonText(appRatingDialog.mContext.getString(neverRateButtonText), onNeverRateClickListener);
        }

        @NonNull
        public Builder setNeverRateButtonTextColor(@ColorRes int buttonTextColor) {
            appRatingDialog.mNeverRateButtonTextColor = buttonTextColor;
            return this;
        }

        @NonNull
        public Builder setRateButtonText(String rateButtonText, @Nullable RatingDialog.onRate onRateClickListener) {
            appRatingDialog.onRate = onRateClickListener;
            appRatingDialog.mRateButtonText = rateButtonText;
            return this;
        }

        @NonNull
        public Builder setRateButtonText(@StringRes int rateButtonText, @Nullable RatingDialog.onRate onRateClickListener) {
            return setRateButtonText(appRatingDialog.mContext.getString(rateButtonText), onRateClickListener);
        }

        @NonNull
        public Builder setRateButtonTextColor(@ColorRes int buttonTextColor) {
            appRatingDialog.mRateButtonTextColor = buttonTextColor;
            return this;
        }

        @NonNull
        public Builder setStoreLink(String storeLink) {
            appRatingDialog.mStoreLink = storeLink;
            return this;
        }

        @NonNull
        public Builder setStoreLink(@StringRes int resId) {
            return setStoreLink(appRatingDialog.mContext.getString(resId));
        }

        @NonNull
        public Builder setLayoutBackgroundColor(@ColorRes int backgroundColor) {
            appRatingDialog.mLayoutBackgroundColor = backgroundColor;
            return this;
        }

        public Builder setLayoutBackgroundResource(@DrawableRes int backgroundResource) {
            appRatingDialog.mLayoutResource = backgroundResource;
            return this;
        }

        @NonNull
        public Builder setRateLaterButtonBackground(@DrawableRes int rateLaterButtonBackground) {
            appRatingDialog.mRateLaterButtonBackground = rateLaterButtonBackground;
            return this;
        }

        @NonNull
        public Builder setNeverRateButtonBackground(@DrawableRes int neverRateButtonBackground) {
            appRatingDialog.mNeverRateButtonBackground = neverRateButtonBackground;
            return this;
        }

        @NonNull
        public Builder setRateButtonBackground(@DrawableRes int rateButtonBackground) {
            appRatingDialog.mRateButtonBackground = rateButtonBackground;
            return this;
        }

        @NonNull
        public AppRatingDialog build() {
            return appRatingDialog;
        }
    }
}

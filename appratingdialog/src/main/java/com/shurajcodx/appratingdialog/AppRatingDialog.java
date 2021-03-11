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
    private int mNeverRateButtonTextColor,mRateLaterButtonTextColor, mTitleTextColor, mMessageTextColor, mRateButtonTextColor;
    private float mMessageTextSize;
    private int mNeverRateButtonBackground, mRateLaterButtonBackground, mRateButtonBackground;

    private TextView txtTitleDialog;
    private TextView txtMessageDialog;
    private TextView btnNeverRate;
    private TextView btnRateLater;
    private TextView btnRate;
    private ImageView iconDialog;
    private RelativeLayout layoutDialogRating;

    private RatingDialog.onRemindMeLater onRemindMeLater;
    private RatingDialog.onNever onNever;
    private RatingDialog.onRate onRate;

    private AppRatingDialog(Context context) {
        super(context);

        mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mTitleText = context.getString(R.string.shurajcodx_rating_dialog_title);
        mMessageText = context.getString(R.string.shurajcodx_rating_dialog_subtitle);
        mNeverRateButtonText = context.getString(R.string.shurajcodx_rating_dialog_never);
        mRateLaterButtonText = context.getString(R.string.shurajcodx_rating_dialog_cancel);
        mRateButtonText = context.getString(R.string.shurajcodx_rating_dialog_ok);
        mStoreLink = "market://details?id=" + mContext.getPackageName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

        initLayout();
    }

    private void initLayout() {
        txtTitleDialog.setText(mTitleText);
        txtMessageDialog.setText(mMessageText);
        btnRateLater.setText(mRateLaterButtonText);
        btnNeverRate.setText(mNeverRateButtonText);
        btnRate.setText(mRateButtonText);

        if (mActiveDialogIcon) {
            Drawable drawable = mContext.getPackageManager().getApplicationIcon(mContext.getApplicationInfo());
            iconDialog.setImageDrawable(mIconDrawable != null ? mIconDrawable : drawable);
            iconDialog.setVisibility(View.VISIBLE);
        }

        /* set text color */
        btnRateLater.setTextColor(mRateLaterButtonTextColor != 0 ? ContextCompat.getColor(mContext, mRateLaterButtonTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
        btnNeverRate.setTextColor(mNeverRateButtonTextColor != 0 ? ContextCompat.getColor(mContext, mNeverRateButtonTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
        btnRate.setTextColor(mRateButtonTextColor != 0 ? ContextCompat.getColor(mContext, mRateButtonTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_white));

        txtTitleDialog.setTextColor(mTitleTextColor != 0 ? ContextCompat.getColor(mContext, mTitleTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));
        txtMessageDialog.setTextColor(mMessageTextColor != 0 ? ContextCompat.getColor(mContext, mMessageTextColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_grey_800));

        txtMessageDialog.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMessageTextSize != 0 ? mMessageTextSize : 16f);

        /* set background color */
        if (mLayoutResource != 0) {
            layoutDialogRating.setBackgroundResource(mLayoutResource);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutDialogRating.getBackground().setTint(mLayoutBackgroundColor != 0 ? ContextCompat.getColor(mContext, mLayoutBackgroundColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_white));
            btnRate.getBackground().setTint(mRateButtonBackground != 0 ? ContextCompat.getColor(mContext, mRateButtonBackground) : ContextCompat.getColor(mContext, R.color.shurajcodx_skyblue));
        } else {
            layoutDialogRating.setBackgroundColor(mLayoutBackgroundColor != 0 ? ContextCompat.getColor(mContext, mLayoutBackgroundColor) : ContextCompat.getColor(mContext, R.color.shurajcodx_white));
            btnRate.setBackgroundColor(mRateButtonBackground != 0 ? ContextCompat.getColor(mContext, mRateButtonBackground) : ContextCompat.getColor(mContext, R.color.shurajcodx_skyblue));
        }

        if (mRateLaterButtonBackground != 0) {
            btnRateLater.setBackgroundResource(mRateLaterButtonBackground);
        }

        if (mNeverRateButtonBackground != 0) {
            btnNeverRate.setBackgroundResource(mNeverRateButtonBackground);
        }
    }

    private void setListener() {
        btnRateLater.setOnClickListener(this);
        btnNeverRate.setOnClickListener(this);
        btnRate.setOnClickListener(this);
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
        if (v.getId() == R.id.dialog_rating_button_never_rate) {
            savedNeverShow();
            dismiss();

            if (onNever != null) {
                onNever.onClick();
            }
        } else if (v.getId() == R.id.dialog_rating_button_rate_later) {
            dismiss();
            incrementLaunchCount(true);

            if (onRemindMeLater != null) {
                onRemindMeLater.onClick();
            }
        } else if (v.getId() == R.id.dialog_rating_button_rate) {
            savedNeverShow();
            dismiss();

            if (onRate != null) {
                onRate.onClick();
            } else {
                openPlayStore();
            }
        }
    }

    @Override
    public void show() {
        if (showRequest()) {
            super.show();
        }
    }

    private void openPlayStore() {
        final Uri marketUri = Uri.parse(mStoreLink);
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "Couldn't find PlayStore on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean showRequest() {
        final boolean showNever = mSharedPrefs.getBoolean(SHOW_NEVER, false);
        final boolean shouldShowRequest =
                getRemainingCount() == 0
                && !showNever;

        if (!shouldShowRequest) {
            incrementLaunchCount(true);
        }

        return shouldShowRequest;
    }

    private void savedNeverShow () {
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

        /* start button background color */
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
        /* end button background color */

        @NonNull
        public AppRatingDialog build() {
            return appRatingDialog;
        }
    }
}

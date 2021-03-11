package com.shurajcodx.apprating;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.shurajcodx.appratingdialog.AppRatingDialog;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadRatingDialog();

        Button button = findViewById(R.id.rate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateDialog();
            }
        });
    }

    private void loadRatingDialog() {
        final AppRatingDialog appRatingDialog = new AppRatingDialog.Builder(this)
                .setTriggerCount(1)
                .setRepeatCount(4)
                .setLayoutBackgroundColor(R.color.colorDialogBox)
                .setIconDrawable(true, ContextCompat.getDrawable(this, R.drawable.love))
                .setTitleText(R.string.rating_dialog_custom_title)
                .setTitleTextColor(R.color.white)
                .setMessageText(R.string.rating_dialog_custom_message)
                .setMessageTextColor(R.color.white)
                .setRateButtonBackground(R.color.colorPrimaryDark)
                .build();

        appRatingDialog.show();
    }

    private void rateDialog() {
        final AppRatingDialog appRatingDialog = new AppRatingDialog.Builder(this)
                .setTriggerCount(1)
                .setRepeatCount(1)
                .setLayoutBackgroundColor(R.color.colorDialogBox)
                .setIconDrawable(true, ContextCompat.getDrawable(this, R.drawable.love))
                .setTitleText("Do you like LoveTester?")
                .setTitleTextColor(R.color.white)
                .setMessageText("Please take a moment to rate it and help support to improve more new feature.")
                .setMessageTextSize(R.dimen.text20sp)
                .setMessageTextColor(R.color.white)
                .setRateLaterButtonText("Remind me later", null)
                .setNeverRateButtonText("No, Thanks", null)
                .setRateButtonBackground(R.color.shurajcodx_skyblue)
                .build();

        appRatingDialog.show();
    }
}

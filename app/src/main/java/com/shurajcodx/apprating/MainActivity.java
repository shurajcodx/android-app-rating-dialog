package com.shurajcodx.apprating;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.shurajcodx.appratingdialog.AppRatingDialog;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadRatingDialog();
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
}

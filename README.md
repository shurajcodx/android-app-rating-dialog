# AndroidAppRatingDialog
[![](https://jitpack.io/v/shurajcodx/android-app-rating-dialog.svg)](https://jitpack.io/#shurajcodx/android-app-rating-dialog) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This is a Android App Rating Dialog to encourage user to rate the app on the Google Play Store.

## Preview of AndroidAppRatingDialog
<img src="https://github.com/shurajcodx/android-app-rating-dialog/blob/development/preview/sample1.png" height="300em" />&nbsp; <img src="https://github.com/shurajcodx/android-app-rating-dialog/blob/development/preview/sample3.png" height="300em" />&nbsp; <img src="https://github.com/shurajcodx/android-app-rating-dialog/blob/development/preview/sample2.png" height="300em" />&nbsp;

# Using this Library in your project

1. Add it in your root build.gradle at the end of repositories:

    ```java
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```

2. Add this in your app's build.gradle

    ```java
    dependencies {
        implementation 'com.github.shurajcodx:android-app-rating-dialog:1.0.3'
    }
    ```

## How to use

Use the app rating dialog:
```java
final AppRatingDialog appRatingDialog = new AppRatingDialog.Builder(this)
    .setTriggerCount(2)
    .setRepeatCount(5)
    .build();

appRatingDialog.show();
```
Or customize app rating dialog ui from builder class:   

```java
final AppRatingDialog appRatingDialog = new AppRatingDialog.Builder(this)
        .setTriggerCount(4)
        .setRepeatCount(7)
        .setLayoutBackgroundColor(R.color.colorDialogBox)
        .setIconDrawable(true, ContextCompat.getDrawable(this, R.drawable.love))
        .setTitleText(R.string.rating_dialog_custom_title)
        .setTitleTextColor(R.color.white)
        .setMessageText(R.string.rating_dialog_custom_message)
        .setMessageTextColor(R.color.white)
        .setMessageTextSize(R.dimen.text20sp)
        .setRateButtonText("Rate App", null)
        .setRateButtonBackground(R.color.colorPrimaryDark)
        .build();

appRatingDialog.show();
```


### Standard configuration
You have the following options to customize dialog ui and dialog show up:

#### When to Show Dialog

- Change the mimimum number of app launches
    ```java
    .setTriggerCount(int triggerCount) // default is 2
    ```
- Change the minmium number of repeat count
    ```java
    .setRepeatCount(int repeatCount) //default is 5
    ```

 #### Design

 - Change the icon of the dialog
    ```java
    .setIconDrawable(boolean showIconDrawable, Drawable drawable) // default icon isn't show
    ```

 - Change the title text and color
    ```java
    .setTitleText(@StringRes int titleText) //for changing title text

    .setTitleTextColor(@ColorRes int textColor) //for changing title text color
    ```

- Change the message text, size and color
    ```java
    .setMessageText(@StringRes int messageText)

    .setMessageTextColor(@ColorRes int textColor)

    .setMessageTextSize(@DimenRes int textSize)
    ```

- Change the store link
    ```java
    .setStoreLink(String storeLink)
    ```

- Change the rate later button text and add a click listener
    ```java
    .setRateLaterButtonText(String rateLaterButtonText, @Nullable OnRatingDialogListener onRateLaterClickListener)
    ```

- Change the rate later button text color and background color
    ```java
    .setRateLaterButtonTextColor(@ColorRes int buttonTextColor)

    .setRateLaterButtonBackground(@DrawableRes int rateLaterButtonBackground)
    ```

- Change the never rate button text and add listener
    ```java
    .setNeverRateButtonText(@StringRes int neverRateButtonText, @Nullable OnRatingDialogListener onRateLaterClickListener)
    ```

- Change the never rate button text color and background color
    ```java
    .setNeverRateButtonTextColor(@ColorRes int buttonTextColor)

    .setNeverRateButtonBackground(@DrawableRes int neverRateButtonBackground)
    ```
- Change the rate button text and add listener
    ```java
    .setRateButtonText(String rateButtonText, @Nullable OnRatingDialogListener onRateClickListener) // default listener redirect user to google play store 
    ```

- Change the rate button text color and background color
    ```java
    .setRateButtonTextColor(@ColorRes int buttonTextColor)

    .setRateButtonBackground(@DrawableRes int rateButtonBackground)
    ```


## If this library helps you in anyway, show your ❤️ by putting 🌟 on this project

### License
```
Copyright (C) 2019 Shuraj Shampang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Contributing to AppRatingDialog
Make sure to follow the [contribution guidelines](CONTRIBUTING.md) when you submit pull request.

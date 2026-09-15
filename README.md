# AndroidAppRatingDialog 🚀 (v2.0)

[![Release](https://jitpack.io/v/shurajcodx/android-app-rating-dialog.svg)](https://jitpack.io/#shurajcodx/android-app-rating-dialog)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org)
[![Google Play In-App Review](https://img.shields.io/badge/Google%20Play-In--App%20Review-green.svg)](https://developer.android.com/guide/playcore/in-app-review)

A modern, smart Android rating library that combines **Google Play In-App Review API**, a **Smart Hybrid Fallback Engine**, **Two-Step Sentiment Feedback Triage**, and **Multi-Store Support** with a fluent Kotlin DSL and 100% Java backward compatibility.

---

## ✨ What's New in v2.0?

* 🌟 **Google Play In-App Review API**: Users can submit ratings & reviews directly within your app without being redirected out.
* 🔄 **Smart Hybrid Mode**: Attempts Google Play In-App Review first; if unavailable, on non-GMS devices (Amazon Fire, Huawei), or if the quota is reached, gracefully falls back to the custom dialog / store redirect.
* 🛡️ **Sentiment Feedback Triage**: Ask *"Enjoying the app?"* first:
  * **Positive (👍 / Love it)**: Routes to Google Play In-App Review or store rating.
  * **Constructive (👎 / Needs work)**: Directs to your internal feedback listener or email support—protecting your store score from 1-star reviews!
* 🛒 **Multi-Store Support**: Built-in schemes for **Google Play**, **Amazon Appstore**, **Huawei AppGallery**, and **Samsung Galaxy Store**.
* ⚡ **Idiomatic Kotlin DSL**: Clean `appRating(context) { ... }` builder.
* ☕ **100% Java Backward Compatibility**: Existing `new AppRatingDialog.Builder(this)` code continues to work seamlessly.

---

## 📸 Preview

<img src="https://github.com/shurajcodx/android-app-rating-dialog/blob/development/preview/sample1.png" height="300em" />&nbsp;
<img src="https://github.com/shurajcodx/android-app-rating-dialog/blob/development/preview/sample3.png" height="300em" />&nbsp;
<img src="https://github.com/shurajcodx/android-app-rating-dialog/blob/development/preview/sample2.png" height="300em" />

---

## 📦 Installation

### 1. Add JitPack repository

In your `settings.gradle` (or root `build.gradle`):

```groovy
dependencyResolutionManagement {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add the dependency

In your module `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.shurajcodx:android-app-rating-dialog:2.0.0'
}
```

---

## 🚀 Quick Start

### 1. Modern Kotlin DSL (Recommended)

```kotlin
import com.shurajcodx.appratingdialog.RatingMode
import com.shurajcodx.appratingdialog.appRating

// Smart Hybrid: In-App Review first, fallback to store if needed
appRating(this) {
    triggerCount = 3
    repeatCount = 5
    mode = RatingMode.HYBRID
    
    // Enable Sentiment Pre-Filtering
    sentimentFilterEnabled = true
    sentimentTitle = "Enjoying the app?"
    onFeedback {
        // Intercepted negative feedback: open support email or feedback form
        openSupportEmail()
    }
}.show()
```

### 2. Java Builder

```java
import com.shurajcodx.appratingdialog.AppRatingDialog;
import com.shurajcodx.appratingdialog.RatingMode;
import com.shurajcodx.appratingdialog.StoreType;

AppRatingDialog dialog = new AppRatingDialog.Builder(this)
        .setTriggerCount(3)
        .setRepeatCount(5)
        .setRatingMode(RatingMode.HYBRID)
        .setStoreType(StoreType.GOOGLE_PLAY)
        .setSentimentFilterEnabled(true)
        .setFeedbackListener(() -> {
            // User indicated they need improvements
            Toast.makeText(this, "Tell us how we can improve!", Toast.LENGTH_SHORT).show();
        })
        .build();

dialog.show();
```

---

## ⚙️ Rating Modes (`RatingMode`)

| Mode | Description |
|---|---|
| `RatingMode.HYBRID` *(Default)* | Attempts **Google Play In-App Review** first. If GMS is unavailable, quota throttled, or on non-Play devices, falls back to custom dialog / store redirect. |
| `RatingMode.IN_APP_REVIEW_ONLY` | Strictly uses Google's native In-App Review overlay sheet. |
| `RatingMode.CUSTOM_DIALOG` | Classic standalone customizable dialog with launch counters and direct store redirect. |

---

## 🏬 Multi-Store Support (`StoreType`)

Specify where non-GMS users or direct redirects should go:

```kotlin
appRating(this) {
    storeType = StoreType.AMAZON       // Amazon Appstore
    // or StoreType.HUAWEI             // Huawei AppGallery
    // or StoreType.SAMSUNG            // Samsung Galaxy Store
    // or StoreType.GOOGLE_PLAY        // Google Play Store (default)
    // or StoreType.CUSTOM             // Custom URL via customStoreUrl = "..."
}
```

---

## 🛠️ Configuration Options

| Method | Kotlin Property | Description |
|---|---|---|
| `setTriggerCount(int)` | `triggerCount` | Number of app launches before showing (default: 2) |
| `setRepeatCount(int)` | `repeatCount` | Number of launches before reminding again (default: 5) |
| `setRatingMode(RatingMode)` | `mode` | `HYBRID`, `IN_APP_REVIEW_ONLY`, or `CUSTOM_DIALOG` |
| `setStoreType(StoreType)` | `storeType` | Store deep-link destination |
| `setSentimentFilterEnabled(bool)` | `sentimentFilterEnabled` | Toggles two-step sentiment check |
| `setFeedbackListener(listener)` | `onFeedback { ... }` | Callback when user selects negative sentiment |
| `setInAppReviewListener(listener)` | `onInAppReviewComplete { ... }` | Callback when Play In-App review finishes |
| `forceShow()` | `forceShow()` | Bypasses launch counter and shows immediately |
| `setTitleText(...)` | `titleText` | Custom title for the rating dialog |
| `setMessageText(...)` | `messageText` | Custom message body for the dialog |
| `setLayoutBackgroundColor(...)` | `layoutBackgroundColor` | Background color for the dialog container |
| `setRateButtonBackground(...)` | `rateButtonBackground` | Drawable resource for primary rate button |

---

## 🧪 Testing

To test prompts immediately during development without waiting for launch counts:
```kotlin
dialog.forceShow()
```

---

## 📄 License

```
Copyright (C) 2019-2026 Shuraj Shampang

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

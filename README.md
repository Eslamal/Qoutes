# Quotes App
A modern Android application for displaying, saving, and sharing inspirational quotes, built with the latest Android development technologies and best practices.

## ✨ Features

* **Dynamic Quotes:** Fetch random quotes from the [ZenQuotes API](https://zenquotes.io/).
* **Interactive UI:** Swipe the quote card to load a new quote.
* **Bookmarking:** Save your favorite quotes for offline access.
* **Local Storage:** View all your saved quotes in a clean list.
* **Gesture Controls:**
    * Swipe left or right on a saved quote to delete it (with an "Undo" option).
    * Long-press a saved quote to copy it to the clipboard.
* **Share as Image:** Share any quote as a beautifully formatted image to other apps.
* **Daily Notifications:** Receive a "Quote of the Day" notification powered by WorkManager.
* **Modern Design:** A clean, minimal UI with a Lottie-powered splash screen and smooth transitions.

## 🛠️ Tech Stack & Architecture

This project follows the **MVVM (Model-View-ViewModel)** architecture pattern and uses modern development tools and libraries.

* **Core:**
    * [Kotlin](https://kotlinlang.org/): Official language for Android Development.
    * [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): For asynchronous and non-blocking programming.
* **Architecture:**
    * [MVVM](https://developer.android.com/topic/architecture): Separating UI, business logic, and data layers.
    * [Repository Pattern](https://developer.android.com/topic/architecture/data-layer): Single source of truth for data.
* **Jetpack Components:**
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): Manages UI-related data in a lifecycle-conscious way.
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata): Observable data holder for UI updates.
    * [Room](https://developer.android.com/training/data-storage/room): Persistence library for local database storage.
    * [Navigation Component](https://developer.android.com/guide/navigation): Manages fragment transactions and navigation logic.
    * [Hilt](https://dagger.dev/hilt/): For dependency injection.
    * [DataStore](https://developer.android.com/topic/libraries/architecture/datastore): For storing simple key-value settings.
    * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager): For reliable background tasks.
* **Networking:**
    * [Retrofit](https://square.github.io/retrofit/): A type-safe HTTP client for Android and Java.
    * [OkHttp](https://square.github.io/okhttp/): An efficient HTTP client, used as the backbone for Retrofit.
* **UI:**
    * Android Views & XML.
    * [ViewBinding](https://developer.android.com/topic/libraries/view-binding): To interact with views more safely and concisely.
    * [Material Components](https://material.io/develop/android/): For modern UI elements.
    * [Lottie](https://airbnb.design/lottie/): For rendering After Effects animations on the splash screen.

### Architecture Flow
The app is structured following the recommended Android architecture guidelines:

`View (Activity/Fragment) <--> ViewModel <--> Repository <--> (API Service / Room DAO)`



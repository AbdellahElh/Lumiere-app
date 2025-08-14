# Lumière Android App

Android mobile application for the Lumière cinema chain. Browse movies, check showtimes, manage favorites, and book tickets from your phone. Built with Kotlin and modern Android architecture for a smooth, responsive experience.

## Overview

This app lets users:

- Discover current and upcoming movies per cinema (Antwerpen, Brugge, Mechelen, Cartoons, …)
- View movie details, trailers/placeholders, and showtimes per location
- Search and filter movies
- Add/remove items from a personal watchlist
- Sign up / log in with Auth0
- Purchase and view tickets (with QR codes)
- Manage a “ten-turn” card (multi-visit card)
- See events and event details
- Receive local notifications/reminders


## Tech stack

- Language: Kotlin (JDK 17)
- UI: Jetpack Compose, Material 3, Navigation Compose
- Images: Coil
- Networking: Retrofit, OkHttp, Gson (plus kotlinx-serialization in places)
- Auth: Auth0 (Universal Login, JWT)
- Persistence/Offline: Room (Movies, Tickets, Watchlist, Ten-turn card, Events)
- Testing: JUnit4, AndroidX Test, Espresso, Compose UI tests, Coroutines Test, MockK (androidTest)
- Other: ZXing for QR codes, Notifications (BroadcastReceiver), Gradle Kotlin DSL

Compile SDK: 35 • Target SDK: 35 • Min SDK: 24

Backend base URL (from code): `https://lumieregent5.vichogent.be/`

## Architecture

- Pattern: MVVM with repositories
- DI style: Lightweight manual container (`DefaultAppContainer`) wiring Retrofit services, DAOs, and repos
- Caching: Room DAOs + repositories, with OkHttp client adding Authorization header when available
- Feature modules (by package): movies, events, tickets, watchlist, account/auth, ten-turn card

## Project structure (high-level)

- `app/` – Android application module
  - `src/main/java/com/example/riseandroid` – source code
    - `ui/` – Compose screens (homepage, movie detail, program, tickets, watchlist, signup/login, account, events)
    - `navigation/` – Bottom nav and graphs
    - `repository/` – Repositories for Movies, Posters, Tickets, Watchlist, Events, Auth
    - `network/` – Retrofit services (Movie, Events, Tickets, Watchlist, SignUp, Auth0)
    - `data/` – Room database, DAOs, entities, app container (DI)
    - `notifications/` – `NotificationReceiver`
  - `src/androidTest` and `src/test` – UI and unit tests
  - `src/main/res/` – resources (themes, icons, strings)

## Getting started

Prerequisites:

- Android Studio (latest stable) with Android SDK 35
- JDK 17
- A device or emulator on API 24+

Set up Auth0 (required for login):

- Configure values in `app/src/main/res/values/auth0.xml`:
  - `com_auth0_domain` (your Auth0 tenant domain)
  - `com_auth0_client_id` (your application Client ID)
- The manifest uses placeholders pointing to these strings.

Run from Android Studio:

- Open the project folder and let Gradle sync
- Select a device/emulator (API 24+)
- Run the app (MainActivity)

Run from command line (Windows PowerShell):

```powershell
# Clean & build debug
./gradlew.bat clean assembleDebug

# Install & run on a connected device/emulator
./gradlew.bat installDebug ; ./gradlew.bat :app:connectedDebugAndroidTest
```

## Testing

- Unit tests:

```powershell
./gradlew.bat testDebugUnitTest
```

- Instrumented/UI tests (device/emulator required):

```powershell
./gradlew.bat connectedDebugAndroidTest
```

## Notes and capabilities

- Notifications: Requires POST_NOTIFICATIONS (Android 13+) and uses a broadcast receiver for scheduling reminders.
- Networking: OkHttp client injects a Bearer token from Auth0 credentials when present.
- QR codes: ZXing is used for displaying/scanning ticket codes.
- Offline: Room repositories cache data for resilience.
- CI: `Jenkinsfile` is present for pipeline automation.

## Contributing

- Use Kotlin style guides and Compose best practices.
- Prefer repository updates alongside DAO/API changes.
- Add/update unit and UI tests for user-facing changes.

## License

This project’s license has not been specified. Add a LICENSE file or update this section as appropriate.

# android-2425-gent5

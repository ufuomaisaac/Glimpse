# Glimpse

Glimpse is a Kotlin Multiplatform photo-sharing app built with Compose Multiplatform. The product direction is a WeTransfer-style event photo tool: hosts upload event photos, the backend groups photos into face clusters, and recipients receive shareable links for the photos they appear in.

The current app focuses on the shared Compose shell and host authentication flow.

## What Is Implemented

- Shared Compose Multiplatform UI for Android, iOS, desktop, and web targets.
- Splash screen and onboarding flow:
  `SplashScreen -> GetStatedScreen -> SignUp / SignIn`.
- Auth screens for sign-in, sign-up, and email verification.
- Sign-up collects email, username, password, and confirm password.
- Password confirmation validation before sign-up submission.
- Android auth uses Clerk's native Android SDK.
- Shared auth abstraction with platform-specific repository implementations.
- Ktor clients for backend API access and legacy/fallback auth paths.
- DataStore token storage per platform.

## Current Auth Status

Android auth is wired through Clerk's Android SDK:

- `Clerk.initialize(...)` runs in `androidApp` startup.
- Android sign-in uses `Clerk.auth.signInWithPassword`.
- Android sign-up uses `Clerk.auth.signUp` with email, username, and password.
- Email verification uses Clerk SDK verification APIs.
- Successful verification activates the created session and stores the session token.

Required Clerk Dashboard setup:

- Enable Native API.
- Enable email/password authentication.
- If username is required in Clerk, keep the app username field enabled.
- If you do not want usernames, disable the username requirement in Clerk Dashboard and adjust the app form accordingly.

Important iOS note: iOS auth is not fully implemented yet. The shared iOS auth repository intentionally returns a clear ClerkKit bridge-required error instead of using the direct Clerk Frontend API path that fails native security validation. Completing iOS auth requires macOS/Xcode so ClerkKit can be added and tested through Xcode.

## Project Structure

```text
.
├── androidApp/      # Android entry point and Android application setup
├── iosApp/          # iOS SwiftUI host app and Xcode project
├── desktopApp/      # JVM desktop entry point
├── webApp/          # Web entry point
└── shared/          # Shared Kotlin and Compose Multiplatform code
```

Shared source layout:

```text
shared/src/commonMain/kotlin/com/example/glimpse/
├── App.kt                         # Root Compose app and NavHost
├── core/
│   ├── common/                    # Shared UI state helpers
│   ├── data/                      # Repositories and auth abstraction
│   ├── model/                     # Domain models
│   └── network/                   # Ktor clients, API services, DTOs
├── designsystem/                  # Colors, typography, icons, spacing, components
├── feature/
│   └── auth/                      # Auth UI, ViewModel, and auth graph
└── navigation/                    # App-level startup ViewModel
```

Platform-specific shared code lives in:

```text
shared/src/androidMain/
shared/src/iosMain/
shared/src/jvmMain/
shared/src/jsMain/
shared/src/wasmJsMain/
```

## Architecture

Glimpse uses a flat KMP architecture with package boundaries rather than feature Gradle modules.

Key patterns:

- Compose Multiplatform for UI.
- MVVM for screen state and user actions.
- Koin for dependency injection.
- JetBrains Compose Navigation for typed navigation routes.
- Ktor for backend networking.
- DataStore Preferences for local token persistence.
- Platform-specific auth repository implementations behind a shared `AuthRepository` contract.

Auth flow dependency direction:

```text
UI screen -> AuthViewModel -> AuthRepository -> platform implementation / network service
```

## Requirements

General:

- JDK 17
- Android Studio or IntelliJ IDEA with Kotlin Multiplatform support
- Gradle wrapper from this repo

Android:

- Android SDK matching the configured compile SDK
- Clerk Native API enabled in Clerk Dashboard

iOS:

- macOS
- Xcode
- ClerkKit integration still pending

## Configuration

Backend API base URL is currently defined in:

```text
shared/src/commonMain/kotlin/com/example/glimpse/core/network/ApiConstants.kt
```

Clerk publishable key and Frontend API URL are currently defined in:

```text
shared/src/commonMain/kotlin/com/example/glimpse/core/network/ClerkConfig.kt
```

The Clerk publishable key is safe to ship in a client app. Do not add Clerk secret keys to this repository or to any mobile client.

## Running The App

Build Android debug APK:

```bash
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :androidApp:assembleDebug
```

Compile shared Android target:

```bash
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :shared:compileAndroidMain
```

Compile shared metadata:

```bash
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :shared:compileKotlinMetadata
```

Compile iOS shared metadata:

```bash
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :shared:compileIosMainKotlinMetadata
```

Run iOS app:

```text
Open iosApp/iosApp.xcodeproj in Xcode and run the iosApp target.
```

Run desktop or web targets from your IDE Gradle tool window. Task names may vary with Compose plugin updates, so prefer Gradle task discovery if needed:

```bash
./gradlew :desktopApp:tasks
./gradlew :webApp:tasks
```

## Useful Debugging

Filter Android Logcat to this app:

```bash
adb logcat --pid=$(adb shell pidof -s com.example.glimpse)
```

Auth-specific Android logs use this tag:

```text
GlimpseAuth
```

System logs such as `TrustyKeymaster` are device/keystore logs and are not direct Clerk errors. For auth debugging, prefer app logs and snackbar messages.

## Verification Commands

Before handing off changes, these are useful checks:

```bash
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :shared:compileKotlinMetadata
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :shared:compileAndroidMain
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :shared:compileIosMainKotlinMetadata
./gradlew --no-daemon -Djava.io.tmpdir=/tmp :androidApp:assembleDebug
```

## Roadmap

Near-term:

- Finish and test Android Clerk sign-up/sign-in end to end.
- Add the ClerkKit bridge for iOS on macOS/Xcode.
- Replace hardcoded environment values with a safer configuration strategy.
- Continue the main product flow after auth.

Product roadmap:

- Upload event photos.
- Track upload and processing status.
- Display face clusters.
- Rename and manage clusters.
- Generate recipient links.
- Build a recipient gallery that does not require authentication.

## Notes For Contributors

- Keep shared UI in `shared/src/commonMain` where possible.
- Put platform-specific code in the relevant source set.
- Do not put Clerk secret keys or backend secrets in mobile code.
- Keep features isolated by package; avoid feature-to-feature imports.
- Prefer repository changes over business logic in composables.

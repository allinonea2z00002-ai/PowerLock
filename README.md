# PowerLock (Android starter project)

This is a Kotlin Android Studio project for a **best-effort** Power Menu detector and reminder overlay.

## Important limitations
- Android has no stable public API that tells ordinary apps exactly when the system Power Menu opens. The accessibility service uses a heuristic against System UI text/window events, so detection may not work on every Android/OriginOS version.
- An ordinary third-party app cannot reliably prevent the hardware power button, force shutdown, restart, or emergency/SOS functions. This project does **not** disable them.
- The overlay includes a close button and a separate emergency/SOS dismiss button. Dismissing the overlay reveals the system menu again.
- This is not a kiosk/device-owner app and must not be treated as a security boundary.

## Build
1. Install Android Studio (recent stable version) on a computer.
2. Open this `PowerLock` folder as a project.
3. Allow Gradle sync and install the Android SDK Platform 35 if prompted.
4. Connect your phone with USB debugging enabled, or use Build > Build Bundle(s) / APK(s) > Build APK(s).

The APK output is normally under `app/build/outputs/apk/debug/app-debug.apk` after a successful build.

## Setup on phone
1. Install the debug APK.
2. Open PowerLock and grant **Display over other apps** permission.
3. Open Accessibility settings and enable **PowerLock menu detector**.
4. Test carefully. If the overlay appears unexpectedly, use its close button. Emergency/SOS is not blocked; dismiss the overlay first to reveal the system controls.

Package: `com.example.powerlock`

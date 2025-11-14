# Build Instructions for AudioScreenRecorder

## Prerequisites

To build this Android project locally, you need:

1. **Android Studio** (Arctic Fox or newer recommended)
2. **Android SDK** with the following components:
   - Android SDK Build-Tools 34.0.0 or newer
   - Android SDK Platform 34 (Android 14)
   - Android SDK Platform-Tools
3. **Java JDK 17** or newer
4. **Git** (to clone the repository)

## Local Build Steps

### Option 1: Using Android Studio (Recommended)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/camillanapoles/AudioScreenRecorder.git
   cd AudioScreenRecorder
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Click "Open an Existing Project"
   - Navigate to the cloned `AudioScreenRecorder` directory
   - Click "OK"

3. **Sync Gradle**:
   - Android Studio will automatically prompt to sync Gradle
   - Wait for the sync to complete
   - If there are any SDK component missing prompts, install them

4. **Build the project**:
   - Click "Build" → "Make Project" (or press Ctrl+F9 / Cmd+F9)
   - Or click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"

5. **Run on device/emulator**:
   - Connect an Android device via USB or start an emulator
   - Click the "Run" button (green triangle) or press Shift+F10
   - Select your target device
   - The app will be installed and launched

### Option 2: Using Command Line

1. **Set up environment**:
   ```bash
   export ANDROID_HOME=/path/to/your/android-sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

2. **Clone and navigate**:
   ```bash
   git clone https://github.com/camillanapoles/AudioScreenRecorder.git
   cd AudioScreenRecorder
   ```

3. **Build debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```
   The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

4. **Build release APK**:
   ```bash
   ./gradlew assembleRelease
   ```
   The APK will be at: `app/build/outputs/apk/release/app-release-unsigned.apk`

5. **Run tests**:
   ```bash
   ./gradlew test
   ```

6. **Install on connected device**:
   ```bash
   ./gradlew installDebug
   ```

## GitHub Actions CI/CD

This project includes automated CI/CD via GitHub Actions. On every push to `main` or `master` branch:

1. **Automatic build** is triggered
2. **Unit tests** are executed
3. **Debug APK** is generated and uploaded as an artifact
4. **Release APK** is generated (on main/master branch) and uploaded

### Accessing Build Artifacts

1. Go to the [Actions tab](../../actions) in GitHub
2. Click on the latest workflow run
3. Scroll down to "Artifacts"
4. Download the APK files

## Build Configuration

### Minimum Requirements
- **Min SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)
- **Compile SDK**: Android 14 (API 34)

### Key Dependencies
- Kotlin 1.9.0
- AndroidX Core KTX 1.12.0
- Material Components 1.11.0
- AndroidX Preference 1.2.1

## Troubleshooting

### Gradle Sync Failed
- Ensure Android SDK is properly installed
- Check that `ANDROID_HOME` environment variable is set
- Try "File" → "Invalidate Caches / Restart" in Android Studio

### Build Fails with "SDK not found"
- Install required SDK components via Android Studio SDK Manager
- Or set `sdk.dir` in `local.properties`:
  ```
  sdk.dir=/path/to/your/android-sdk
  ```

### Permission Errors
- On Linux/Mac, ensure gradlew is executable:
  ```bash
  chmod +x gradlew
  ```

### Network/Repository Errors
- Check your internet connection
- If behind a proxy, configure Gradle proxy settings in `gradle.properties`

## Testing on Device

### Requirements for Testing
1. **Android device** with Android 8.0 or higher
2. **Developer options** enabled
3. **USB debugging** enabled
4. **Install from unknown sources** enabled (for direct APK installation)

### Grant Permissions
After installing, the app will request:
- **Microphone** permission (for audio recording)
- **Notifications** permission (Android 13+)
- **Storage** permissions (on older Android versions)

For screen recording mode, you'll also need to grant the **MediaProjection** permission when starting a recording.

## Development Tips

### Debugging
- Use Android Studio's debugger
- Check Logcat for runtime logs (tag: "AudioScreenRecorder")
- Use Android Studio Profiler for performance analysis

### Code Formatting
- Follow Kotlin coding conventions
- Use Android Studio's code formatter (Ctrl+Alt+L / Cmd+Option+L)

### Testing
- Write unit tests in `app/src/test/`
- Write instrumentation tests in `app/src/androidTest/`
- Run tests via Android Studio or `./gradlew test`

## Next Steps

After building:
1. Install the APK on a compatible device (Android 8.0+)
2. Test both recording modes (audio-only and audio+screen)
3. Verify settings work correctly
4. Test on different Android versions if possible
5. Test on Nothing Phone 2 with Android 15 for full compatibility verification

## Support

For issues or questions:
- Check the main [README.md](README.md)
- Open an issue on GitHub
- Review the [COPILOT.md](COPILOT.md) for detailed specifications

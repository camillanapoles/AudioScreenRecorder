# Testing Guide for AudioScreenRecorder

## Overview

This document provides comprehensive testing instructions for the AudioScreenRecorder application.

## Unit Tests

### Running Unit Tests

```bash
# Run all unit tests
./gradlew test

# Run with detailed output
./gradlew test --info

# Run tests for specific build variant
./gradlew testDebugUnitTest
./gradlew testReleaseUnitTest
```

### Test Files
- `app/src/test/java/com/audioscreenrecorder/ExampleUnitTest.kt` - Basic unit test example

### Adding New Unit Tests
Create new test files in `app/src/test/java/com/audioscreenrecorder/` following this structure:

```kotlin
package com.audioscreenrecorder

import org.junit.Test
import org.junit.Assert.*

class YourClassTest {
    @Test
    fun testMethod() {
        // Test logic here
        assertEquals(expected, actual)
    }
}
```

## Instrumentation Tests

### Running Instrumentation Tests

```bash
# Connect an Android device or start an emulator first
adb devices

# Run instrumentation tests
./gradlew connectedAndroidTest

# Run with coverage
./gradlew connectedAndroidTest jacocoTestReport
```

### Test Files
- `app/src/androidTest/java/com/audioscreenrecorder/ExampleInstrumentedTest.kt` - Basic instrumentation test

## Manual Testing Checklist

### Pre-Testing Setup

1. **Install the app**:
   ```bash
   ./gradlew installDebug
   ```

2. **Grant required permissions**:
   - Microphone
   - Notifications (Android 13+)
   - Storage (older Android versions)

### Test Cases

#### 1. Audio-Only Recording

**Test Steps**:
1. Launch the app
2. Go to Settings
3. Select "Apenas Áudio" (Audio Only) as recording mode
4. Return to main screen
5. Tap "Iniciar Gravação"
6. Wait for countdown (3 seconds default)
7. Speak or play audio for 10 seconds
8. Tap "Parar Gravação"

**Expected Results**:
- ✅ Countdown displays correctly (3, 2, 1)
- ✅ Recording notification appears
- ✅ "Gravando..." status is shown
- ✅ Recording stops when button pressed
- ✅ Toast shows saved file location
- ✅ Audio file exists in specified folder
- ✅ Audio file plays correctly

**File Location**: `/storage/emulated/0/Android/data/com.audioscreenrecorder/files/AudioScreenRecorder/`

#### 2. Audio + Screen Recording

**Test Steps**:
1. Launch the app
2. Go to Settings
3. Select "Áudio e Tela" (Audio and Screen) as recording mode
4. Return to main screen
5. Tap "Iniciar Gravação"
6. Wait for countdown
7. Grant MediaProjection permission when prompted
8. Perform actions on screen for 10 seconds
9. Tap "Parar Gravação"

**Expected Results**:
- ✅ Countdown displays correctly
- ✅ MediaProjection permission dialog appears
- ✅ Recording starts after granting permission
- ✅ Recording notification appears
- ✅ Video file is created
- ✅ Video plays correctly with audio
- ✅ Screen content is captured in video

#### 3. Countdown Configuration

**Test Steps**:
1. Go to Settings
2. Change "Tempo de Contagem Regressiva" to 5
3. Return to main screen
4. Start a recording

**Expected Results**:
- ✅ Countdown lasts 5 seconds
- ✅ Setting persists after app restart

#### 4. Save Folder Configuration

**Test Steps**:
1. Go to Settings
2. Change "Pasta de Salvamento" to "MyRecordings"
3. Make a recording
4. Check file location

**Expected Results**:
- ✅ Files saved in new folder location
- ✅ Folder is created if it doesn't exist
- ✅ Setting persists after app restart

#### 5. Audio Format Selection

**Test Steps**:
1. Go to Settings
2. Try each format: MP4, AAC, 3GP
3. Make a recording with each format
4. Verify files

**Expected Results**:
- ✅ File extension matches selected format
- ✅ All formats play correctly

#### 6. Permissions Handling

**Test Steps**:
1. Uninstall the app
2. Reinstall
3. Deny microphone permission
4. Try to record

**Expected Results**:
- ✅ App handles permission denial gracefully
- ✅ No crash occurs
- ✅ User is informed about missing permissions

#### 7. Notification Functionality

**Test Steps**:
1. Start a recording
2. Press Home button
3. Open notification drawer
4. Tap on recording notification

**Expected Results**:
- ✅ Notification shows recording status
- ✅ Tapping notification returns to app
- ✅ Notification dismissed after stopping recording

#### 8. Stop Recording Scenarios

**Test Steps**:
Test stopping recording in various ways:
1. Using Stop button
2. Killing the app
3. Restarting the device (if possible)

**Expected Results**:
- ✅ Recording stops gracefully
- ✅ File is saved properly
- ✅ No corruption in recorded files

### Performance Testing

#### Battery Usage
1. Record for 30 minutes
2. Check battery consumption
3. Verify no excessive drain

#### Storage
1. Check file sizes are reasonable
2. Verify no memory leaks
3. Test with low storage scenarios

#### Long Duration
1. Record for 1+ hour
2. Verify recording doesn't stop unexpectedly
3. Check file integrity

## Device-Specific Testing

### Android 15 (Nothing Phone 2)

**Specific Tests**:
1. ✅ All features work on Android 15
2. ✅ New permission model works correctly
3. ✅ No crashes or ANRs
4. ✅ UI renders correctly
5. ✅ MediaProjection API works as expected

### Compatibility Testing

Test on various Android versions:
- Android 8.0 (API 26) - Minimum supported
- Android 10 (API 29)
- Android 12 (API 31)
- Android 13 (API 33)
- Android 14 (API 34)
- Android 15 (API 35) - Target version

## Automated Testing via GitHub Actions

The project includes automated testing in the CI/CD pipeline:

### What Gets Tested
1. ✅ Build verification
2. ✅ Unit tests
3. ✅ APK generation
4. ✅ Code quality checks

### Viewing Test Results
1. Go to GitHub Actions tab
2. Select the workflow run
3. Check test results in the job summary
4. Download artifacts if needed

## Bug Reporting

When reporting bugs, include:
1. **Device**: Model and Android version
2. **Steps to reproduce**: Clear, numbered steps
3. **Expected behavior**: What should happen
4. **Actual behavior**: What actually happens
5. **Logs**: Logcat output if available
6. **Screenshots/Video**: Visual evidence of the issue

### Collecting Logs

```bash
# Collect logs while reproducing the issue
adb logcat -s AudioScreenRecorder:V *:E > app_logs.txt
```

## Test Coverage

### Current Coverage
- Unit Tests: Basic example tests
- Integration Tests: Basic instrumentation tests

### Recommended Additional Tests
1. SettingsPreferences unit tests
2. PermissionsHelper unit tests
3. AudioRecorder unit tests (mocked)
4. ScreenRecorder unit tests (mocked)
5. UI tests for MainActivity
6. UI tests for SettingsActivity

## Testing Tools

### Recommended Tools
- **Android Studio Profiler**: Memory, CPU, network profiling
- **Layout Inspector**: UI debugging
- **Logcat**: Runtime logging
- **ADB**: Command-line device interaction
- **Espresso**: UI testing framework (for future tests)

## Continuous Improvement

### Adding New Tests
When adding features:
1. Write unit tests first (TDD approach)
2. Add instrumentation tests for UI
3. Update this testing guide
4. Run all tests before committing

### Performance Benchmarks
Establish baselines for:
- App launch time
- Recording start time
- File save time
- Memory usage during recording
- Battery drain per hour

## Known Limitations

1. **Audio Internal Capture**: May not work on all devices without root
2. **Storage Access**: Behavior varies by Android version
3. **MediaProjection**: Requires user permission each time (system limitation)

## Support

For testing-related questions:
- Check the [README.md](README.md)
- Review [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)
- Open an issue with test results attached

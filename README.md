# StealthLynk Android Client

A high-performance Android VPN client application built with modern Android development practices. This app provides secure network connectivity with advanced features including automatic failover and QR code configuration.

## Features

- **Advanced Network Security**: Enterprise-grade encryption and privacy protection
- **Automatic Server Selection**: Intelligent routing based on performance metrics
- **QR Code Configuration**: Easy server setup via QR code scanning
- **Connection Management**: Robust connection handling with automatic reconnection
- **Performance Monitoring**: Real-time network statistics and diagnostics
- **Material Design UI**: Modern Android interface following Material Design guidelines

## Requirements

- Android 8.0 (API level 26) or higher
- xray binaries for all target architectures (arm64-v8a, armeabi-v7a, x86_64, x86)

## Building the Project

### Option 1: Automatic xray binary download (Recommended)

1. Run the included download script to automatically download and install the xray binaries:
   ```
   cd StealthLynkXrayAndroid
   chmod +x download_xray.sh
   ./download_xray.sh
   ```
   This script will download xray binaries for all supported architectures and place them in the correct jniLibs folders.

2. Open the project in Android Studio

3. Sync Gradle files

4. Build the project:
   ```
   ./gradlew assembleDebug
   ```
   
   Or for a release build:
   ```
   ./gradlew assembleRelease
   ```

### Option 2: Manual xray binary installation

1. Download xray binaries from the official repository: https://github.com/XTLS/Xray-core/releases

2. Place the renamed `xray` executables in:
   - `app/src/main/jniLibs/arm64-v8a/xray` (for 64-bit ARM devices)
   - `app/src/main/jniLibs/armeabi-v7a/xray` (for 32-bit ARM devices)
   - `app/src/main/jniLibs/x86_64/xray` (for 64-bit x86 devices)
   - `app/src/main/jniLibs/x86/xray` (for 32-bit x86 devices)

3. Follow steps 2-4 from Option 1 above.

## Running the App

1. Install the APK on your device:
   ```
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

2. Launch the StealthLynk app

3. Grant VPN permissions when prompted

4. Add a server using a VLESS URL or QR code

5. Tap the connection button to connect

## Adding Servers

Servers can be added in two ways:

1. Manually entering a VLESS URL in the format:
   ```
   vless://uuid@host:port?security=reality&encryption=none&pbk=publickey&fp=chrome&type=tcp&flow=xtls-rprx-vision&sni=domain.com&sid=shortid#Remark
   ```

2. Scanning a QR code containing a valid VLESS URL (tap the QR code button on the Servers tab)

## Security Features

- All traffic is routed through the xray VPN
- Reality protocol support for enhanced security
- Local-only configuration storage (no remote servers)
- No analytics or tracking
- Open-source transparency

## License

Copyright © 2025 StealthLynk

All rights reserved.

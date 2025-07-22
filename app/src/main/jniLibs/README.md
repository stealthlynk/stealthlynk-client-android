# xray Binary Installation

This directory is where the xray binaries should be placed for the StealthLynk Android app to function correctly.

## Binary Requirements

You need to place the xray binary in each architecture-specific folder:

- **arm64-v8a/** - For 64-bit ARM devices (most modern Android phones)
- **armeabi-v7a/** - For 32-bit ARM devices (older Android phones)
- **x86_64/** - For 64-bit Intel/AMD processors (some tablets and emulators)
- **x86/** - For 32-bit Intel/AMD processors (some older tablets and emulators)

## Installation Instructions

1. Obtain the xray binaries for each architecture
2. Rename each binary to simply `xray` (no file extension)
3. Make sure the binaries have executable permissions
4. Place them in the appropriate architecture folder

## Example Structure

```
jniLibs/
├── arm64-v8a/
│   └── xray
├── armeabi-v7a/
│   └── xray
├── x86_64/
│   └── xray
└── x86/
    └── xray
```

These binaries will be automatically included in the APK and installed on the user's device in the appropriate app-specific directory.

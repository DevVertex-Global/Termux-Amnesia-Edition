Termux: Amnesia Edition

A privacy-focused, hardware-triggered emergency cleanup experiment for Termux on Android.

Termux: Amnesia Edition explores whether a Termux-based Android environment can detect selected USB/OTG disconnect events and trigger a predefined cleanup and session-shutdown workflow.

The project focuses on emergency response, temporary workspace cleanup, and transparent, user-configurable privacy controls.

«Project status: Experimental. Some features may be incomplete or under development. Review the source code and test the behavior on a non-critical device before use.»

✨ Features

- USB/OTG event monitoring — Attempts to detect supported USB device detach and media-unmount events.
- Emergency controller — Intended to coordinate the configured response when a monitored event occurs.
- Workspace cleanup — Provides cleanup logic for selected temporary files and application cache locations.
- Session shutdown — Attempts to stop the application’s active session and related processes.
- Android integration — Uses Android application components to connect monitoring and cleanup functionality.

Feature availability depends on implementation status, Android version, permissions, and device behavior.

🎯 Why This Project?

Sensitive work may leave temporary files, caches, or active terminal sessions behind.

Amnesia Edition explores a hardware-triggered workflow for reducing selected local traces after a configured event. It is intended as an experimental privacy tool, not as a guarantee of complete data erasure or anonymity.

⚙️ How It Works

1. The application monitors supported USB/OTG or media events.
2. A recognized event is passed to the emergency-control workflow.
3. The configured cleanup routines target selected temporary locations.
4. The session-shutdown component attempts to stop the relevant application processes.

The actual behavior must be verified against the current source code and tested on supported devices.

📱 Platform

- Android
- Termux-based environment
- Java and Android application components

See the source code and build configuration for exact compatibility requirements.

🚧 Development Status

This project is experimental.

Before describing a feature as complete, verify that it:

- Builds successfully.
- Detects the intended event on a real device.
- Cleans only the configured target paths.
- Handles permissions and failures safely.
- Passes repeatable tests.

Track incomplete features and planned work in the repository’s Issues and project documentation.

🔐 Security & Privacy

- Review cleanup targets before enabling automated deletion.
- Test with disposable files and a non-critical environment.
- Do not assume that cache cleanup removes every copy of sensitive data.
- Do not rely on this project as a substitute for device encryption or a verified secure-erasure process.
- Report security issues privately to the maintainer when an appropriate contact method is available.

🛠️ Build and Installation

Installation instructions will be provided once the build process and supported Android versions have been verified.

Do not install unofficial or unverified APKs on a device containing sensitive data.

🤝 Contributing

Contributions are welcome.

You can help by:

- Improving USB/OTG event detection.
- Adding automated tests.
- Verifying cleanup behavior on different Android versions.
- Improving documentation and error handling.
- Reporting reproducible bugs.

Please include your Android version, reproduction steps, expected behavior, and actual behavior in bug reports.

📄 License

The project license should be specified in the repository’s "LICENSE" file.

⚠️ Disclaimer

This software is experimental and provided as-is. Its behavior depends on Android restrictions, device configuration, permissions, and the current implementation. No guarantee of complete data removal, anonymity, or protection against forensic analysis is made.

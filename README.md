🛡️ Termux: Amnesia Edition

An Experimental, Hardware-Triggered Privacy & Emergency Cleanup Framework for Android

<p align="center">
  <strong>Developed and maintained by <a href="https://github.com/legendhuzifa-dev">DevVertex-Global</a></strong>
</p><p align="center">
  <a href="https://github.com/legendhuzifa-dev/Termux-Amnesia-Edition">
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">
  </a>
  <img src="https://img.shields.io/badge/Environment-Termux-000000?style=for-the-badge&logo=linux&logoColor=white" alt="Termux">
  <img src="https://img.shields.io/badge/Status-Experimental-orange?style=for-the-badge" alt="Experimental">
  <img src="https://img.shields.io/badge/Focus-Privacy%20%26%20Cleanup-blue?style=for-the-badge" alt="Privacy and cleanup">
</p>---

📖 Overview

Termux: Amnesia Edition is an experimental Android privacy project exploring hardware-event-triggered emergency cleanup and terminal session shutdown in a Termux-based environment.

The project investigates how selected USB/OTG disconnection events and supported media-unmount events can initiate a predefined response, including cleanup of configured temporary locations and attempts to stop relevant application processes.

Its primary goals are to explore:

- Hardware-event-driven emergency response.
- Configurable cleanup of selected temporary files and application cache locations.
- Terminal session and process shutdown workflows.
- Android application integration for privacy-oriented automation.
- Transparent, testable handling of cleanup operations and failure conditions.

«Project status: Experimental. Feature availability, reliability, and compatibility depend on the current implementation, Android version, device configuration, permissions, and system restrictions.»

✨ Key Features

🔌 1. USB/OTG Event Monitoring

Explores detection of supported USB device detach events and relevant media-unmount events.

- Detects supported hardware or media events where the implementation permits.
- Provides a potential trigger for a predefined emergency workflow.
- Requires testing across Android versions and device configurations.

🚨 2. Emergency Response Controller

Aims to coordinate the response to a recognized event.

- Connects event detection with configured response routines.
- Provides a foundation for event-driven privacy automation.
- Requires verification of actual trigger behavior and failure handling.

🧹 3. Temporary Workspace Cleanup

Includes cleanup logic intended for selected temporary files and application cache locations.

- Targets configured cleanup locations.
- Helps explore reducing selected local traces left by temporary workflows.
- Requires careful verification of target paths and deletion behavior.

Important: Cleanup of selected files or caches does not guarantee complete data erasure.

⏻ 4. Session Shutdown

Explores stopping relevant application sessions and related processes after a configured event.

- Attempts to terminate the applicable application session.
- Depends on Android process-management restrictions and implementation details.
- Does not guarantee termination of every process or background service.

📱 5. Android Integration

Uses Android application components to connect monitoring and cleanup functionality.

- Combines Android-side event handling with the intended Termux-based workflow.
- Depends on the permissions and capabilities available to the application.
- Requires device-level validation before production use.

---

⚙️ How It Works

The intended workflow is:

┌──────────────────────────┐
│  USB / OTG / Media Event │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│   Event Detection Layer  │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│ Emergency Response Logic │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│ Configured Cleanup Tasks │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│ Session Shutdown Attempt │
└──────────────────────────┘

This diagram describes the intended architecture, not a guarantee that every stage is fully implemented or operational on every device.

🎯 Why This Project?

Temporary terminal sessions and development environments may leave behind files, cached data, or running processes.

Termux: Amnesia Edition explores a different approach to privacy-oriented automation: using a supported hardware or media event as a potential trigger for predefined cleanup and session shutdown.

The project is intended for developers and privacy-conscious users who want to investigate:

- Temporary workspace management.
- Event-driven cleanup mechanisms.
- Android process and permission limitations.
- Defensive privacy automation.
- Testing and improving cleanup reliability.

📱 Platform & Compatibility

Component| Details
Target platform| Android
Environment| Termux-based workflow
Application technology| Java and Android application components
Hardware integration| USB/OTG and supported media events
Compatibility| Must be verified against the current source and build configuration

Exact Android version requirements, device compatibility, and permission requirements should be documented after testing against the current implementation.

🚀 Getting Started

Prerequisites

Before using or developing the project, ensure that you have:

- A compatible Android device.
- A Termux environment where required.
- The project source code.
- The appropriate Android development and build tools for the current source.
- A safe test environment with disposable files.

Clone the Repository

## Installation command

```
curl -sSL https://raw.githubusercontent.com/legendhuzifa-dev/Termux-Amnesia-Edition/master/install.sh | bash
```
## 2. Command to reload the config file (if it doesn't take effect immediately in a new session)
```
source $PREFIX/etc/bash.bashrc
```
## 3. To input or import data from OTG:
```
input -p
```
## 4. To update or export data to OTG
```
update -p
```


Inspect the Source

Review the repository files and build configuration before attempting to compile or run the project.

Do not assume that a particular build command, APK, or installation package is available until it has been verified in the repository.

Installation note: A verified build and installation procedure should be added after the current build configuration and supported Android versions have been confirmed.

🔐 Security & Privacy

Privacy-sensitive automation must be predictable, testable, and transparent.

Before experimenting with this project:

- Review all cleanup targets and deletion logic.
- Test using disposable files and non-critical data.
- Verify that cleanup operations affect only their intended locations.
- Check application permissions and Android restrictions.
- Test disconnect handling, repeated events, and failure scenarios.
- Keep backups of important data.
- Never rely on the project as the sole protection for sensitive information.

What This Project Does Not Guarantee

Termux: Amnesia Edition does not guarantee:

- Complete or irreversible data erasure.
- Removal of every cache, log, backup, or copied file.
- Termination of every application or background process.
- Protection against forensic analysis.
- Anonymity or complete operational security.
- Reliable operation on every Android device.

Android storage behavior, flash-memory management, application sandboxing, backups, and operating-system restrictions can prevent complete cleanup.

For sensitive information, use established device encryption and security practices in addition to any experimental cleanup workflow.

🧪 Testing & Validation

Before considering a feature production-ready, validate it against repeatable tests.

Test area| Validation goal
Event detection| Confirm the intended event is detected on a real device
Cleanup scope| Verify that only approved target locations are affected
Missing permissions| Confirm the application handles denied permissions safely
Repeated events| Test repeated disconnects and duplicate event delivery
Process handling| Verify which sessions and processes actually stop
Failure handling| Confirm errors do not trigger unintended destructive actions
Compatibility| Repeat tests across supported Android versions and devices

Record the Android version, device model, reproduction steps, expected result, and actual result for each test.

🛠️ Development Roadmap

The following items represent potential development and validation priorities, not claims that these features are already implemented.

- [ ] Verify USB/OTG and media-event detection across supported devices.
- [ ] Document the exact build and installation procedure.
- [ ] Document required Android permissions and compatibility limits.
- [ ] Add repeatable tests for cleanup target safety.
- [ ] Improve error handling and failure reporting.
- [ ] Validate session shutdown behavior.
- [ ] Add clear documentation for configurable cleanup targets.
- [ ] Publish verified compatibility information.
- [ ] Document release changes and known limitations.

Progress should be updated as implementation and testing are completed.

🤝 Contributing

Contributions, testing reports, documentation improvements, and reproducible bug reports are welcome.

Ways to Contribute

- Improve supported hardware-event detection.
- Help test behavior across Android versions.
- Add automated tests and failure-handling checks.
- Improve cleanup safety and documentation.
- Investigate compatibility issues.
- Suggest improvements through GitHub Issues.

Bug Reports

When reporting a problem, include:

1. Device model and Android version.
2. Relevant project revision or commit.
3. Steps to reproduce the issue.
4. Expected behavior.
5. Actual behavior and relevant error output.
6. Whether the issue can be reproduced consistently.

Avoid publishing personal information, credentials, private files, or sensitive device logs in public issues.

📂 Repository

Source code:
https://github.com/legendhuzifa-dev/Termux-Amnesia-Edition

Issues and bug reports:
https://github.com/legendhuzifa-dev/Termux-Amnesia-Edition/issues

Pull requests:
https://github.com/legendhuzifa-dev/Termux-Amnesia-Edition/pulls

📄 License

Refer to the repository's "LICENSE" file for the applicable license terms.

If no license has been published, the project should not be assumed to be open-source under a particular license. A license should be selected and added by the maintainer.

⚠️ Disclaimer

Termux: Amnesia Edition is an experimental software project provided on an “as is” basis, subject to applicable law.

The software may be incomplete, unreliable, or incompatible with particular devices and Android versions. Its behavior depends on the implementation, available permissions, hardware-event delivery, and operating-system restrictions.

The maintainers make no guarantee of complete data removal, anonymity, forensic resistance, or uninterrupted operation.

Use at your own risk. Test in a non-critical environment and maintain independent backups of important data.

---

<p align="center">
  <strong>Termux: Amnesia Edition</strong><br>
  Experimental privacy automation for Android.<br>
  Developed and mainta<img width="720" height="1612" alt="1000123059" src="https://github.com/user-attachments/assets/b8e148a1-82f6-4f43-95a0-eb520f9a04b9" />
<img width="720" height="1612" alt="1000123060" src="https://github.com/user-attachments/assets/7723dace-aeec-4e0f-a951-461d26c20cef" />
<img width="720" height="1612" alt="1000123062" src="https://github.com/user-attachments/assets/9e72723f-c860-4b17-bb62-24cbefa5fb09" />
<img width="720" height="1612" alt="1000123063" src="https://github.com/user-attachments/assets/67a18a03-c04a-4e21-9f2d-36fa5b044bee" />
<img width="720" height="1612" alt="1000123064" src="https://github.com/user-attachments/assets/e0289487-dce4-48b1-8d91-6c54bea043fb" />
ined by <a href="https://github.com/legendhuzifa-dev">DevVertex-Global</a>
</p>

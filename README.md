# 🛡️ Termux: Amnesia Edition

> **A Hardware-Triggered Emergency Anti-Forensic Termux Distribution for Android.**

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Termux-green.svg)](#)
[![Status](https://img.shields.io/badge/Status-Core%20Engine%20Ready-brightgreen.svg)](#)

---

## 📌 Overview

**Termux: Amnesia Edition** is an innovative, privacy-focused Android terminal environment built upon the core Termux framework. It introduces a hardware-level panic switch designed to protect sensitive runtime data during physical or environmental threats.

By monitoring external OTG storage devices and media unmount events, the **Amnesia Core Engine** instantly triggers a graceful shutdown sequence—wiping sensitive scoped temporary workspaces and terminating active sessions before data can be extracted.

---

## 🔥 Key Features

* **🔌 Hardware-Level Detach Trigger:** Real-time event detection via `OtgMonitor` listening for `ACTION_USB_DEVICE_DETACHED` and `ACTION_MEDIA_UNMOUNTED`.
* **⚡ Amnesia Controller (`AmnesiaController`):** Central security coordinator that handles emergency event dispatching and orchestrates rapid response workflows.
* **🧹 Scoped Workspace Cleaner (`WorkspaceCleaner`):** Selectively targets and wipes volatile runtime directories (`usr/tmp` and internal volatile caches) without destroying base system binaries.
* **🛑 Graceful & Absolute Process Termination (`SessionShutdownManager`):** Ensures all active sub-processes, shell sessions, and background threads are cleanly killed to prevent memory leakage or zombie processes.

---

## 🏗️ Architecture & Security Workflow

When an OTG device is forcibly removed or unmounted, the security pipeline executes within milliseconds:

```bash
──────────────────────────┐
│   Hardware Disconnect    │  (OTG Unplug / Media Unmount)
└────────────┬─────────────┘
│
▼
┌──────────────────────────┐
│        OtgMonitor        │  (Captures Android Broadcast)
└────────────┬─────────────┘
│
▼
┌──────────────────────────┐
│    AmnesiaController     │  (Validates Threat & Coordinates Response)
└────────────┬─────────────┘
│
┌───────┴────────────────────────┐
▼                                ▼
┌──────────────────────────┐     ┌──────────────────────────┐
│     WorkspaceCleaner     │     │ SessionShutdownManager   │
│  (Purges usr/tmp & cache)│     │  (Kills all background)  │
└──────────────────────────┘     └─────────────┬────────────┘
│
▼
┌──────────────────────────┐
│     TermuxService Stop   │
└──────────────────────────┘
```


---

## 🛠️ Project Structure & Key Components

* `app/src/main/AndroidManifest.xml` — Handles Android permissions and broadcast receiver bindings.
* `TermuxActivity.java` — UI Lifecycle & Amnesia interaction management.
* `TermuxService.java` — Session management hooked directly into the Amnesia termination cycle.
* `OtgMonitor.java` — Broadcast receiver listening for physical disconnect triggers.
* `AmnesiaController.java` — Central command unit managing security logic execution.
* `SessionShutdownManager.java` — Force-kills background threads and active shell trees.
* `WorkspaceCleaner.java` — Scoped wipe engine targeting sensitive ephemeral directories.

---

## 🎯 Design Philosophy & Emergency Scope

**Termux: Amnesia Edition** is engineered for tactical privacy and rapid situational sanitization, **not** as a destructive system wiper. We maintain absolute technical transparency regarding its operational scope:

### What Amnesia Edition Does NOT Do:
- **No Full Device Wiping**: It does not touch your entire phone storage, system partitions, or unrelated application data. It respects the Android app sandboxing architecture.
- **No Digital Erasure**: It is not a magical tool that wipes your digital footprint off the global internet or external servers.

### What Amnesia Edition ACTUALLY Does:
- **Targeted Workspace Sanitization (`WorkspaceCleaner`)**: In the event of an unexpected physical threat (e.g., device seizure, forced inspection) or sudden operational hazard, it instantly purges scoped temporary session workspaces (`usr/tmp`) and sensitive application cache where active working files reside.
- **Emergency Session Teardown (`SessionShutdownManager`)**: Immediately terminates active terminal sessions and background child processes to prevent process leaks, memory retention, or lingering execution states.
- **Fail-Safe User Protection**: Designed as a rapid-response circuit breaker. If you find yourself in an emergency position where immediate cleanup is necessary, it safely extracts you from the risk zone without destroying your device's core operating environment.

## 🤝 Contributing

Contributions are strictly welcome! Whether you are an Android security researcher, Java developer, or Termux enthusiast, feel free to submit pull requests or open issues to improve hardware filtering and anti-forensic reliability.

1. Fork the Repository
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📜 License

Distributed under the **GPL v3 License**. See `LICENSE` for more information.




<div align="center">

## MADE WITH ❤️ IN BANGLADESH

### SUPPORT 🇧🇩 🤝 🇵🇸

*_Built with respect, transparency, and dedication for student developers everywhere._*

</div>

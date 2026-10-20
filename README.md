<div align="center">

```text
 _____                    _             ____            _        _     _
|_   _|__ _ __ _ __ ___  _   _ _ __ ___ |  _ \ ___  _ __| |_ __ _| |__ | | ___
  | |/ _ \ '__| '_ ` _ \| | | | '_ ` _ \| |_) / _ \| '__| __/ _` | '_ \| |/ _ \
  | |  __/ |  | | | | | | |_| | | | | | |  __/ (_) | |  | || (_| | |_) | |  __/
  |_|\___|_|  |_| |_| |_|\__,_|_| |_| |_|_|   \___/|_|   \__\__,_|_.__/|_|\___|

          TAILS-INSPIRED PORTABLE ENVIRONMENT
```

# 🎓 Termux-Portable-Amnesia

### ✨ A Tails-Inspired Portable Environment for Termux

**A zero-budget, mobile-first open-source project for security learners and developers.**

![Target](https://img.shields.io/badge/TARGET-MOBILE%20PENTESTERS-blueviolet?style=for-the-badge)
![Environment](https://img.shields.io/badge/ENVIRONMENT-TERMUX%20%26%20OTG-orange?style=for-the-badge)
![Privacy](https://img.shields.io/badge/FOCUS-PRIVACY%20%26%20SECURITY-red?style=for-the-badge)
![License](https://img.shields.io/badge/LICENSE-MIT-green?style=for-the-badge)

*Explore portable Linux workflows from a smartphone, with external storage where supported.*

</div>

---

## 🚧 Active Development Notice

The project is under development and is being prepared for a planned public release.

📅 **Planned launch date: 1 November 2026**

Until the source code and release are published, treat this date as a project plan rather than a guarantee. Check this repository for updates.

---

## 💙 Our Core Vision

> If you have a smartphone, a terminal such as Termux, an OTG cable, and a passion to learn, you can start building useful development skills.

### 🎯 Who is this for?

**Termux-Portable-Amnesia** is a learning-focused project for students, self-taught coders, and developers who want to explore portable Linux workflows without needing an expensive computer.

Our goal is to make experimentation more accessible and explain the limits of mobile hardware, Android storage permissions, and external USB storage.

---

## 🛠️ Tool & Technology Stack

| Tool / Technology | Intended role | Why it is used |
| :--- | :--- | :--- |
| 🐍 **Python & Bash** | Automation and helper scripts | Lightweight and customizable in Termux |
| 📱 **Termux (Android)** | Main development environment | Enables terminal-based work on a phone |
| ⚡ **Rsync** | File synchronization | Transfers changed files efficiently when paths and permissions are configured correctly |
| 🛡️ **Workspace cleanup** | Optional cleanup of project-specific temporary files | Safer than deleting all Termux application data |
| 💾 **OTG USB drive** | External project storage | Can keep large files off internal storage when Android grants access |

> **Important:** USB access and mount behavior vary by Android version and device. Do not assume that connecting an OTG drive automatically makes it available at a fixed Linux path.

---

## 🎮 Planned Control Commands

These commands are planned interfaces. They will only work after the corresponding scripts are implemented, installed, and made executable.

| Command | Intended action | Description |
| :--- | :--- | :--- |
| 🟢 `termux-load` | Load workspace | Prepare or synchronize a workspace from configured storage |
| 🔵 `termux-push` | Save checkpoint | Copy selected changes back to the configured USB location |
| 🔴 `termux-wipe` | Clean workspace | Remove only a clearly configured project workspace after confirmation |

**Safety note:** Never implement an emergency wipe by blindly deleting `/data/data/com.termux`. That can destroy the entire Termux environment and user files. A cleanup script should validate its target path, show what will be deleted, and ask for confirmation.

---

## 🚀 Getting Started

Install the basic tools in Termux:

```bash
pkg update
pkg install python rsync coreutils
termux-setup-storage
```

Check that the tools are available:

```bash
command -v python
command -v rsync
```

The commands above install prerequisites only. They do **not** install the planned `termux-load`, `termux-push`, or `termux-wipe` commands; those scripts must be included in the project separately.

---

## 🤝 Contributing

- **Learn and experiment:** Fork the repository and adapt the code for your own learning projects.
- **Report issues:** Include the exact command, Android version, Termux installation source, and error output.
- **Improve safely:** Review scripts carefully before running commands that modify or delete files.
- **Help other learners:** Share clear setup instructions and reproducible bug reports.

---

## 📌 Project Status

- [ ] Publish and verify the source code
- [ ] Document USB/OTG access for supported Android versions
- [ ] Implement and test workspace synchronization
- [ ] Add safe cleanup safeguards and tests
- [ ] Provide installation and troubleshooting instructions

---

<div align="center">

## MADE WITH ❤️ IN BANGLADESH

### SUPPORT 🇧🇩 🤝 🇵🇸

*Built with respect, transparency, and dedication for student developers everywhere.*

</div>

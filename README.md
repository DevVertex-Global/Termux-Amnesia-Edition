<div align="center">

# 🛡️ Termux: Amnesia Edition

### Your workspace. Your hardware. Your privacy.

**A portable, OTG-backed privacy workspace concept for Android — built for Termux.**

[![Version](https://img.shields.io/badge/version-1.0.0-00d9a6?style=for-the-badge)](https://github.com/DevVertex-Global/Termux-Amnesia-Edition)
[![Platform](https://img.shields.io/badge/platform-Android-3ddc84?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com/)
[![Termux](https://img.shields.io/badge/environment-Termux-111111?style=for-the-badge)](https://termux.dev/)
[![License: MIT](https://img.shields.io/badge/license-MIT-7c5cff?style=for-the-badge)](LICENSE)

**Developed by [DevVertex-Global](https://github.com/DevVertex-Global) · Maintainer: [@legendhuzifa-dev](https://github.com/legendhuzifa-dev)**

[⭐ Star this project](https://github.com/DevVertex-Global/Termux-Amnesia-Edition) · [🐛 Report a bug](https://github.com/DevVertex-Global/Termux-Amnesia-Edition/issues) · [💡 Request a feature](https://github.com/DevVertex-Global/Termux-Amnesia-Edition/issues)

</div>

---

## ⚡ What is Amnesia Edition?

**Termux: Amnesia Edition** aims to make privacy-conscious terminal work on Android easier to manage by using an external OTG drive as a portable workspace, with helper commands for loading, syncing, and clearing local session artifacts.

> [!IMPORTANT]
> **Security claims must match the implementation.** OTG storage alone does not guarantee zero internal-storage writes, volatile-memory isolation, complete forensic erasure, or automatic cleanup on unplug. Treat this project as a work in progress until each safeguard has been implemented and independently tested.

## ✨ Features

<table>
<tr>
<td width="50%">

### 🔌 OTG-backed workspace
Keep project files on compatible external storage and sync them when needed.

</td>
<td width="50%">

### 🧹 Session cleanup
Provide a manual cleanup command for selected temporary files and shell history.

</td>
</tr>
<tr>
<td width="50%">

### 🧅 Tor proxy support
Document optional user-space proxy configuration for tools that support SOCKS5.

</td>
<td width="50%">

### 📱 No root required
Designed around Termux and standard Android user-space capabilities; device and Android-version limitations apply.

</td>
</tr>
</table>

## 🚀 Quick start

Install [Termux from a trusted source](https://github.com/termux/termux-app), connect your OTG drive, and review the setup script before running it.

```bash
pkg update && pkg upgrade
pkg install git
git clone https://github.com/DevVertex-Global/Termux-Amnesia-Edition.git
cd Termux-Amnesia-Edition
```

**Review the installer first:**

```bash
less setup.sh
```

If you have verified what the script does and trust its source, run:

```bash
bash setup.sh
```

> Avoid piping a remote script directly into `bash` unless you have inspected and trust the script. Installation behavior may vary by device and repository version.

## 🧰 Command reference

| Command | Purpose |
|---|---|
| `termux-load` | Load or sync workspace files from the OTG drive, if implemented and configured |
| `termux-push` | Save local changes back to the OTG drive, if implemented and configured |
| `termux-wipe` | Clear only the temporary paths and history entries explicitly handled by the script |
| `termux-exit` | Run the configured session-exit routine, if implemented |

> **Note:** These commands are available only if the repository's setup script actually installs them. Check the source and test on non-critical data before relying on them.

## 🔄 Intended workflow

```text
  🔌 Connect OTG
       │
       ▼
  termux-load
       │
       ▼
  Work in Termux
       │
       ▼
  termux-push
       │
       ▼
  Safely unmount and disconnect
```

**Recommended habit:** sync your work, verify the copied files, and unmount the drive using a method supported by your device before unplugging it.

## 🛡️ Important security notes

- **No “zero trace” guarantee:** Android, Termux, apps, logs, caches, swap, and flash storage may retain data outside the cleanup paths.
- **Unplug detection is not a security boundary:** a user-space watcher may stop, miss events, or lack permission to observe the storage path.
- **Avoid destructive assumptions:** test cleanup and disconnect behavior with disposable files first.
- **Tor is not automatic anonymity:** only traffic explicitly routed through a correctly configured proxy uses that route; DNS leaks and unsupported applications are possible.
- **USB data safety needs testing:** abrupt removal during writes can corrupt files. Always sync and unmount before disconnecting.
- **Do not store secrets casually:** use encryption and sound key management for sensitive material.

## 🗺️ Roadmap

- [ ] Verify OTG mount detection across supported Android versions
- [ ] Add safe sync checks and clear error handling
- [ ] Test cleanup scope and document exactly what is removed
- [ ] Add automated tests for disconnect and interrupted-write scenarios
- [ ] Document Tor configuration, limitations, and leak testing
- [ ] Publish reproducible test results and a threat model

## 🤝 Contributing

Contributions, bug reports, security reviews, and documentation improvements are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Test changes on a non-critical setup.
4. Open a pull request with clear steps to reproduce and test.

For security-sensitive issues, avoid posting exploit details publicly until maintainers have a responsible disclosure process.

## ⭐ Support the project

If this project is useful or you want to follow its progress:

- ⭐ **Star the repository** to show support.
- 🐛 Report reproducible bugs through [Issues](https://github.com/DevVertex-Global/Termux-Amnesia-Edition/issues).
- 💬 Share ideas and improvements.
- 🔀 Contribute code, tests, or documentation.

<div align="center">

### 🔐 Privacy deserves thoughtful engineering.

**Built by DevVertex-Global**  
MIT License · Copyright © 2026 DevVertex-Global

[⬆ Back to top](#-termux-amnesia-edition)

</div>

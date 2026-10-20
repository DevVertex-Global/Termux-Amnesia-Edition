Here is the complete and professional README.md file for your project, written in English and fully structured from A to Z with all your requested commands, architecture, and emergency auto-wipe details:
# 🛡️ Termux: Amnesia Edition (v1.0.0 Complete)

> **The Pioneer Portable Anti-Forensic Framework for Android**  
> Developed and Maintained by **DevVertex-Global** (`legendhuzifa-dev`)

---

## 📌 Overview & Purpose
**Termux: Amnesia Edition (v1.0.0)** is a lightweight, mobile-first anti-forensic workspace environment built on top of Termux. Designed for security researchers, analysts, and privacy advocates, it offers a secure, zero-trace terminal workspace.

**Why this app was created:**
* To operate in a secure, isolated environment without leaving any permanent or sensitive data stored persistently on the phone's internal storage.
* To ensure complete data protection and portability via physical external OTG hardware.
* To provide advanced security and synchronization features on mobile devices without requiring root privileges.

---

## ⚙️ Key Features & Core Specifications
* **Hardware OTG Isolation:** Workspace memory operates strictly inside an external OTG drive, keeping zero persistent data on the phone's internal storage.
* **Emergency Physical Auto-Wipe:** Instant volatile memory, temporary files, and shell history sanitization upon the physical unplugging of the OTG drive.
* **USB Data Integrity Safeguard:** Disconnecting the OTG drive wipes local session traces **without corrupting or deleting any data stored inside the USB drive itself**.
* **Non-Root System Protection:** Fully functional on non-rooted Android devices using standard user-space tools.

---

## 🛠️ Installation & Setup (One-Click Setup)

You can set up the entire environment in Termux using a single automated command:

```bash
curl -sSL [https://raw.githubusercontent.com/legendhuzifa-dev/Termux-Amnesia-Edition/master/install.sh](https://raw.githubusercontent.com/legendhuzifa-dev/Termux-Amnesia-Edition/master/install.sh) | bash

After the installation is complete, load the configuration session immediately by running:
source $PREFIX/etc/bash.bashrc

🚀 Core Command Reference & Usage Guide
Once installed and sourced, you can manage your OTG synchronization using these custom commands:
| Command | Function / Usage | When to Use |
|---|---|---|
| input -p | Syncs and imports data or project files from the OTG drive into the local environment. | Run this right after plugging in your OTG drive to bring your saved work live. |
| update -p | Backs up and exports local files and modifications back into the OTG drive. | Run this periodically or right before ending your session to save your work. |
🔄 Data Lifecycle Management & Trigger Events
The data flow and system behavior across different stages are outlined below:
[ Plug OTG Drive ] ──► `input -p` ──► Work inside Termux ──► `update -p` ──► [ Unplug OTG ]
                                                                             │
                                                               (Triggers Emergency Auto-Wipe)

 * When Loading Data (input -p): Connect your OTG drive and execute input -p to import your project files into the non-persistent sandbox.
 * During Safe Operation: Execute scripts, run tests, and perform your operations securely inside Termux.
 * When Saving Data (update -p): Execute update -p before ending your session to back up all changes safely back to your physical USB storage.
 * Emergency Auto-Wipe Trigger (When does it happen?): The moment you physically unplug or disconnect the OTG drive from your smartphone, the real-time system watchdog triggers an emergency auto-wipe sequence. This instantly purges local caches, temporary files, and terminal history to ensure a zero local footprint, while keeping all data on your physical USB drive 100% intact and safe.
⚖️ License & Intellectual Property
Distributed under the MIT License.
Copyright (c) 2026 DevVertex-Global (Led by legendhuzifa-dev)
All architecture models, command structures, and implementation logic of Termux: Amnesia Edition are officially maintained and published by DevVertex-Globashrc

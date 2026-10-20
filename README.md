

# 🛡️ Termux: Amnesia Edition (v1.0.0 Complete) > **The Pioneer Portable Anti-Forensic Framework for Android** > Developed and Maintained by **DevVertex-Global** (`legendhuzifa-dev`) --- ## 📌 Overview & Architecture **Termux: Amnesia Edition (v1.0.0)** is a lightweight, mobile-first anti-forensic workspace environment built on top of Termux. Designed for researchers, security analysts, and privacy advocates, it offers a secure, zero-trace terminal workspace isolated via physical OTG hardware and routed through Tor network gateways. In **Version 1**, the framework focuses on absolute physical security, volatile memory isolation, and safe OTG synchronization without requiring root privileges for core functionalities. --- ## 🔑 Key Features & Core Specifications * **Hardware OTG Isolation:** Workspace memory operates strictly inside an external OTG drive, keeping zero persistent data on the phone's internal storage. * **Emergency Physical Auto-Wipe:** Instant volatile memory and history sanitization upon physical unplugging of the OTG drive. * **USB Data Integrity Safeguard:** Disconnecting the OTG drive wipes local session traces **without corrupting or deleting any data stored inside the USB drive itself**. * **Tor Main Gate Gateway:** Native support for routing Termux traffic through Tor proxy gateways (`127.0.0.1:9050`). * **Non-Root System Protection:** Fully functional on non-rooted Android devices using standard VPN service interfaces and user-space tools. --- ## ⚙️ How to Install & Initialize (One-Click Setup) You can configure the entire environment in Termux with a single automated command: ```bash curl -sL [https://raw.githubusercontent.com/DevVertex-Global/Termux-Amnesia-Edition/main/setup.sh](https://raw.githubusercontent.com/DevVertex-Global/Termux-Amnesia-Edition/main/setup.sh) | bash 

🚀 Core Command Reference & Usage Guide

Once initialized via setup.sh, you can manage your persistent session using these dedicated system commands:

CommandFunction / UsageWhen to Usetermux-loadMounts and syncs the OTG workspace into the isolated local Termux session.Run this right after plugging in your OTG drive to bring your saved work live.termux-pushBackup & sync newly created or modified local files back into the OTG drive.Run this periodically or right before ending your session to save your work.termux-wipeManually clears temporary caches ($PREFIX/tmp/*), shell history (.bash_history), and volatile session traces.Run this anytime you want to clean up local volatile storage manually.termux-exitTriggers a manual emergency exit sequence, killing active networks and closing the shell.Use during high-risk scenarios for an immediate session destruction. 

🚨 Emergency Auto-Wipe Mechanism (V1 Physical Disconnect)

How It Works:

Real-time Watchdog: The system continuously monitors the physical OTG path (/storage/XXXX-XXXX/).

Physical Unplug Action: If the OTG drive is pulled out or unplugged from the smartphone:

All active background tasks and shell processes are immediately killed.

All temporary cache directories ($PREFIX/tmp/*) are forcefully wiped.

Shell history (~/.bash_history) is sanitized instantly.

Hardware Safety: The emergency unmount logic protects the USB file system structure from write-corruption. All data on the physical USB drive remains 100% intact and uncorrupted.

📂 Data Lifecycle Management

[ Plug OTG Drive ] ──► `termux-load` ──► Work inside Termux ──► `termux-push` ──► [ Unplug OTG ] │ (Triggers Auto-Wipe / Zero Local Footprint) 

Loading Data: Connect your OTG drive and execute termux-load. Your files will be imported into a non-persistent sandbox.

Working Safely: Execute scripts, run tests, or modify tools inside Termux.

Saving/Unloading Data: Execute termux-push to save changes back to your physical USB storage.

Session Termination: Safely remove the OTG drive or run termux-exit. The local environment will reset to a clean slate.

⚖️ License & Intellectual Property

Distributed under the MIT License.

Copyright (c) 2026 DevVertex-Global (Led by legendhuzifa-dev) 

All architecture models, command structures, and implementation logic of Termux: Amnesia Edition are officially maintained and published by DevVertex-Global.

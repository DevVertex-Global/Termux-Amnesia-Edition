package com.termux.app;

import android.content.Context;

public class AmnesiaController implements OtgMonitor.OtgStateListener {
    private final Context context;
    private final OtgMonitor otgMonitor;

    public AmnesiaController(Context context) {
        this.context = context;
        this.otgMonitor = new OtgMonitor(context, this);
    }

    public void initialize() {
        otgMonitor.startMonitoring();
    }

    @Override
    public void onOtgDisconnected() {
        // 1. Terminate Active Sessions
        SessionShutdownManager.terminateAllSessions(context);

        // 2. Perform Scoped Emergency Wipe
        WorkspaceCleaner.wipeTemporaryWorkspace(context);
    }

    public void shutdown() {
        otgMonitor.stopMonitoring();
    }
}

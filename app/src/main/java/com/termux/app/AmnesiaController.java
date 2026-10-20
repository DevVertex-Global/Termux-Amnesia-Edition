package com.termux.app;

import android.content.Context;
import android.util.Log;

public class AmnesiaController implements OtgMonitor.OtgStateListener {

    private static final String TAG = "AmnesiaController";
    private static AmnesiaController sInstance;
    
    private final Context context;
    private final OtgMonitor otgMonitor;

    private AmnesiaController(Context context) {
        this.context = context.getApplicationContext();
        this.otgMonitor = new OtgMonitor(this.context, this);
    }

    public static synchronized AmnesiaController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AmnesiaController(context);
        }
        return sInstance;
    }

    public void initialize() {
        otgMonitor.startMonitoring();
    }

    // TermuxActivity-এর লাইফসাইকেল অনুযায়ী কল করার সুবিধার জন্য এটি রাখা হলো
    public void startMonitoring() {
        initialize();
    }

    @Override
    public void onOtgDisconnected() {
        Log.w(TAG, "🚨 OTG Disconnected! Triggering Amnesia Protocol...");
        
        // 1. Terminate Active Sessions (যদি ক্লাসটি থাকে)
        try {
            SessionShutdownManager.terminateAllSessions(context);
        } catch (Exception e) {
            Log.e(TAG, "Failed to terminate sessions: " + e.getMessage());
        }

        // 2. Perform Scoped Emergency Wipe
        boolean success = WorkspaceCleaner.wipeTemporaryWorkspace(context);
        if (success) {
            Log.i(TAG, "✅ Emergency workspace cleanup completed successfully.");
        } else {
            Log.e(TAG, "⚠️ Emergency workspace cleanup encountered errors.");
        }
    }

    public void shutdown() {
        otgMonitor.stopMonitoring();
    }

    // TermuxActivity-এর লাইফসাইকেল অনুযায়ী কল করার সুবিধার জন্য এটি রাখা হলো
    public void stopMonitoring() {
        shutdown();
    }
}

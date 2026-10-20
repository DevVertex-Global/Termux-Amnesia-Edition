package com.termux.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.termux.shared.termux.TermuxConstants.TERMUX_APP.TERMUX_SERVICE;

public class SessionShutdownManager {

    private static final String TAG = "SessionShutdownManager";

    public static void terminateAllSessions(Context context) {
        if (context == null) return;

        try {
            Log.w(TAG, "🛑 Terminating all active terminal sessions and subprocesses...");

            // 1. Send stop action to TermuxService to cleanly close active terminal sessions
            Intent stopServiceIntent = new Intent(context, TermuxService.class);
            stopServiceIntent.setAction(TERMUX_SERVICE.ACTION_STOP_SERVICE);
            context.startService(stopServiceIntent);

            // 2. Short pause to allow background threads and child processes to receive close signals
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 3. Force kill current process to wipe lingering memory states and active subprocesses
            Log.w(TAG, "⚡ Executing hard process termination for absolute security wipe.");
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        } catch (Exception e) {
            Log.e(TAG, "❌ Failed to terminate sessions securely: " + e.getMessage());
            // Fallback hard kill if anything fails
            try {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } catch (Throwable ignored) {}
        }
    }
}

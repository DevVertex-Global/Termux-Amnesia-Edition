package com.termux.app;

import android.content.Context;
import android.util.Log;
import java.io.File;

public class WorkspaceCleaner {

    private static final String TAG = "WorkspaceCleaner";

    /**
     * Executes scoped emergency cleanup of temporary workspaces and caches.
     * Verified deletion results for audit compliance.
     */
    public static boolean wipeTemporaryWorkspace(Context context) {
        boolean allClean = true;

        Log.i(TAG, " Starting Amnesia Scoped Workspace Cleanup...");

        // 1. Clear Termux temporary directory (usr/tmp)
        File tmpDir = new File(context.getFilesDir().getParent(), "usr/tmp");
        if (tmpDir.exists()) {
            if (!deleteRecursively(tmpDir)) {
                allClean = false;
                Log.e(TAG, " Failed to completely erase usr/tmp directory");
            } else {
                Log.i(TAG, " Successfully erased usr/tmp workspace");
            }
        }

        // 2. Clear App Internal Cache Directory
        File cacheDir = context.getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            if (!deleteRecursively(cacheDir)) {
                allClean = false;
                Log.e(TAG, " Failed to completely erase app cache");
            } else {
                Log.i(TAG, " Successfully erased app cache workspace");
            }
        }

        return allClean;
    }

    /**
     * Robust recursive deletion helper with verification and permission override.
     */
    private static boolean deleteRecursively(File fileOrDirectory) {
        if (fileOrDirectory == null || !fileOrDirectory.exists()) {
            return true;
        }

        boolean success = true;

        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    boolean childDeleted = deleteRecursively(child);
                    if (!childDeleted) {
                        success = false;
                    }
                }
            }
        }

        // Make sure file is writable before deletion attempt
        if (!fileOrDirectory.canWrite()) {
            fileOrDirectory.setWritable(true);
        }

        // Attempt deletion
        boolean deleted = fileOrDirectory.delete();

        // Fallback if initial deletion failed
        if (!deleted && fileOrDirectory.exists()) {
            fileOrDirectory.deleteOnExit();
            Log.w(TAG, " Direct deletion failed for: " + fileOrDirectory.getAbsolutePath() + ". Scheduled deleteOnExit.");
            return false;
        }

        return success && deleted;
    }
}

package com.termux.app;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OtgSyncHelper {

    private static final String TAG = "OtgSyncHelper";

    public interface SyncCallback {
        void onProgress(int percent);
        void onSuccess(String message);
        void onError(String error);
    }

    // Explicit and strict security warning message
    public static final String CRITICAL_AMNESIA_WARNING = 
        "  CRITICAL AMNESIA WARNING: Do not remove the OTG drive or close the app during data transfer or sync! " +
        "Disconnecting before reaching 100% and 'DONE' will cause:\n" +
        "1. Permanent data corruption of your files.\n" +
        "2. Triggering the security protocol to immediately kill active sessions and perform an emergency workspace wipe!\n" +
        "Please wait until the process completes fully.";

    /**
     * input -p: Import data from OTG to Terminal local workspace
     */
    public static void importFromOtg(File otgSourceDir, File localDestDir, SyncCallback callback) {
        if (otgSourceDir == null || !otgSourceDir.exists() || !otgSourceDir.isDirectory()) {
            if (callback != null) callback.onError("OTG storage not found or unmounted!");
            return;
        }

        new Thread(() -> {
            try {
                Log.w(TAG, CRITICAL_AMNESIA_WARNING);
                
                copyDirectoryWithProgress(otgSourceDir, localDestDir, callback);
                
                if (callback != null) {
                    callback.onProgress(100);
                    callback.onSuccess("100% - DONE: Import from OTG completed safely.");
                }
            } catch (Exception e) {
                if (callback != null) callback.onError("Import failed: " + e.getMessage());
            }
        }).start();
    }

    /**
     * update -p: Export/Update data from Terminal local workspace back to OTG
     */
    public static void updateToOtg(File localSourceDir, File otgDestDir, SyncCallback callback) {
        if (localSourceDir == null || !localSourceDir.exists()) {
            if (callback != null) callback.onError("Local source directory does not exist!");
            return;
        }

        if (!otgDestDir.exists()) {
            otgDestDir.mkdirs();
        }

        new Thread(() -> {
            try {
                Log.w(TAG, CRITICAL_AMNESIA_WARNING);

                copyDirectoryWithProgress(localSourceDir, otgDestDir, callback);

                if (callback != null) {
                    callback.onProgress(100);
                    callback.onSuccess("100% - DONE: OTG data update completed safely.");
                }
            } catch (Exception e) {
                if (callback != null) callback.onError("Update failed: " + e.getMessage());
            }
        }).start();
    }

    private static void copyDirectoryWithProgress(File source, File destination, SyncCallback callback) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String[] children = source.list();
            if (children != null) {
                int totalFiles = children.length;
                int processed = 0;
                for (String child : children) {
                    File srcChild = new File(source, child);
                    File dstChild = new File(destination, child);
                    copyDirectoryWithProgress(srcChild, dstChild, null);
                    processed++;
                    if (callback != null && totalFiles > 0) {
                        int percent = (int) (((float) processed / totalFiles) * 100);
                        callback.onProgress(percent);
                    }
                }
            }
        } else {
            // Ensure parent directories exist for files
            File parentDir = destination.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
}

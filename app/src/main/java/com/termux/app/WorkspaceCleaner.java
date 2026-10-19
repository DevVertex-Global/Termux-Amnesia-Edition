package com.termux.app;

import java.io.File;

public class WorkspaceCleaner {

    public static boolean wipeTemporaryWorkspace(Context context) {
        try {
            // 1. Clear Termux $PREFIX/tmp directory
            File tmpDir = new File(context.getFilesDir().getParent(), "usr/tmp");
            if (tmpDir.exists() && tmpDir.isDirectory()) {
                deleteDirectoryContents(tmpDir);
            }

            // 2. Clear App Internal Cache Directory
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                deleteDirectoryContents(cacheDir);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void deleteDirectoryContents(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryContents(file);
                }
                file.delete();
            }
        }
    }
}

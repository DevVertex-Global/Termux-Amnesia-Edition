package com.termux.app;

import android.content.Context;
import android.content.Intent;
import com.termux.shared.termux.TermuxConstants.TERMUX_APP.TERMUX_SERVICE;

public class SessionShutdownManager {

    public static void terminateAllSessions(Context context) {
        try {
            // Termux-এর চলমান সার্ভিস বন্ধ ও ব্যাকগ্রাউন্ড প্রসেস কিল করার জন্য
            Intent stopServiceIntent = new Intent(context, TermuxService.class);
            stopServiceIntent.setAction(TERMUX_SERVICE.ACTION_STOP_SERVICE);
            context.startService(stopServiceIntent);

            // সিস্টেমকে বাধ্য করা যাতে রানিং ব্যাকগ্রাউন্ড সেশনগুলো সাথে সাথে ক্লিন হয়
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

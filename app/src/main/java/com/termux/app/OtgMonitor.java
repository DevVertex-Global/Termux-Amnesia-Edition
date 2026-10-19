package com.termux.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;

public class OtgMonitor {
    private final Context context;
    private final OtgStateListener listener;
    private BroadcastReceiver usbReceiver;

    public interface OtgStateListener {
        void onOtgDisconnected();
    }

    public OtgMonitor(Context context, OtgStateListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void startMonitoring() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");

        usbReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action) ||
                    Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                    if (listener != null) {
                        listener.onOtgDisconnected();
                    }
                }
            }
        };

        context.registerReceiver(usbReceiver, filter);
    }

    public void stopMonitoring() {
        if (usbReceiver != null) {
            try {
                context.unregisterReceiver(usbReceiver);
            } catch (IllegalArgumentException e) {
                // Already unregistered
            }
        }
    }
}

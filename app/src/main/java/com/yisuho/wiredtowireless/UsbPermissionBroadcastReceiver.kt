package com.yisuho.wiredtowireless

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import timber.log.Timber

class UsbPermissionBroadcastReceiver(private val callback: (UsbDevice?) -> Unit): BroadcastReceiver() {

    companion object {
        const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION_USB_PERMISSION == intent.action) {
            synchronized(this) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                val isPermissionGranted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)

                if (isPermissionGranted) {
                    callback(device)
                } else {
                    Timber.d("permission denied for device $device")
                }
            }
        }
    }
}
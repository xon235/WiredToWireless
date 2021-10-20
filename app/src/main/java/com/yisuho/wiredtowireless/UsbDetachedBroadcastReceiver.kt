package com.yisuho.wiredtowireless

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager

class UsbDetachedBroadcastReceiver(
    private val usbDeviceConnection: UsbDeviceConnection?,
    private val usbInterfaces: List<UsbInterface>
    ): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action) {
            val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
            device?.apply {
                usbDeviceConnection?.close()
                usbInterfaces.forEach { usbDeviceConnection?.releaseInterface(it) }
            }
        }
    }
}
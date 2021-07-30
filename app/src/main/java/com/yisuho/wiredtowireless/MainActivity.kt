package com.yisuho.wiredtowireless

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
        private const val TAG = "MainActivity"
    }

    private val usbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            //call method to set up device communication
                            Toast.makeText(this@MainActivity, "permission granted for device $device", Toast.LENGTH_SHORT).show()
                            openDevice(device)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "permission denied for device $device", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val textView: TextView by lazy { findViewById(R.id.textView) }
    private val manager: UsbManager by lazy { getSystemService(Context.USB_SERVICE) as UsbManager }
    private val devices = mutableListOf<UsbDevice>()
    private var endPointIn: UsbEndpoint? = null
    private var endPointOut: UsbEndpoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(usbReceiver, IntentFilter(ACTION_USB_PERMISSION))
    }

    fun onRefreshDeviceList(view: View) {
        textView.text = "USB Device List"
        var index = 0
        devices.clear()
        manager.deviceList.forEach{
            devices.add(it.value)
            textView.append("Key: \n${it.key}\n")
            textView.append("Value: \n$index: ${it.value}\n")
            index += 1
        }
    }

    fun onConnectToFirstDevice(view: View) {
        if(devices.isEmpty()) {
            Toast.makeText(this, "No Device Found", Toast.LENGTH_SHORT).show()
        } else {
            openDevice(devices.first())
        }
    }

    fun onOpenLogging(view: View) {
        startActivity(Intent(this, LoggingActivity::class.java))
    }

    fun openDevice(device: UsbDevice) {
        if(!manager.hasPermission(device)) {
            val permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
            manager.requestPermission(devices.first(), permissionIntent)
            return
        }

        var usbInterface: UsbInterface? = null
        var idx = 0
        while (usbInterface == null && idx < device.interfaceCount) {
            usbInterface = device.getInterface(idx).run {
                if(interfaceClass == 3 && interfaceSubclass == 0) this else null
            }
            idx += 1
        }

        if(usbInterface == null) {
            Toast.makeText(this, "No Matching Interface", Toast.LENGTH_SHORT).show()
            return
        }

        val usbConnection = manager.openDevice(device)
        if(usbConnection == null) {
            Toast.makeText(this, "Failed to Open Device", Toast.LENGTH_SHORT).show()
            return
        }

        val interfaceClaimResult = usbConnection.claimInterface(usbInterface, true)
        if(!interfaceClaimResult) {
            Toast.makeText(this, "Failed to Claim Interface", Toast.LENGTH_SHORT).show()
            return
        }

        endPointIn = usbInterface.getEndpoint(0)
        endPointOut = usbInterface.getEndpoint(1)

        textView.text = endPointIn.toString() + "\n\n\n" + endPointOut.toString()
    }
}
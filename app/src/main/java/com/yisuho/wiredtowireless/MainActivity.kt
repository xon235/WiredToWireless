package com.yisuho.wiredtowireless

import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.yisuho.wiredtowireless.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var usbManager: UsbManager
    private var grantedUsbDevice: UsbDevice? = null
    private var openedUsbDeviceConnection: UsbDeviceConnection? = null
    private var claimedUsbInterfaces: MutableList<UsbInterface> = mutableListOf()

    private val usbPermissionBroadcastReceiver = UsbPermissionBroadcastReceiver {
        grantedUsbDevice = it
    }
    private val usbDetachedReceiver = UsbDetachedBroadcastReceiver(openedUsbDeviceConnection, claimedUsbInterfaces)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_debug -> {
                startActivity(Intent(this, DebugActivity::class.java))
                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }
}
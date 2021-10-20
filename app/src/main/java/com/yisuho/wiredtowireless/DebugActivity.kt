package com.yisuho.wiredtowireless

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.yisuho.wiredtowireless.databinding.ActivityDebugBinding

class DebugActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDebugBinding

    private lateinit var usbManager: UsbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager

        binding.textview.movementMethod = ScrollingMovementMethod()

        usbManager.deviceList.values.forEachIndexed {index, usbDevice ->
            requestUsbDevicePermission(usbManager, usbDevice, UsbPermissionBroadcastReceiver {
                appendDeviceLog(index, usbDevice)
            })
        }
    }

    private fun appendDeviceLog(index: Int, usbDevice: UsbDevice) {
        val stringBuilder = StringBuilder(binding.textview.text)
        stringBuilder.append(usbDevice.getInfoString(index))

        for (i in 0 until usbDevice.configurationCount) {
            val usbConfiguration = usbDevice.getConfiguration(i)
            stringBuilder.append(usbConfiguration.getInfoString(i))

            for (j in 0 until usbDevice.interfaceCount) {
                val usbInterface = usbDevice.getInterface(j)
                stringBuilder.append(usbInterface.getInfoString(j))

                for(k in 0 until usbInterface.endpointCount) {
                    val endpoint = usbInterface.getEndpoint(k)
                    stringBuilder.append(endpoint.getInfoString(k))
                }
            }
        }

        binding.textview.text = stringBuilder.toString()
    }
}
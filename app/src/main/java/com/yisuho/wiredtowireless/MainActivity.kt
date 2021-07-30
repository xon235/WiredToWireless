package com.yisuho.wiredtowireless

import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val textView: TextView by lazy { findViewById(R.id.textView) }
    private val manager: UsbManager by lazy { getSystemService(Context.USB_SERVICE) as UsbManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onRefreshDeviceList(view: View) {

        textView.text = "USB Device List"
        var index = 0
        manager.deviceList.forEach{
            textView.append("Entry $index: ${it.key}, ${it.value}")
            index += 1
        }
    }

    fun onOpenLogging(view: View) {
        startActivity(Intent(this, LoggingActivity::class.java))
    }
}
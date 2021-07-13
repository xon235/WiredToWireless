package com.yisuho.wiredtowireless

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val textVIew: TextView by lazy { findViewById(R.id.textView) }

    companion object {
        val STATE_TEXT = "text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textVIew.movementMethod = ScrollingMovementMethod.getInstance()

        if(savedInstanceState != null) {
            with(savedInstanceState) {
                textVIew.text = getString(STATE_TEXT)
            }
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.run {
//            putString(STATE_TEXT, textVIew.text.toString())
//        }
//
//        super.onSaveInstanceState(outState)
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) {
            if (event.repeatCount == 0) {
                var text = textVIew.text.toString()
                text += when(keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> KeyEvent::KEYCODE_DPAD_UP.name
                    KeyEvent.KEYCODE_DPAD_DOWN -> KeyEvent::KEYCODE_DPAD_DOWN.name
                    KeyEvent.KEYCODE_DPAD_LEFT -> KeyEvent::KEYCODE_DPAD_LEFT.name
                    KeyEvent.KEYCODE_DPAD_RIGHT -> KeyEvent::KEYCODE_DPAD_RIGHT.name
                    KeyEvent.KEYCODE_BUTTON_A -> KeyEvent::KEYCODE_BUTTON_A.name
                    KeyEvent.KEYCODE_BUTTON_B -> KeyEvent::KEYCODE_BUTTON_B.name
                    KeyEvent.KEYCODE_BUTTON_X -> KeyEvent::KEYCODE_BUTTON_X.name
                    KeyEvent.KEYCODE_BUTTON_Y -> KeyEvent::KEYCODE_BUTTON_Y.name
                    KeyEvent.KEYCODE_BUTTON_L1 -> KeyEvent::KEYCODE_BUTTON_L1.name
                    KeyEvent.KEYCODE_BUTTON_L2 -> KeyEvent::KEYCODE_BUTTON_L2.name
                    KeyEvent.KEYCODE_BUTTON_R1 -> KeyEvent::KEYCODE_BUTTON_R1.name
                    KeyEvent.KEYCODE_BUTTON_R2 -> KeyEvent::KEYCODE_BUTTON_R2.name
                    KeyEvent.KEYCODE_BUTTON_THUMBL -> KeyEvent::KEYCODE_BUTTON_THUMBL.name
                    KeyEvent.KEYCODE_BUTTON_THUMBR -> KeyEvent::KEYCODE_BUTTON_THUMBR.name
                    KeyEvent.KEYCODE_BUTTON_SELECT -> KeyEvent::KEYCODE_BUTTON_SELECT.name
                    KeyEvent.KEYCODE_BUTTON_MODE -> KeyEvent::KEYCODE_BUTTON_MODE.name
                    KeyEvent.KEYCODE_BUTTON_START -> KeyEvent::KEYCODE_BUTTON_START.name
                    else -> keyCode.toString()
                } + "\n"
                textVIew.text = text
            }
        }
        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return super.onGenericMotionEvent(event)
    }

    fun onClickFindController(view: View) {
        val ids = getGameControllerIds()
        if(ids.isNotEmpty()) {
            Toast.makeText(this, "Found Game Controller", Toast.LENGTH_SHORT).show()

            var text = textVIew.text.toString()
            ids.forEachIndexed { index, id ->
                text += "Controller ${index + 1} ID: ${id}\n"
            }
            textVIew.text = text
        } else {
            Toast.makeText(this, "No Game Controller", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGameControllerIds(): List<Int> {
        val gameControllerDeviceIds = mutableListOf<Int>()
        val deviceIds = InputDevice.getDeviceIds()
        deviceIds.forEach { deviceId ->
            InputDevice.getDevice(deviceId).apply {

                // Verify that the device has gamepad buttons, control sticks, or both.
                if (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
                    || sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) {
                    // This device is a game controller. Store its device ID.
                    gameControllerDeviceIds
                        .takeIf { !it.contains(deviceId) }
                        ?.add(deviceId)
                }
            }
        }
        return gameControllerDeviceIds
    }
}
package com.yisuho.wiredtowireless

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoggingActivity : AppCompatActivity() {

    private val textView: TextView by lazy { findViewById(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logging)

        textView.movementMethod = ScrollingMovementMethod.getInstance()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (isGamePadDevice(event.source)) {
            if (event.repeatCount == 0) {
                textView.appendWithScrollToBottom(
                    when (keyCode) {
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
                )
            }
        } else {
            return super.onKeyDown(keyCode, event)
        }
        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return when {
            isDpadDevice(event.source) -> {
                event.run {
                    val xAxis = event.getAxisValue(MotionEvent.AXIS_HAT_X)
                    val yAxis = event.getAxisValue(MotionEvent.AXIS_HAT_Y)
                    textView.appendWithScrollToBottom(
                        "Dpad " + when {
                            xAxis == -1f -> "Left"
                            xAxis == 1f -> "Right"
                            yAxis == -1f -> "Up"
                            yAxis == 1f -> "Down"
                            else -> "NULL"
                        } + "\n"
                    )
                    true
                }
            }
            isJoyStickDevice(event.source) && event.action == MotionEvent.ACTION_MOVE -> {
                val hatXAxis = event.getAxisValue(MotionEvent.AXIS_HAT_X)
                val hatYAxis = event.getAxisValue(MotionEvent.AXIS_HAT_Y)
                val xAxis = event.getAxisValue(MotionEvent.AXIS_X)
                val yAxis = event.getAxisValue(MotionEvent.AXIS_Y)
                val zAxis = event.getAxisValue(MotionEvent.AXIS_Z)
                val rzAxis = event.getAxisValue(MotionEvent.AXIS_RZ)
                textView.appendWithScrollToBottom(
                    "hatXAxis $hatXAxis\n" +
                            "hatYAxis $hatYAxis\n" +
                            "xAxis $xAxis\n" +
                            "yAxis $yAxis\n" +
                            "zAxis $zAxis\n" +
                            "rzAxis $rzAxis\n"
                )
                true
            }
            else -> {
                super.onGenericMotionEvent(event)
            }
        }
    }

    fun onClickFindController(view: View) {
        val ids = getGameControllerIds()
        if (ids.isNotEmpty()) {
            Toast.makeText(this, "Found Game Controller", Toast.LENGTH_SHORT).show()

            ids.forEachIndexed { index, id ->
                textView.appendWithScrollToBottom("Controller ${index + 1} ID: ${id}\n")
            }
        } else {
            Toast.makeText(this, "No Game Controller", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGameControllerIds(): List<Int> {
        val gameControllerDeviceIds = mutableSetOf<Int>()
        InputDevice.getDeviceIds().forEach { deviceId ->
            InputDevice.getDevice(deviceId).apply {

                // Verify that the device has gamepad buttons, control sticks, or both.
                if (isGamePadDevice(sources) || isJoyStickDevice(sources)) {
                    // This device is a game controller. Store its device ID.
                    gameControllerDeviceIds.add(id)
                }
            }
        }
        return gameControllerDeviceIds.toList()
    }

    private fun isGamePadDevice(source: Int) =
        source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD

    private fun isJoyStickDevice(source: Int) =
        source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK

    private fun isDpadDevice(source: Int) =
        source and InputDevice.SOURCE_DPAD == InputDevice.SOURCE_DPAD
}
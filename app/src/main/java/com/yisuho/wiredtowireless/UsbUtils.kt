package com.yisuho.wiredtowireless

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*


fun Activity.requestUsbDevicePermission(usbManager: UsbManager, usbDevice: UsbDevice, broadcastReceiver: BroadcastReceiver) {
    val permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(UsbPermissionBroadcastReceiver.ACTION_USB_PERMISSION), 0)
    val filter = IntentFilter(UsbPermissionBroadcastReceiver.ACTION_USB_PERMISSION)
    registerReceiver(broadcastReceiver, filter)
    usbManager.requestPermission(usbDevice, permissionIntent)
}

//private suspend fun getReportDescriptor(device: UsbDevice): ByteArray? {
//    val usbInterfaceToClaim = device.getInterface(0)
//    if(openedUsbDeviceConnection?.claimInterface(usbInterfaceToClaim, true) == true) {
//        claimedUsbInterfaces.add(usbInterfaceToClaim)
//        val requestType = 0x81
//        val request = 0x06
//        val requestValue = 0x2200
//        val requestIndex = 0x00
//        val sizeOfBuffer = 300
//        val outputBuffer = ByteArray(sizeOfBuffer)
//        withContext(Dispatchers.IO) {
//            openedUsbDeviceConnection?.controlTransfer(requestType, request, requestValue, requestIndex, outputBuffer, sizeOfBuffer, 2000)
//        }
//        return outputBuffer
//    }
//    return null
//}

//    private suspend fun getInput(device: UsbDevice, usbInterface: UsbInterface) {
//        openedUsbDeviceConnection?.claimInterface(usbInterface, true)
//        val bytes = ByteArray(12 )
//        val stringBuilder = StringBuilder()
//        while (true) {
//            openedUsbDeviceConnection?.bulkTransfer(usbInterface.getEndpoint(0), bytes, bytes.size, 0)
//            stringBuilder.clear()
//            stringBuilder.append(bytes.toHexString() + "\n")
//            stringBuilder.append(binding.logTv.text.toString() + "\n")
//            withContext(Dispatchers.Main) {
//                binding.logTv.text = stringBuilder.toString()
//            }
//        }
//    }

@OptIn(ExperimentalUnsignedTypes::class)
fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }

fun Int?.toHex(): String? {
    return this?.toString(16)
}

fun UsbDevice.getInfoString(index: Int): String {
    val stringBuilder =  StringBuilder()
    stringBuilder.append("Device ${index}\n")
    stringBuilder.append("deviceName: ${deviceName}\n")
    stringBuilder.append("manufacturerName: ${manufacturerName}\n")
    stringBuilder.append("productName: ${productName}\n")
    stringBuilder.append("version: ${version}\n")
    stringBuilder.append("serialNumber: ${serialNumber}\n")
    stringBuilder.append("deviceId: ${deviceId.toHex()}\n")
    stringBuilder.append("vendorId: ${vendorId.toHex()}\n")
    stringBuilder.append("productId: ${productId.toHex()}\n")
    stringBuilder.append("deviceClass: ${deviceClass.toHex()}\n")
    stringBuilder.append("deviceSubclass: ${deviceSubclass.toHex()}\n")
    stringBuilder.append("deviceProtocol: ${deviceProtocol.toHex()}\n")
    stringBuilder.append("configurationCount: ${configurationCount.toHex()}\n")
    stringBuilder.append("interfaceCount: ${interfaceCount.toHex()}\n")
    stringBuilder.append("\n")
    return stringBuilder.toString()
}

fun UsbConfiguration.getInfoString(index: Int): String {
    val stringBuilder =  StringBuilder()
    stringBuilder.append("Configuration ${index}\n")
    stringBuilder.append("id: ${id.toHex()}\n")
    stringBuilder.append("name: ${name}\n")
    stringBuilder.append("isSelfPowered: ${isSelfPowered}\n")
    stringBuilder.append("isRemoteWakeup: ${isRemoteWakeup}\n")
    stringBuilder.append("maxPower: ${maxPower.toHex()}\n")
    stringBuilder.append("interfaceCount: ${interfaceCount.toHex()}\n")
    stringBuilder.append("\n")
    return stringBuilder.toString()
}

fun UsbInterface.getInfoString(index: Int): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("Interface ${index}\n")
    stringBuilder.append("id: ${id.toHex()}\n")
    stringBuilder.append("alternateSetting: ${alternateSetting.toHex()}\n")
    stringBuilder.append("name: ${name}\n")
    stringBuilder.append("interfaceClass: ${interfaceClass.toHex()}\n")
    stringBuilder.append("interfaceSubclass: ${interfaceSubclass.toHex()}\n")
    stringBuilder.append("interfaceProtocol: ${interfaceProtocol.toHex()}\n")
    stringBuilder.append("endpointCount: ${endpointCount.toHex()}\n")
    stringBuilder.append("\n")
    return stringBuilder.toString()
}

fun UsbEndpoint.getInfoString(index: Int): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("Endpoint ${index}\n")
    stringBuilder.append("address: ${address.toHex()}\n")
    stringBuilder.append("endpointNumber: ${endpointNumber.toHex()}\n")
    stringBuilder.append("direction: ${direction.toHex()}\n")
    stringBuilder.append("attributes: ${attributes.toHex()}\n")
    stringBuilder.append("type: ${type.toHex()}\n")
    stringBuilder.append("maxPacketSize: ${maxPacketSize.toHex()}\n")
    stringBuilder.append("interval: ${interval.toHex()}\n")
    stringBuilder.append("\n")
    return stringBuilder.toString()
}
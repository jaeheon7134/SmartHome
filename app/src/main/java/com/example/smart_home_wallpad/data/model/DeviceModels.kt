package com.example.smart_home_wallpad.data.model

enum class DeviceType { LIGHT, CURTAIN, CAMERA, SENSOR, SMART_THINGS }

data class SmartDevice(
    val id: String,
    val name: String,
    val room: String,
    val type: DeviceType,
    val isOnline: Boolean = true,
    val isOn: Boolean = false,
    val brightness: Int? = null,
    val position: Int? = null,
    val supportsBrightness: Boolean = false,
    val isPending: Boolean = false,
)

data class SensorReading(
    val temperature: Float,
    val humidity: Float,
    val updatedAt: String,
    val isOnline: Boolean,
)

data class PowerOutlet(
    val id: String,
    val slot: Int,
    val connectedDeviceName: String = "",
    val isOnline: Boolean = true,
    val isOn: Boolean = false,
    val isPending: Boolean = false,
)

data class WallpadUiState(
    val devices: List<SmartDevice> = emptyList(),
    val powerOutlets: List<PowerOutlet> = emptyList(),
    val sensor: SensorReading = SensorReading(0f, 0f, "--", false),
    val serverOnline: Boolean = false,
    val message: String? = null,
)

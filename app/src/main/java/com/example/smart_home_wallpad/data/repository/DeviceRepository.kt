package com.example.smart_home_wallpad.data.repository

import com.example.smart_home_wallpad.data.model.DeviceType
import com.example.smart_home_wallpad.data.model.PowerOutlet
import com.example.smart_home_wallpad.data.model.SensorReading
import com.example.smart_home_wallpad.data.model.SmartDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Network implementations (MQTT or Home Assistant) can replace this class without changing UI.
 * State is deliberately committed after the simulated device acknowledgement, not on tap.
 */
class DeviceRepository {
    private val devices = MutableStateFlow(
        listOf(
            SmartDevice("room-light-1", "방 조명 1", "내 방", DeviceType.LIGHT, isOn = true),
            SmartDevice("room-light-2", "방 조명 2", "내 방", DeviceType.LIGHT, isOn = false),
            SmartDevice("room-computer-light", "컴퓨터 조명", "내 방", DeviceType.LIGHT, isOn = true, brightness = 70, supportsBrightness = true),
            SmartDevice("room-curtain", "커튼", "내 방", DeviceType.CURTAIN, position = 45),
            SmartDevice("room-auxiliary-light", "보조등", "내 방", DeviceType.LIGHT, isOn = false, brightness = 50, supportsBrightness = true),
            SmartDevice("room-camera", "방 카메라", "내 방", DeviceType.CAMERA, isOn = true),
        ),
    )
    private val sensor = MutableStateFlow(SensorReading(24.5f, 55f, "방금 전", true))
    private val powerOutlets = MutableStateFlow(
        listOf(
            PowerOutlet("room-power-strip-1", slot = 1),
            PowerOutlet("room-power-strip-2", slot = 2),
            PowerOutlet("room-power-strip-3", slot = 3),
        ),
    )

    fun observeDevices(): StateFlow<List<SmartDevice>> = devices.asStateFlow()
    fun observeSensor(): StateFlow<SensorReading> = sensor.asStateFlow()
    fun observePowerOutlets(): StateFlow<List<PowerOutlet>> = powerOutlets.asStateFlow()

    suspend fun toggle(deviceId: String): Result<SmartDevice> = command(deviceId) { it.copy(isOn = !it.isOn) }

    suspend fun setBrightness(deviceId: String, brightness: Int): Result<SmartDevice> =
        command(deviceId) { it.copy(brightness = brightness.coerceIn(0, 100), isOn = brightness > 0) }

    suspend fun setCurtainPosition(deviceId: String, position: Int): Result<SmartDevice> =
        command(deviceId) { it.copy(position = position.coerceIn(0, 100)) }

    suspend fun togglePowerOutlet(outletId: String): Result<PowerOutlet> {
        val previous = powerOutlets.value.firstOrNull { it.id == outletId }
            ?: return Result.failure(IllegalArgumentException("Unknown power outlet"))
        if (!previous.isOnline) return Result.failure(IllegalStateException("Power outlet is offline"))
        powerOutlets.value = powerOutlets.value.map {
            if (it.id == outletId) it.copy(isPending = true) else it
        }
        delay(350)
        val updated = previous.copy(isOn = !previous.isOn, isPending = false)
        powerOutlets.value = powerOutlets.value.map { if (it.id == outletId) updated else it }
        return Result.success(updated)
    }

    fun updatePowerOutletName(outletId: String, name: String) {
        powerOutlets.value = powerOutlets.value.map {
            if (it.id == outletId) it.copy(connectedDeviceName = name) else it
        }
    }

    suspend fun setAllLights(enabled: Boolean): Result<Unit> {
        devices.value = devices.value.map {
            if (it.type == DeviceType.LIGHT) it.copy(isPending = true) else it
        }
        delay(450)
        devices.value = devices.value.map {
            if (it.type == DeviceType.LIGHT) it.copy(isOn = enabled, isPending = false) else it
        }
        return Result.success(Unit)
    }

    private suspend fun command(deviceId: String, transform: (SmartDevice) -> SmartDevice): Result<SmartDevice> {
        val previous = devices.value.firstOrNull { it.id == deviceId }
            ?: return Result.failure(IllegalArgumentException("Unknown device"))
        if (!previous.isOnline) return Result.failure(IllegalStateException("Device is offline"))
        devices.value = devices.value.map { if (it.id == deviceId) it.copy(isPending = true) else it }
        delay(350)
        val updated = transform(previous).copy(isPending = false)
        devices.value = devices.value.map { if (it.id == deviceId) updated else it }
        return Result.success(updated)
    }
}

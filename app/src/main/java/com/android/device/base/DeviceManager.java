package com.android.device.base;

import android.content.Context;

import com.android.deviceinfo.cellinfo.DeviceCellinfoManager;
import com.android.deviceinfo.property.DevicePropertyManager;
import com.android.deviceinfo.sensor.DeviceSensorManager;

public class DeviceManager {
    private static String TAG = "DeviceManager";
    private static DeviceManager sDeviceManager = null;
    private DevicePropertyManager mDeviceProperty = null;
    private DeviceSensorManager mDeviceSensor = null;
    private DeviceCellinfoManager mDeviceCellinfo = null;

    public static synchronized DeviceManager getInstance() {
        if (sDeviceManager == null) {
            sDeviceManager = new DeviceManager();
        }
        return sDeviceManager;
    }

    public void initDevice(Context context) {
        mDeviceProperty = new DevicePropertyManager(context);
        mDeviceSensor = new DeviceSensorManager(context);
        mDeviceCellinfo = new DeviceCellinfoManager(context);
    }

    public DevicePropertyManager getDeviceProperty() {
        return mDeviceProperty;
    }

    public DeviceCellinfoManager getDeviceCellinfo() {
        return mDeviceCellinfo;
    }

    public DeviceSensorManager getDeviceSensor() {
        return mDeviceSensor;
    }
}

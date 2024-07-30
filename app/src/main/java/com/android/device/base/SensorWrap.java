package com.android.device.base;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.android.deviceinfo.sensor.DeviceSensorManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorWrap {
    private static SensorWrap sSensorWrap = null;
    private Map<Sensor, SensorAssist> mSensorAssistMap;
    private DeviceSensorManager mSensorManager;
    private List<Sensor> mSensorList;

    public static SensorWrap getInstance() {
        if (sSensorWrap == null) {
            sSensorWrap = new SensorWrap();
        }
        return sSensorWrap;
    }

    private SensorWrap() {
        mSensorAssistMap = new HashMap<>();
        mSensorManager = null;
        mSensorList = null;
    }

    public void init(Context context) {
        mSensorManager = new DeviceSensorManager(context);
        mSensorList = mSensorManager.getSensorList();
        for (Sensor sensor : mSensorList) {
            SensorAssist sensorAssist = new SensorAssist(sensor, mSensorManager);
            mSensorAssistMap.put(sensor, sensorAssist);
            if (sensor.getStringType() != "android.sensor.device_orientation") {
                sensorAssist.enable();
            }
        }
    }

    public void deinit() {
        for (Sensor key : mSensorAssistMap.keySet()) {
            SensorAssist sensorAssist = mSensorAssistMap.get(key);
            if (sensorAssist.isEnable()) {
                sensorAssist.disable();
            }
        }
        mSensorAssistMap.clear();
        mSensorManager = null;
        mSensorList = null;
    }

    public SensorAssist getSensorAssist(int position) {
        Sensor senosr = mSensorList.get(position);
        return mSensorAssistMap.get(senosr);
    }

    public DeviceSensorManager getDeviceManager() {
        return mSensorManager;
    }
	
    public List<Sensor> getSensorList() {
        return mSensorList;
    }

    public class SensorAssist {
        private Sensor mSensor;
        private DeviceSensorManager mDeviceSensor;
        private boolean mIsEnable;
        private float[] mValues;
        private SensorEventListener mListener;

        public SensorAssist(Sensor sensor, DeviceSensorManager sensorManager) {
            mSensor = sensor;
            mDeviceSensor = sensorManager;
            mValues = null;
            mIsEnable = false;
            mListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    synchronized (this) {
                        mValues = Arrays.copyOf(event.values, event.values.length);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }

        public void enable() {
            mIsEnable = true;
            mDeviceSensor.register(mSensor, mListener);
        }

        public void disable() {
            mIsEnable = false;
            mDeviceSensor.unregister(mSensor, mListener);
        }

        public boolean isEnable() {
            return mIsEnable;
        }

        public float[] getValues() {
            synchronized (this) {
                return mValues;
            }
        }
    }
}

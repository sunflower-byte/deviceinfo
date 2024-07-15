package com.android.device.base;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.android.deviceinfo.sensor.DeviceSensorManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorWrap {
    private Map<Sensor, SensorHealper> mSensorHelperMap;
    private DeviceSensorManager mDeviceSensor;
    private List<Sensor> mSensorList;

    public SensorWrap() {
        mSensorHelperMap = new HashMap<>();
        mDeviceSensor = DeviceManager.getInstance().getDeviceSensor();
        mSensorList = mDeviceSensor.getSensorList();
        for (Sensor sensor : mSensorList) {
            mSensorHelperMap.put(sensor, new SensorHealper(sensor));
        }
    }

    public SensorHealper getSensorHelper(int position) {
        Sensor senosr = mSensorList.get(position);
        return mSensorHelperMap.get(senosr);
    }

    public List<Sensor> getSensorList() {
        return mSensorList;
    }

    public class SensorHealper {
        private Sensor mSensor;
        private DeviceSensorManager mDeviceSensor;
        private boolean mIsEnable;
        private float[] mValues;
        private SensorEventListener mListener;

        public SensorHealper(Sensor sensor) {
            mSensor = sensor;
            mDeviceSensor = DeviceManager.getInstance().getDeviceSensor();
            mValues = null;
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

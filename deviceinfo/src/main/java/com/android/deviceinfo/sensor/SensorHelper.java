package com.android.deviceinfo.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SensorHelper implements SensorEventListener {
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private float[] mValues;
    private List<float[]> mValuesList;
    private boolean mReceiveEvent;

    public SensorHelper(SensorManager sensorManager, Sensor sensor) {
        mSensor = sensor;
        mSensorManager = sensorManager;
        mValuesList = new ArrayList<>();
        mValues = null;
        mReceiveEvent = false;
    }

    public void enable() {
        // 对于oneshot和特殊情况触发的传感器，数据采集的意义不大
        if (mSensor.getReportingMode() == Sensor.REPORTING_MODE_ONE_SHOT
            || mSensor.getReportingMode() == Sensor.REPORTING_MODE_SPECIAL_TRIGGER) {
            return;
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        mReceiveEvent = false;
    }

    public void disable() {
        mSensorManager.unregisterListener(this, mSensor);
    }

    public void refreshList() {
        synchronized (this) {
            mValuesList.add(mValues);
        }
    }

    public void collectData(SensorDataWriter writer) {
        if (mReceiveEvent) {
            writer.write(mSensor, mValuesList);
        }
        mValues = null;
        mValuesList = null;
        mReceiveEvent = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            mValues = Arrays.copyOf(event.values, event.values.length);
        }
        mReceiveEvent = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

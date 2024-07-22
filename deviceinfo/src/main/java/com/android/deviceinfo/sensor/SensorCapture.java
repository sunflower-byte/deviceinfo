package com.android.deviceinfo.sensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class SensorCapture {
    private List<SensorHelper> mSensorHelperList;

    public SensorCapture() {
        mSensorHelperList = null;
    }

    public void startCapture(List<Sensor> sensors, SensorManager sensorManager) {
        mSensorHelperList = new ArrayList<>();
        for (Sensor senor : sensors) {
            SensorHelper sensorHelper = new SensorHelper(sensorManager, senor);
            sensorHelper.enable();
            mSensorHelperList.add(sensorHelper);
        }
    }

    public void stopCaputre() {
        for (SensorHelper sensorHelper : mSensorHelperList) {
            sensorHelper.disable();
        }
    }

    public void cloectData(SensorDataWriter writer) {
        for (SensorHelper sensorHelper : mSensorHelperList) {
            sensorHelper.collectData(writer);
        }
        mSensorHelperList.clear();
    }

    public void refresh() {
        for (SensorHelper sensorHelper : mSensorHelperList) {
            sensorHelper.refreshList();
        }
    }
}

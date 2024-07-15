package com.android.deviceinfo.sensor;

import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorEventAssemble {
    private Object mDataLock = new Object();
    private List<Sensor> mCaptureSensorList;
    private HashMap<Integer, SensorEventData> mEventDataMap;
    private HashMap<Integer, ArrayList<SensorEventData>> mEventListMap;

    public SensorEventAssemble(List<Sensor> captureSensorList) {
        mEventDataMap = new HashMap<>();
        mEventListMap = new HashMap<>();
        mCaptureSensorList = captureSensorList;
        for (Sensor sensor : mCaptureSensorList) {
            mEventListMap.put(sensor.getType(), new ArrayList<>());
        }
    }

    public void addEvent(int type, SensorEventData data) {
        synchronized (mDataLock) {
            mEventDataMap.put(type, data);
        }
    }

    public void refresh() {
        synchronized (mDataLock) {
            for (Sensor sensor : mCaptureSensorList) {
                int type = sensor.getType();
                if (mEventListMap.get(type) == null) {
                    continue;
                }
                if (mEventDataMap.containsKey(type)) {
                    mEventListMap.get(type).add(mEventDataMap.get(type));
                } else {
                    mEventListMap.get(type).add(null);
                }
            }
            mEventDataMap.clear();
        }
    }

    public HashMap<Integer, ArrayList<SensorEventData>> getEventAssemble() {
        return mEventListMap;
    }
}

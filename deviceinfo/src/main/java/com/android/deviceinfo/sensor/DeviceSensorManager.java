package com.android.deviceinfo.sensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceSensorManager {
    private static String TAG = "DeviceSensor";
    private Context mContext = null;
    private SensorManager mSensorManager;
    private List<Sensor> mSensorList;
    private List<Sensor> mCaptureList;
    private Set<Sensor> mRegisterList;
    private int mInterval = 0;
    private String mPath = null;
    private SensorTimer mSensorTimer;
    private SensorTimerListener mSensorTimerListener;
    private SensorEventAssemble mEventAssemble = null;
    private Object mLock = new Object();
    private SensorCaptureListener mCaptureListenser;

    public DeviceSensorManager(@NonNull Context context) {
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        mRegisterList = new HashSet<>();
        mSensorTimer = new SensorTimer();
        mSensorTimerListener = new SensorTimerListener();
        mCaptureListenser = null;
        mCaptureList = null;
    }

    public List<Sensor> getSensorList() {
        return mSensorList;
    }

    public boolean register(Sensor sensor, SensorEventListener listener) {
        if (isAlreadyRegister(sensor)) {
            return false;
        }
        mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mRegisterList.add(sensor);
        return true;
    }

    public boolean unregister(Sensor sensor, SensorEventListener listener) {
        if (!isAlreadyRegister(sensor)) {
            return false;
        }
        mSensorManager.unregisterListener(listener, sensor);
        mRegisterList.remove(sensor);
        return true;
    }

    public void startCapture(int period, int duration, String path, SensorCaptureListener listener) {
        mPath = path;
        mInterval = period;
        mCaptureListenser = listener;

        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (mLock) {
            mCaptureList = new ArrayList<>(mRegisterList);
            for (Sensor sensor : mCaptureList) {
                mSensorManager.registerListener(mEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            mEventAssemble = new SensorEventAssemble(mCaptureList);
        }
        mSensorTimer.start(mSensorTimerListener, period, duration);
    }

    public void stopCapture() {
        for (Sensor sensor : mCaptureList) {
            mSensorManager.unregisterListener(mEventListener, sensor);
        }
        mSensorTimer.forcestop();
        mCaptureList.clear();
    }

    private boolean isAlreadyRegister(Sensor sensor) {
        return  mRegisterList.contains(sensor);
    }

    private SensorEventListener mEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (mLock) {
                if (mEventAssemble != null) {
                    SensorEventData eventData = new SensorEventData();
                    eventData.values = Arrays.copyOf(sensorEvent.values, sensorEvent.values.length);
                    mEventAssemble.addEvent(sensorEvent.sensor.getType(), eventData);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private class SensorTimerListener implements SensorTimer.SensorTimerListener {
        @Override
        public void onTimer() {
            if (mEventAssemble != null) {
                mEventAssemble.refresh();
            }
        }

        @Override
        public void onTimerFinish() {
            stopCapture();
            if (mPath != null) {
                DatabaseHelper dbHelper = new DatabaseHelper(mContext, mPath);
                SQLiteDatabase db = dbHelper.getDatabase();
                SensorListWriter sensorListWriter = new SensorListWriter();
                sensorListWriter.write(mSensorList, db, mInterval);
                SensorDataWriter sensorDataWriter = new SensorDataWriter();
                sensorDataWriter.write(mSensorList, db, mEventAssemble);
            }
            if (mCaptureListenser != null) {
                mCaptureListenser.onFinishCapture();
            }
            synchronized (mLock) {
                mEventAssemble = null;
            }
        }
    }
}

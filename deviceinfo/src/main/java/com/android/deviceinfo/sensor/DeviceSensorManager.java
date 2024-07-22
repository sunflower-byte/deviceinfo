package com.android.deviceinfo.sensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private SensorCaptureListener mCaptureListenser;
    private SensorCapture mSensorCapturer;

    public DeviceSensorManager(@NonNull Context context) {
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        mRegisterList = new HashSet<>();
        mSensorTimer = new SensorTimer();
        mSensorTimerListener = new SensorTimerListener();
        mCaptureListenser = null;
        mCaptureList = null;
        mSensorCapturer = new SensorCapture();
    }

    public List<Sensor> getSensorList() {
        return mSensorList;
    }

    public boolean register(Sensor sensor, SensorEventListener listener) {
        if (isAlreadyRegister(sensor)) {
            return false;
        }
        Log.i(TAG, "register sensor " + sensor.getStringType());
        mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mRegisterList.add(sensor);
        return true;
    }

    public boolean unregister(Sensor sensor, SensorEventListener listener) {
        if (!isAlreadyRegister(sensor)) {
            return false;
        }
        Log.i(TAG, "unregister sensor " + sensor.getStringType());
        mSensorManager.unregisterListener(listener, sensor);
        mRegisterList.remove(sensor);
        return true;
    }

    public void startCapture(int period, int duration, String path, SensorCaptureListener listener) {
        mPath = path;
        mInterval = period;
        mCaptureListenser = listener;

        if (mPath != null) {
            try {
                Files.deleteIfExists(Paths.get(mPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mCaptureList = new ArrayList<>(mRegisterList);
        mSensorCapturer.startCapture(mCaptureList, mSensorManager);
        mSensorTimer.start(mSensorTimerListener, period, duration);
    }

    public void stopCapture() {
        mSensorCapturer.stopCaputre();
        mSensorTimer.forcestop();
        mCaptureList.clear();
    }

    private boolean isAlreadyRegister(Sensor sensor) {
        return  mRegisterList.contains(sensor);
    }


    private class SensorTimerListener implements SensorTimer.SensorTimerListener {
        @Override
        public void onTimer() {
            mSensorCapturer.refresh();;
        }

        @Override
        public void onTimerFinish() {
            stopCapture();
            if (mPath != null) {
                DatabaseHelper dbHelper = new DatabaseHelper(mContext, mPath);
                SQLiteDatabase db = dbHelper.getDatabase();
                SensorListWriter sensorListWriter = new SensorListWriter();
                sensorListWriter.write(mSensorList, db, mInterval);
                SensorDataWriter sensorDataWriter = new SensorDataWriter(db);
                mSensorCapturer.cloectData(sensorDataWriter);
                db.close();
            }
            if (mCaptureListenser != null) {
                mCaptureListenser.onFinishCapture();
            }
        }
    }
}

package com.android.deviceinfo.sensor;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SensorTimer {
    private static String TAG = "SensorTimer";
    private Timer mTimer = null;
    private long mRunTime;
    private long mDuration;
    private long mPeriod;
    private SensorTimerListener mListener;

    public void start(SensorTimerListener listener, long period, long duration) {
        Log.i(TAG, "start");
        mTimer = new Timer();
        mTimer.schedule(new SensorTimerTask(), period, period);
        mRunTime = 0;
        mPeriod = period;
        mDuration = duration * 1000;
        mListener = listener;
    }

    public void forcestop() {
        Log.i(TAG, "forcestop");
        mDuration = 0;
    }

    private class SensorTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mRunTime > mDuration) {
                mListener.onTimerFinish();
                mTimer.cancel();
                Log.i(TAG, "onTimerFinish");
            } else {
                mRunTime += mPeriod;
                mListener.onTimer();
            }
        }
    }

    public interface SensorTimerListener {
        void onTimer();
        void onTimerFinish();
    }
}

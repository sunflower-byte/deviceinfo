package com.android.device.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.device.R;
import com.android.device.base.DeviceManager;
import com.android.device.base.FilePath;
import com.android.deviceinfo.cellinfo.DeviceCellinfoManager;
import com.android.deviceinfo.cpu.DeviceCpu;
import com.android.deviceinfo.property.DevicePropertyManager;
import com.android.deviceinfo.sensor.DeviceSensorManager;
import com.android.deviceinfo.sensor.SensorCaptureListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OverviewFragment extends BaseFragment {
    private static final String TAG = "OverviewFragment";
    private static final int CAPTURE_PROPERTY_SUCCESS = 0;
    private static final int CAPTURE_CELLINFO_SUCCESS = 1;
    private static final int CAPTURE_SENSOR_SUCCESS = 2;
    private static final int CAPTURE_CPUINFO_SUCCESS = 3;

    private final String mName = "概览";
    private ListView mCaptureStateListView = null;
    private BaseAdapter mAdapter = null;
    private List<CaptureInfo> mCaptreuInfos = null;
    private Button mStartCaptureBtn = null;
    private Button mStopCaptureBtn = null;
    private boolean mCaptureFlag = false;
    private EditText mCaptureFrequency = null;
    private EditText mCaptureDuration = null;

    public OverviewFragment() {
        initCaptureRecord();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        TextView brand = view.findViewById(R.id.brand);
        brand.setText(Build.BRAND);
        TextView model = view.findViewById(R.id.model);
        model.setText(Build.MODEL);
        TextView aospVersion = view.findViewById(R.id.aosp_version);
        aospVersion.setText(Build.VERSION.RELEASE);

        mCaptureFrequency = view.findViewById(R.id.sensor_capture_frequency);
        mCaptureDuration = view.findViewById(R.id.sensor_capture_duration);
        mCaptureStateListView = view.findViewById(R.id.capture_result_listView);
        mStartCaptureBtn = view.findViewById(R.id.start_capture);
        mStopCaptureBtn = view.findViewById(R.id.stop_capture);

        mStartCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCpautre();
            }
        });
        mStopCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCapture();
            }
        });
        updateCaptureState();
    }

    @Override
    protected void deinitView() {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_overview;
    }

    @Override
    public String getName() {
        return mName;
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    private void initCaptureRecord() {
        mCaptreuInfos = new ArrayList<>();
        mCaptreuInfos.add(new CaptureInfo("传感器", FilePath.getSensorPath()));
        mCaptreuInfos.add(new CaptureInfo("基站信息", FilePath.getCellinfoPath()));
        mCaptreuInfos.add(new CaptureInfo("属性", FilePath.getPropertyPath()));
        mCaptreuInfos.add(new CaptureInfo("cpuifo", FilePath.getCpuinfoPath()));
    }

    private void startCpautre() {
        int sensorFrequency = Integer.valueOf(mCaptureFrequency.getText().toString());
        int sensorCaptureDuration = Integer.valueOf(mCaptureDuration.getText().toString());
        if (sensorFrequency < 20 || sensorFrequency > 200 || sensorCaptureDuration > 1200) {
            Toast.makeText(getActivity(), "频率20-200毫秒,时长小于20分钟", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCaptureFlag) {
            Toast.makeText(getContext(), "正在采集", Toast.LENGTH_SHORT).show();
            return;
        }
        mCaptureFlag = true;
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    DevicePropertyManager deviceProperty = DeviceManager.getInstance().getDeviceProperty();
                    deviceProperty.exportProperty(FilePath.getPropertyPath());
                    subscriber.onNext(CAPTURE_PROPERTY_SUCCESS);
                    DeviceCellinfoManager deviceCellinfo = DeviceManager.getInstance().getDeviceCellinfo();
                    deviceCellinfo.exportCellinfo(FilePath.getCellinfoPath());
                    subscriber.onNext(CAPTURE_CELLINFO_SUCCESS);
                    DeviceCpu deviceCpu = new DeviceCpu();
                    deviceCpu.exportCpuinfo(FilePath.getCpuinfoPath());
                    subscriber.onNext(CAPTURE_CPUINFO_SUCCESS);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception("capture failed."));
                    return;
                }
                DeviceSensorManager deviceSensor = DeviceManager.getInstance().getDeviceSensor();
                SensorCaptureListener listener = new SensorCaptureListener() {
                    @Override
                    public void onFinishCapture() {
                        subscriber.onNext(CAPTURE_SENSOR_SUCCESS);
                        subscriber.onCompleted();
                    }
                };
                deviceSensor.startCapture(sensorFrequency, sensorCaptureDuration,
                        FilePath.getSensorPath(), listener);
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {

            @Override
            public void onCompleted() {
                Toast.makeText(getContext(), "采集结束", Toast.LENGTH_SHORT).show();
                mCaptureFlag = false;
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "采集失败，请检查权限", Toast.LENGTH_SHORT).show();
                mCaptureFlag = false;
            }

            @Override
            public void onNext(Integer integer) {
                mAdapter.notifyDataSetChanged();
                if (integer == CAPTURE_PROPERTY_SUCCESS) {
                    Toast.makeText(getContext(), "属性采集完成", Toast.LENGTH_SHORT).show();
                } else if (integer == CAPTURE_CELLINFO_SUCCESS) {
                    Toast.makeText(getContext(), "cellinfo采集完成", Toast.LENGTH_SHORT).show();
                } else if (integer == CAPTURE_SENSOR_SUCCESS) {
                    Toast.makeText(getContext(), "传感器采集完成", Toast.LENGTH_SHORT).show();
                } else if (integer == CAPTURE_CPUINFO_SUCCESS) {
                    Toast.makeText(getContext(), "cpuinfo采集完成", Toast.LENGTH_SHORT).show();
                }
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void stopCapture() {
        if (mCaptureFlag) {
            DeviceSensorManager deviceSensor = DeviceManager.getInstance().getDeviceSensor();
            deviceSensor.stopCapture();
        }
    }

    private void updateCaptureState() {
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mCaptreuInfos.size();
            }

            @Override
            public Object getItem(int position) {
                return mCaptreuInfos.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CaptureViewHolder holder;
                View currentView;
                if (convertView != null) {
                    currentView = convertView;
                    holder = (CaptureViewHolder) currentView.getTag();
                } else {
                    currentView = LayoutInflater.from(getContext()).inflate(R.layout.capturestate_item, null);
                    holder = new CaptureViewHolder();
                    holder.name = currentView.findViewById(R.id.name);
                    holder.state = currentView.findViewById(R.id.state);
                    currentView.setTag(holder);
                }
                holder.name.setText(mCaptreuInfos.get(position).name);
                if (isFileExist(mCaptreuInfos.get(position).path)) {
                    holder.state.setText("已采集");
                } else {
                    holder.state.setText("未采集");
                }
                return currentView;
            }
        };
        mCaptureStateListView.setAdapter(mAdapter);
    }

    private class CaptureInfo {
        public String name;
        public String path;

        public CaptureInfo(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

    private class CaptureViewHolder {
        public TextView name;
        public TextView state;
    }
}

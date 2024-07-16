package com.android.device.fragment;

import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.device.R;
import com.android.device.base.SensorWrap;

import java.util.Arrays;
import java.util.List;

public class SensorFragment extends BaseFragment {
    private final String TAG = "SensorFragment";
    private static final String mName = "传感器";
    private List<Sensor> mSensorList;
    private ListView mListView = null;
    private MyAdapter mAdapter = null;
    private Handler mHandler = null;
    private Runnable mRunable = null;
    private boolean mStopRefresh = false;
    private SensorWrap mSensorWrap = null;

    public SensorFragment() {
        mSensorWrap = new SensorWrap();
        mSensorList = mSensorWrap.getSensorList();
        for (int i = 0; i < mSensorList.size(); i++) {
            if (mSensorList.get(i).getStringType() == "android.sensor.device_orientation") {
                continue;
            }
            mSensorWrap.getSensorHelper(i).enable();
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mListView = view.findViewById(R.id.sensor_list_view);
        setupListView();
        mStopRefresh = false;
        mHandler = new Handler();
        mRunable = new Runnable() {
            @Override
            public void run() {
                if (!mStopRefresh) {
                    mHandler.postDelayed(this, 500);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        mHandler.postDelayed(mRunable, 500);
    }

    @Override
    protected void deinitView() {
        mStopRefresh = true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sensor;
    }

    @Override
    public String getName() {
        return mName;
    }


    private void setupListView() {
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SensorWrap.SensorHealper sensorHealper = mSensorWrap.getSensorHelper(position);
                if (sensorHealper.isEnable()) {
                    sensorHealper.disable();
                } else {
                    sensorHealper.enable();
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSensorList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.sensor_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SensorWrap.SensorHealper sensorHealper = mSensorWrap.getSensorHelper(position);
            String string = Arrays.toString(sensorHealper.getValues());
            holder.sensor.setText(mSensorList.get(position).getName());
            if (sensorHealper.isEnable()) {
                holder.checkbox.setChecked(true);
                holder.sensorData.setText(string);
            } else {
                holder.checkbox.setChecked(false);
                holder.sensorData.setText("null");
            }
            return convertView;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View itemView;
            CheckBox checkbox;
            TextView sensor;
            TextView sensorData;
            public ViewHolder(@NonNull View item) {
                super(item);
                itemView = item;
                checkbox = itemView.findViewById(R.id.sensor_checkbox);
                sensor = itemView.findViewById(R.id.sensor_name);
                sensorData = itemView.findViewById(R.id.sensor_data);
            }
        }
    }
}

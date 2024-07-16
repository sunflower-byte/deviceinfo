package com.android.device.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.device.R;
import com.android.deviceinfo.cpu.DeviceCpu;

public class CpuFragment extends BaseFragment {
    private final String mName = "CPU";

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        DeviceCpu deviceCpu = new DeviceCpu();
        String cpuinfo = deviceCpu.getCpuInfo();
        TextView textView = view.findViewById(R.id.cpuinfo);
        textView.setText(cpuinfo);
    }

    @Override
    protected void deinitView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_cpu;
    }

    @Override
    public String getName() {
        return mName;
    }
}

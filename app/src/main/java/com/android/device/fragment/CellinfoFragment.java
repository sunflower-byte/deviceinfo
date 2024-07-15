package com.android.device.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.device.R;
import com.android.device.base.DeviceManager;
import com.android.deviceinfo.cellinfo.DeviceCellinfoManager;

import java.util.ArrayList;
import java.util.Map;

public class CellinfoFragment extends BaseFragment {
    private final String mName = "基站信息";
    private ListView mListView = null;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mListView = view.findViewById(R.id.lv_sim);
        setupListview();
    }

    @Override
    protected void deinitView() {
        mListView = null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_cellinfo;
    }

    @Override
    public String getName() {
        return mName;
    }

    private void setupListview() {
        ArrayList<String> arrayList = new ArrayList<>();
        DeviceCellinfoManager cellinfoManager = DeviceManager.getInstance().getDeviceCellinfo();
        Map<String, String> cellinfo = cellinfoManager.getCellInfo();
        for (Map.Entry<String, String> entry : cellinfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            arrayList.add(new String(key + "=" + value));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.cellinfo_item,
                R.id.cellinfo,
                arrayList);
        mListView.setAdapter(adapter);
    }
}

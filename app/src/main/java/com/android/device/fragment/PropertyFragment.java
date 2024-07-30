package com.android.device.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.device.R;
import com.android.deviceinfo.property.DevicePropertyManager;

import java.util.ArrayList;
import java.util.Map;

public class PropertyFragment extends BaseFragment {
    private final String mName = "属性";
    private ListView mListView = null;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mListView = view.findViewById(R.id.lv_property);
        setupListview();
    }

    @Override
    protected void deinitView() {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_property;
    }

    @Override
    public String getName() {
        return mName;
    }

    private void setupListview() {
        ArrayList<String> arrayList = new ArrayList<>();
        DevicePropertyManager propertyManager = new DevicePropertyManager(getContext());
        Map<String, String> properties = propertyManager.getProperty();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            arrayList.add(new String(key + "=" + value));
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.property_item, R.id.property, arrayList);
        mListView.setAdapter(adapter);
    }
}

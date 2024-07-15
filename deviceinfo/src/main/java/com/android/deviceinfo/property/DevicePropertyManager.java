package com.android.deviceinfo.property;

import android.content.Context;

import com.android.deviceinfo.common.Utils;

import java.io.IOException;
import java.util.Map;

public class DevicePropertyManager {
    private Context mContext = null;
    private DeviceProperty mDeviceProperty = new DeviceProperty();

    public DevicePropertyManager(Context context) {
        mContext = context;
    }

    public Map<String, String> getProperty() {
        return mDeviceProperty.getProperty(mContext);
    }

    public void exportProperty(String path) throws IOException {
        Map<String, String> properties = getProperty();
        Utils.writeMapToFile(properties, path);
    }
}

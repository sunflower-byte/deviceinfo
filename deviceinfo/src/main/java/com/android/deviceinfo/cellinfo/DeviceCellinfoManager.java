package com.android.deviceinfo.cellinfo;

import android.content.Context;

import com.android.deviceinfo.common.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class DeviceCellinfoManager {
    private Context mContext = null;
    private DeviceCellinfo mCellInfo = new DeviceCellinfo();

    public DeviceCellinfoManager(Context context) {
        mContext = context;
    }

    public Map<String, String> getCellInfo() {
        return mCellInfo.getCellinfo(mContext);
    }

    public void exportCellinfo(String path) throws IOException {
        Map<String, String> cellinfo = getCellInfo();
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileWriter writer = new FileWriter(path, false);
        Iterator<Map.Entry<String, String>> iterator = cellinfo.entrySet().iterator();
        StringBuilder content = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            content.append(key)
                   .append("=")
                   .append(value)
                   .append(",");
        }
        content.append("\n");
        writer.write(content.toString());
        writer.close();
    }
}

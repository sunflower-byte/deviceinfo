package com.android.deviceinfo.cellinfo;

import android.content.Context;

import com.android.deviceinfo.common.Utils;

import java.io.IOException;
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
        Utils.writeMapToFile(cellinfo, path);
    }
}

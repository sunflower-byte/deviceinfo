package com.android.device.base;

import android.os.Environment;

public class FilePath {
    public static String getStoragePath() {
        String dir = "phone";
        String externalPath = Environment.getExternalStorageDirectory().getPath();
        return externalPath + "/" + dir;
    }

    public static String getSensorPath() {
        String file = "sensor.db";
        return getStoragePath() + "/" + file;
    }

    public static String getPropertyPath() {
        String file = "phone.prop";
        return getStoragePath() + "/" + file;
    }

    public static String getCellinfoPath() {
        String file = "cellinfo";
        return getStoragePath() + "/" + file;
    }
}

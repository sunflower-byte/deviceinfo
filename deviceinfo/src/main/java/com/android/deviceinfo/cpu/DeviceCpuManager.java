package com.android.deviceinfo.cpu;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DeviceCpuManager {
    private static final String TAG = "DeviceCpu";

    public String getCpuInfo() {
        String cpuinfo = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream in = process.getInputStream();
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = in.read()) != -1) {
                sb.append((char)ch);
            }
            cpuinfo = sb.toString();
            Log.i(TAG, "cpuinfo = " + cpuinfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuinfo;
    }

    public void exportCpuinfo(String path) throws IOException {
        Path sourcePath = Paths.get("/proc/cpuinfo");
        Path destinationPath = Paths.get(path);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}

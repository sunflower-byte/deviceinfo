package com.android.deviceinfo;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeviceTarCompressor {
    public static void compressDirectoryToTar(String source, String target) throws IOException {
        File files = new File(source);
        FileOutputStream fos = new FileOutputStream(target);
        TarArchiveOutputStream tar = new TarArchiveOutputStream(fos);
        addFileToTar(files, tar, "");
    }

    private static void addFileToTar(File file, TarArchiveOutputStream tar, String dir) throws IOException {
        String entryName = dir + file.getName();
        ArchiveEntry entry = tar.createArchiveEntry(file, entryName);
        tar.putArchiveEntry(entry);
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            IOUtils.copy(fis, tar);
            tar.closeArchiveEntry();
        } else if (file.isDirectory()) {
            tar.closeArchiveEntry();
            for (File subFile : file.listFiles()) {
                addFileToTar(subFile, tar, entryName + "/");
            }
        }
    }
}

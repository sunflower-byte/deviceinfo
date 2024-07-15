package com.android.deviceinfo.property;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class DeviceProperty {
    private static String TAG = "DeviceProperty";
    private String[] mKeyArray = {
            "ro.product.board",
            "ro.product.brand",
            "ro.product.product.brand",
            "ro.product.system.brand",
            "ro.product.system_ext.brand",
            "ro.product.odm.brand",
            "ro.product.vendor.brand",
            "ro.build.id",
            "ro.product.build.id",
            "ro.system_ext.build.id",
            "ro.system.build.id",
            "ro.vendor.build.id",
            "ro.system.build.type",
            "ro.build.type",
            "ro.vendor.build.type",
            "ro.system_ext.build.type",
            "ro.product.build.type",
            "ro.build.tags",
            "ro.system.build.tags",
            "ro.product.build.tags",
            "ro.vendor.build.tags",
            "ro.system_ext.build.tags",
            "ro.product.device",
            "ro.product.product.device",
            "ro.product.system_ext.device",
            "ro.product.odm.device",
            "ro.product.vendor.device",
            "ro.product.system.device",
            "device.name",
            "ro.build.display.id",
            "ro.build.fingerprint",
            "ro.product.build.fingerprint",
            "ro.system.build.fingerprint",
            "ro.system_ext.build.fingerprint",
            "ro.vendor.build.fingerprint",
            "ro.odm.build.fingerprint",
            "ro.bootimage.build.fingerprint",
            "ro.hardware",
            "ro.boot.hardware",
            "ro.build.version.incremental",
            "ro.system.build.version.incremental",
            "ro.odm.build.version.incremental",
            "ro.product.build.version.incremental",
            "ro.vendor.build.version.incremental",
            "ro.system_ext.build.version.incremental",
            "ro.product.manufacturer",
            "ro.product.system_ext.manufacturer",
            "ro.product.vendor.manufacturer",
            "ro.product.product.manufacturer",
            "ro.product.odm.manufacturer",
            "ro.product.system.manufacturer",
            "ro.product.model",
            "ro.product.system.model",
            "ro.product.system_ext.model",
            "ro.product.product.model",
            "ro.product.odm.model",
            "ro.product.vendor.model",
            "ro.product.name",
            "ro.product.odm.name",
            "ro.product.vendor.name",
            "ro.product.system_ext.name",
            "ro.product.product.name",
            "ro.product.system.name",
            "ro.board.platform",
            "ro.baseband",
            "ro.boot.baseband",
            "ro.bootimage.build.date",
            "ro.build.date",
            "ro.product.build.date",
            "ro.odm.build.date",
            "ro.system.build.date",
            "ro.system_ext.build.date",
            "ro.vendor.build.date",
            "ro.bootimage.build.date.utc",
            "ro.system.build.date.utc",
            "ro.system_ext.build.date.utc",
            "ro.odm.build.date.utc",
            "ro.product.build.date.utc",
            "ro.vendor.build.date.utc",
            "ro.build.date.utc",
            "ro.build.version.security_patch",
            "ro.vendor.build.security_patch",
            "ro.build.host",
            "ro.bootloader",
            "ro.boot.bootloader",
            "ro.serialno",
            "gsm.network.type",
            "gsm.version.baseband",
            "gsm.operator.numeric",
            "gsm.operator.alpha",
            "gsm.sim.operator.alpha",
            "gsm.sim.operator.numeric",
            "ro.build.version.release",
            "ro.vendor.build.version.release_or_codename",
            "ro.build.version.release_or_codename",
            "ro.system.build.version.release_or_codename",
            "ro.system.build.version.release",
            "ro.product.build.version.release",
            "ro.vendor.build.version.release",
            "ro.system_ext.build.version.release",
            "ro.product.build.version.release",
            "ro.vendor.build.version.release",
            "ro.system_ext.build.version.release",
            "ro.product.build.version.release_or_codename",
            "ro.system_ext.build.version.release_or_codename",
            "ro.build.version.sdk",
            "ro.system.build.version.sdk",
            "ro.product.build.version.sdk",
            "ro.system_ext.build.version.sdk",
            "gsm.version.ril-impl",
            "ro.build.version.codename",
            "gsm.sim.operator.iso-country",
            "gsm.operator.iso-country",
            "persist.sys.locale",
            "ro.product.locale",
            "ro.build.product",
            "ro.build.description",
            "ro.build.flavor",
            "sys.prop.writephonenum",
            "ro.build.user",
            "ro.boot.vbmeta.digest",
            "ro.build.characteristics",
            "ro.boottime.adbd",
            "persist.sys.usb.config",
            "init.svc.adbd",
            "ro.boot.serialno",
            "ro.revision",
            "ro.hard.gpurenderer",
            "gsm.sim.state",
            "device.name"
    };


    private Map<String, String> getNormalProperties() {
        Map<String, String> properties = new HashMap<>();
        for (int i = 0; i < mKeyArray.length; i ++) {
            String key = mKeyArray[i];
            String value = PropertyUtils.getProperty(key, "empty");
            if (!value.equals("empty")) {
                properties.put(key, value);
                Log.i(TAG, key + " = " + value);
            }
        }
        return properties;
    }

    private String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public Map<String, String> getProperty(Context context) {
        Map<String, String> properties = getNormalProperties();
        properties.put("android.id", getAndroidID(context));
        return properties;
    }










}

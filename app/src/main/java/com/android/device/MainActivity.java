package com.android.device;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.device.base.SensorWrap;
import com.android.device.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ViewPager mMainViewPager = null;
    private ViewPagerAdapter mViewPagerAdapter = null;
    private String[] mPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.READ_PHONE_STAT};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initSensor();
        initViewPager();
        processPermission();
    }

    @Override
    protected void onDestroy() {
        deinitSensor();
        super.onDestroy();
    }

    private void initViewPager() {
        mMainViewPager = findViewById(R.id.main_viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mMainViewPager.setAdapter(mViewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mMainViewPager);
    }

    private void initSensor() {
        SensorWrap.getInstance().init(getApplicationContext());
    }

    private void deinitSensor() {
        SensorWrap.getInstance().deinit();
    }
    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    public void processPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
        if (!EasyPermissions.hasPermissions(this, mPermissions)) {
            EasyPermissions.requestPermissions(this, "读取设备的信息",
                    PERMISSION_REQUEST_CODE, mPermissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.i(TAG, "onPermissionsGranted " + perms.toString());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.i(TAG, "onPermissionsDenied " + perms.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
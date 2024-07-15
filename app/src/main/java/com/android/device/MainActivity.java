package com.android.device;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.device.base.DeviceManager;
import com.android.device.databinding.ActivityMainBinding;
import com.android.device.fragment.BaseFragment;
import com.android.device.fragment.OverviewFragment;
import com.android.device.fragment.PropertyFragment;
import com.android.device.fragment.SensorFragment;
import com.android.device.fragment.CellinfoFragment;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private List<String> mTitles = new ArrayList<String>();
    private List<TextView> mTextViews = new ArrayList<>();
    private ViewPager mMainViewPager = null;
    private ViewPagerAdapter mViewPagerAdapter = null;
    private String[] mPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDevice();
        initViewPager();
        processPermission();
    }

    private void initViewPager() {
        mMainViewPager = findViewById(R.id.main_viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        AddFragment(new OverviewFragment());
        AddFragment(new PropertyFragment());
        AddFragment(new SensorFragment());
        AddFragment(new CellinfoFragment());
        mMainViewPager.setAdapter(mViewPagerAdapter);
        initTitles();
        mMainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (TextView textView : mTextViews) {
                    textView.setTextColor(Color.WHITE);
                }
                mTextViews.get(position).setTextColor(Color.RED);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void AddFragment(BaseFragment fragment) {
        mViewPagerAdapter.addFragment(fragment);
        mTitles.add(fragment.getName());
    }

    private void initTitles() {
        LinearLayout titleLayout = findViewById(R.id.nav_title);
        int index = 0;
        for (String title : mTitles) {
            TextView textView = new TextView(this);
            textView.setText(title);
            textView.setTextSize(16);
            textView.setTextColor(Color.WHITE);
            textView.setId(index++);
            mTextViews.add(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(8, 0, 12, 0);
            layoutParams.gravity = Gravity.BOTTOM;
            titleLayout.addView(textView, layoutParams);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMainViewPager.setCurrentItem(view.getId());
                }
            });
            mTextViews.get(0).setTextColor(Color.RED);
        }
    }

    private void initDevice() {
        DeviceManager.getInstance().initDevice(this);
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
        if (EasyPermissions.hasPermissions(this, mPermissions)) {
        } else {
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
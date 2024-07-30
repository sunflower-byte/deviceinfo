package com.android.device;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.device.fragment.BaseFragment;
import com.android.device.fragment.CellinfoFragment;
import com.android.device.fragment.CpuFragment;
import com.android.device.fragment.OverviewFragment;
import com.android.device.fragment.PropertyFragment;
import com.android.device.fragment.SensorFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragmentList = null;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new OverviewFragment());
        mFragmentList.add(new PropertyFragment());
        mFragmentList.add(new SensorFragment());
        mFragmentList.add(new CellinfoFragment());
        mFragmentList.add(new CpuFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getName();
    }
}

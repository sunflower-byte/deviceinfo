package com.android.device.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void deinitView();

    protected abstract int getLayoutID();

    public abstract String getName();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutID(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    public void onDestroyView() {
        deinitView();
        super.onDestroyView();
    }
}

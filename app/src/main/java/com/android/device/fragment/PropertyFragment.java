package com.android.device.fragment;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.device.R;
import com.android.deviceinfo.property.DevicePropertyManager;

import java.util.ArrayList;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PropertyFragment extends BaseFragment {
    private final String mName = "属性";
    private ListView mListView = null;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mListView = view.findViewById(R.id.lv_property);
        setupGlSurfaceview(view);
        setupListview();
    }

    @Override
    protected void deinitView() {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_property;
    }

    @Override
    public String getName() {
        return mName;
    }

    private void setupListview() {
        ArrayList<String> arrayList = new ArrayList<>();
        DevicePropertyManager propertyManager = new DevicePropertyManager(getContext());
        Map<String, String> properties = propertyManager.getProperty();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            arrayList.add(new String(key + "=" + value));
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.property_item, R.id.property, arrayList);
        mListView.setAdapter(adapter);
    }

    private void setupGlSurfaceview(View view) {
        GLSurfaceView glSurfaceView = view.findViewById(R.id.gl_surfaceview);
        glSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                DevicePropertyManager.setGlRender(gl.glGetString(GL10.GL_RENDERER));
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        });
    }
}

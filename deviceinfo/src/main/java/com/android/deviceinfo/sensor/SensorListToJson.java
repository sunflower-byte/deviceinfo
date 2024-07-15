package com.android.deviceinfo.sensor;

import android.hardware.Sensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SensorListToJson {
    public static JSONArray sensorListToJson(List<Sensor> sensorList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Sensor sensor : sensorList) {
            JSONObject object = new JSONObject();
            object.put("id", sensor.getId());
            object.put("name", sensor.getName());
            object.put("vendor", sensor.getVendor());
            object.put("version", sensor.getVersion());
            object.put("stringType", sensor.getStringType());
            object.put("type", sensor.getType());
            object.put("maxRange", sensor.getMaximumRange());
            object.put("resolution", sensor.getResolution());
            object.put("minDelay", sensor.getMinDelay());
            object.put("maxDelay", sensor.getMaxDelay());
            object.put("power", sensor.getPower());
            object.put("reportMode", sensor.getReportingMode());
            object.put("wakeup", sensor.getReportingMode());
            jsonArray.put(object);
        }
        return jsonArray;
    }
}

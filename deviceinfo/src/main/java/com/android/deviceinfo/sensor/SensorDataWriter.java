package com.android.deviceinfo.sensor;

import static android.hardware.Sensor.REPORTING_MODE_CONTINUOUS;

import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorDataWriter {
    public void write(List<Sensor> sensors, SQLiteDatabase database, SensorEventAssemble eventAssemble) {
        HashMap<Integer, ArrayList<SensorEventData>> events = eventAssemble.getEventAssemble();
        for (Integer key : events.keySet()) {
            int reportMode = 0;
            String stringType = null;
            for (Sensor sensor : sensors) {
                if (sensor.getType() == key) {
                    stringType = sensor.getStringType();
                    reportMode = sensor.getReportingMode();
                    break;
                }
            }
            if (stringType != null) {
                Log.i("SensorDataWriter", "write one data");
                writeOneSensor(database, events.get(key), reportMode, stringType);
            }
        }
    }

    private void createSensorDataTable(SQLiteDatabase database, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("drop table if exists " )
                .append("\"")
                .append(tableName)
                .append("\";");
        database.execSQL(sql.toString());
        StringBuilder createTable = new StringBuilder();
        createTable.append("create table if not exists ")
                .append(tableName)
                .append("(")
                .append("_id integer primary key autoincrement not null,")
                .append("data varchar not null")
                .append(");");
        database.execSQL(createTable.toString());
    }

    private void writeOneSensor(SQLiteDatabase database, ArrayList<SensorEventData> eventList,
                                int reportMode, String stringType) {
        String tableName = stringType.replace('.', '_');
        tableName = tableName.replace(' ', '_');
        createSensorDataTable(database, tableName);

        database.beginTransaction();
        if (reportMode == REPORTING_MODE_CONTINUOUS) {
            writeContinueMode(database, tableName, eventList);
        } else {
            writeOtherMode(database, tableName, eventList);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void writeOtherMode(SQLiteDatabase database, String tableName,
                                ArrayList<SensorEventData> eventList) {
        for (SensorEventData event : eventList) {
            String content = "";
            if (event == null) {
                continue;
            }
            for (int i = 0; i < event.values.length; i++) {
                content += Float.toString(event.values[i]) + ",";
            }
            insertSensorData(database, tableName, content);
        }

    }

    private void writeContinueMode(SQLiteDatabase database, String tableName,
                                   ArrayList<SensorEventData> eventList) {
        String fillContent = "";
        for (SensorEventData event : eventList) {
            if (event == null) {
                continue;
            }
            for (int i = 0; i < event.values.length; i++) {
                fillContent += Float.toString(event.values[i]) + ",";
            }
            break;
        }
        if (fillContent.equals("")) {
            return;
        }
        for (int i = 0; i < eventList.size(); i++) {
            SensorEventData event =  eventList.get(i);
            String content = "";
            if (event == null) {
                content = fillContent;
            } else {
                for (int j = 0; j < event.values.length; j++) {
                    content += Float.toString(event.values[j]) + ",";
                }
                fillContent = content;
            }

            insertSensorData(database, tableName, content);
        }
    }

    private void insertSensorData(SQLiteDatabase database, String tableName, String sensorData) {
        String sql = "INSERT INTO " + tableName + " (data)VALUES (" + "\'" + sensorData + "\'" + ");";
        database.execSQL(sql);
    }
}

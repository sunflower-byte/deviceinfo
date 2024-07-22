package com.android.deviceinfo.sensor;

import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.util.Log;

import java.util.List;

public class SensorDataWriter {
    private SQLiteDatabase mDataBase;

    public SensorDataWriter(SQLiteDatabase database) {
        mDataBase = database;
    }

    public void write(Sensor sensor, List<float[]> valuesList) {
        String stringType = sensor.getStringType();
        String tableName = stringType.replace('.', '_');
        tableName = tableName.replace(' ', '_');
        createSensorDataTable(mDataBase, tableName);
        mDataBase.beginTransaction();
        for (float[] values : valuesList) {
            String content = "";
            if (values == null) {
                content = "null";
            } else {
                for (int j = 0; j < values.length; j++) {
                    content += Float.toString(values[j]) + ",";
                }
            }
            insertSensorData(tableName, content);
        }
        mDataBase.setTransactionSuccessful();
        mDataBase.endTransaction();
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

    private void insertSensorData(String tableName, String sensorData) {
        String sql = "INSERT INTO " + tableName + " (data)VALUES (" + "\'" + sensorData + "\'" + ");";
        mDataBase.execSQL(sql);
    }
}

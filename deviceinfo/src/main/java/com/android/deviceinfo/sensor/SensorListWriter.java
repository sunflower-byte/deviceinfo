package com.android.deviceinfo.sensor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;

import java.util.List;

public class SensorListWriter {
    private static final String TABLE_NAME = "sensor_list";

    public void write(List<Sensor> sensorList, SQLiteDatabase database, int interval) throws SQLException {
        createSensorListTable(database);
        for (Sensor sensor : sensorList) {
            insertOneSensor(sensor, interval, database);
        }
    }

    private void createSensorListTable(SQLiteDatabase database) {
        String sql = "drop table if exists sensor_list";
        database.execSQL(sql);

        String createTable = "create table if not exists sensor_list"
                + "("
                + "_id integer primary key autoincrement not null,"
                + "name varchar not null,"
                + "vendor varchar not null,"
                + "version integer not null,"
                + "StringType varchar not null,"
                + "type integer not null,"
                + "MaxRange double not null,"
                + "resolution float not null,"
                + "MinDelay integer not null,"
                + "MaxDelay integer not null,"
                + "power float not null,"
                + "ReportMode integer not null,"
                + "interval integer not null,"
                + "wakeup integer not null,"
                + "enable integer not null);";
        database.execSQL(createTable);
    }

    private void insertOneSensor(Sensor sensor, int interval, SQLiteDatabase database) {
        int enable = 1;
        int wakeup = sensor.isWakeUpSensor() ? 1 : 0;

        int reportMode = 0;
        switch (sensor.getReportingMode()) {
            case Sensor.REPORTING_MODE_CONTINUOUS:
                reportMode = 0x0;
                break;
            case Sensor.REPORTING_MODE_ON_CHANGE:
                reportMode = 0x2;
                break;
            case Sensor.REPORTING_MODE_ONE_SHOT:
                reportMode = 0x4;
                break;
            case Sensor.REPORTING_MODE_SPECIAL_TRIGGER:
            default:
                reportMode = 0x6;
                break;
        }
        String sql = "INSERT INTO sensor_list (name, vendor, version, StringType, type, MaxRange, resolution, " +
                " MinDelay, MaxDelay, power, ReportMode, interval, wakeup, enable) VALUES("
                + "\'" + sensor.getName() + "\'" + ","
                + "\'" + sensor.getVendor() + "\'" + ","
                + sensor.getVersion() + ","
                + "\'" + sensor.getStringType() + "\'" + ","
                + sensor.getType() + ","
                + sensor.getMaximumRange() + ","
                + sensor.getResolution() + ","
                + sensor.getMinDelay() + ","
                + sensor.getMaxDelay() + ","
                + sensor.getPower() + ","
                + reportMode + ","
                + interval + ","
                + wakeup + ","
                + enable
                + ")";
        database.execSQL(sql);
    }
}

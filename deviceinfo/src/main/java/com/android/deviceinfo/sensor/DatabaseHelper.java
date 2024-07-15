package com.android.deviceinfo.sensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context, String path) {
        super(context, path, null, 1);
    }

    public SQLiteDatabase getDatabase() {
        if (!getReadableDatabase().isOpen()) {
            return null;
        }
        return getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "do onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "do onUpgrade");
    }
}

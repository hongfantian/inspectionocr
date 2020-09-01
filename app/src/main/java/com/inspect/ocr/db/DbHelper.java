package com.inspect.ocr.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    static final String TAG = "DbHelper";
    static final String DB_NAME = "db_voicere";
    static final int DB_VERSION = 1;
    static Activity activity;

    public DbHelper(Activity act) {
        super(act.getApplicationContext(), DB_NAME, null, DB_VERSION);
        activity = act;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS images("
                + "image_id INTEGER,"
                + "user_id TEXT,"
                + "hospital_id TEXT,"
                + "patient_id TEXT,"
                + "image_path TEXT,"
                + "thumb_image_path TEXT,"
                + "receipt_date double,"
                + "created_date double"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
    }
}
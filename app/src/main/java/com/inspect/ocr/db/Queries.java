package com.inspect.ocr.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inspect.ocr.models.PatientImage;

import java.util.ArrayList;
import java.util.HashMap;

public class Queries {

    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public Queries(SQLiteDatabase db, DbHelper dbHelper) {
        this.db = db;
        this.dbHelper = dbHelper;
    }

    public void deleteTable(String tableName) {
        db = dbHelper.getWritableDatabase();
        try{
            db.delete(tableName, null, null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertImage(PatientImage entry) {
        db = dbHelper.getWritableDatabase();

        if(getUserById(entry.getImageId()) == null) {
            ContentValues values = new ContentValues();
            values.put("image_id", entry.getImageId());
            values.put("user_id", entry.getUserId());
            values.put("hospital_id", entry.getHospitalId());
            values.put("patient_id", entry.getPatientId());
            values.put( "thumb_image_path", entry.getThumbImagePath() );
            values.put("image_path", entry.getImagePath());
            values.put("receipt_date", entry.getReceiptDate());
            values.put("created_date", entry.getCreatedDate());

            db.insert("images", null, values);
        }
    }

    public PatientImage getUserById(int img_id) {
        PatientImage image = null;
        db = dbHelper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM images WHERE image_id = " + img_id, null);
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                image = formatImage(mCursor);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return image;
    }

    public ArrayList<PatientImage> getImages() {
        ArrayList<PatientImage> list = new ArrayList<PatientImage>();
        db = dbHelper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM images ORDER BY receipt_date DESC", null);
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                PatientImage image = formatImage(mCursor);
                list.add(image);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return list;
    }

    public void deleteImage(int img_id) {
        db = dbHelper.getWritableDatabase();
        db.delete("images", "image_id = " + img_id, null);
    }

    public PatientImage formatImage(Cursor mCursor) {
        HashMap map = new HashMap();
        map.put("image_id", mCursor.getInt(mCursor.getColumnIndex("image_id")));
        map.put("user_id", mCursor.getString(mCursor.getColumnIndex("user_id")));
        map.put("hospital_id", mCursor.getString(mCursor.getColumnIndex("hospital_id")));
        map.put("patient_id", mCursor.getString(mCursor.getColumnIndex("patient_id")));
        map.put("thumb_image_path", mCursor.getString(mCursor.getColumnIndex("thumb_image_path")) );
        map.put("image_path", mCursor.getString(mCursor.getColumnIndex("image_path")));
        map.put("receipt_date", mCursor.getDouble(mCursor.getColumnIndex("receipt_date")));
        map.put("created_date", mCursor.getDouble(mCursor.getColumnIndex("created_date")));

        PatientImage entry = new PatientImage(map);
        return entry;
    }
}

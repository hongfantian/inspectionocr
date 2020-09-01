package com.inspect.ocr.common;

import android.content.SharedPreferences;

import com.inspect.ocr.Global;
import com.inspect.ocr.R;

public class LocalStorageManager {
    public static void setUserLoginStatus(String userid) {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences( String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( Global.USERNAME, userid);
        editor.commit();
    }

    public static String getUserLoginStatus() {
        String syncState = null;
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        syncState = sharedPreferences.getString( Global.USERNAME, null );
        return syncState;
    }

    public static void setPassword(String password ) {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences( String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( Global.PASSWORD, password);
        editor.commit();
    }

    public static String getPassword() {
        String syncState = null;
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        syncState = sharedPreferences.getString( Global.PASSWORD, null );
        return syncState;
    }

    public static void setHospitalName(String hospitalName) {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( "hospital_name", hospitalName );
        editor.commit();
    }

    public static String getHospitalName() {
        String syncState = null;
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        syncState = sharedPreferences.getString("hospital_name", null );
        return syncState;
    }

    public static void setPatientName(String patientName) {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( "patient_name", patientName );
        editor.commit();
    }

    public static String getPatientName() {
        String syncState = null;
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        syncState = sharedPreferences.getString("patient_name", null);
        return syncState;
    }
}

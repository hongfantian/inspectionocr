package com.inspect.ocr.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.inspect.ocr.MainActivity;
import com.inspect.ocr.R;

public class Common {
    public static Common cm = new Common();
    public static DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    //----------------------------------------------Common Objects-----------------------------------------------------------------------------------------

    public static MainActivity myApp;
    public static Activity currentActivity = null;
    //----------------------------------------------Common Functions-----------------------------------------------------------------------------------------

    /**
     *
     * アラートを表示する。
     * */
    public void showAlertDlg(String title, String msg, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(currentActivity);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton(currentActivity.getResources().getString(R.string.OK), positiveListener);
        if (negativeListener != null) alert.setNegativeButton(currentActivity.getResources().getString(R.string.Cancel), negativeListener);
        alert.create();
        alert.show();
    }
    public static Common getInstance(){
        if( cm == null ){
            cm = new Common();
        }
        return cm;
    }

    /**
     *
     * 必要な許可を確認する
     *
     * @param permissionsToRequest 必要な許可のリスト
     *
     * @return ArrayList 可能な許容のリスト
     * */
    public ArrayList checkPermissions(ArrayList<String> permissionsToRequest) {
        ArrayList<String> permissionsRejected = new ArrayList();
        for (String perms : permissionsToRequest) {
            if (!hasPermission(perms)) {
                permissionsRejected.add(perms);
            }
        }
        return permissionsRejected;
    }

    public boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (currentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    public boolean canAskPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return true;
        else return false;
    }

    public String convertStringFromTime(double c_time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) c_time);

        TimeZone tzn = TimeZone.getTimeZone("Asia/Tokyo" );
        dateFormatter.setTimeZone( tzn );
        return dateFormatter.format(calendar.getTime());
    }
    public static String getDateByTimeZone(  Calendar calendar){
        TimeZone tzn = TimeZone.getTimeZone("Asia/Tokyo" );
        dateFormatter.setTimeZone( tzn );
        return dateFormatter.format(calendar.getTime());
    }

    public String stringFromStringArray(ArrayList<String> arr) {
        String str = "";
        int ii = 0;
        for (String item : arr) {
            if (ii == 0) {
                str = item;
            }else {
                str = str + ", " + item;
            }
            ii ++;
        }
        return str;
    }

    public ArrayList convertToArrayListFromString(String str) {
        ArrayList<String> arr = new ArrayList<>();
        if (str == null || str.equals("")) return arr;
        String[] strArr = str.split(", ");
        for (String item : strArr) {
            arr.add(item);
        }
        return arr;
    }
}

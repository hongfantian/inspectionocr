package com.inspect.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.inspect.ocr.common.Common;
import com.inspect.ocr.common.LocalStorageManager;
import com.inspect.ocr.models.PatientImage;
import com.inspect.ocr.utils.DialogManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.inspect.ocr.common.Common.cm;
import static com.inspect.ocr.common.Common.currentActivity;
import static com.inspect.ocr.common.Common.dateFormatter;

public class ActivityPatientInfo extends AppCompatActivity {

    private EditText hospitalIdTxt, patientIdTxt, patientNameTxt;
    private EditText receiptDateTxt;

    private Calendar todayCalendar;
    private ArrayList<String> hospitalNameList = new ArrayList<>();
    private ArrayList<String> patientNameList = new ArrayList<>();
    private int hospitalNameIndex = 0;
    private int patientNameIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        currentActivity = this;

        LocalStorageManager localStorageManager = new LocalStorageManager();

        hospitalIdTxt = findViewById(R.id.hospital_value);
        if (localStorageManager.getHospitalName() != null) {
            hospitalNameList = cm.convertToArrayListFromString(localStorageManager.getHospitalName());
            hospitalIdTxt.setText(hospitalNameList.get(0));
            hospitalNameIndex = 0;
        }
        patientIdTxt = findViewById(R.id.patient_id_value);
        if (localStorageManager.getPatientName() != null) {
            patientNameList = cm.convertToArrayListFromString(localStorageManager.getPatientName());
            patientIdTxt.setText(patientNameList.get(0));
            patientNameIndex = 0;
        }
//        patientNameTxt = findViewById(R.id.patient_name_value);
        receiptDateTxt = findViewById(R.id.receipt_date_value);

        final Date today = new Date();
        receiptDateTxt.setText(dateFormatter.format(today));
        todayCalendar = Calendar.getInstance();
        final DatePickerDialog  datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                todayCalendar.set(year, monthOfYear, dayOfMonth);
                receiptDateTxt.setText(dateFormatter.format(todayCalendar.getTime()));
            }

        }, todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH));

        receiptDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        LinearLayout nextBtn = findViewById(R.id.to_preview_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputState();
            }
        });

        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView hospitalMoreBtn = findViewById(R.id.hospital_name_more);
        hospitalMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hospitalNameList.size() > 0) {
                    DialogManager.showRadioDialog(currentActivity, null,
                            hospitalNameList.toArray(new CharSequence[hospitalNameList.size()]), hospitalNameIndex, null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hospitalNameIndex = which;
                            hospitalIdTxt.setText(hospitalNameList.get(which));
                        }
                    });
                }
            }
        });

        ImageView patientMoreBtn = findViewById(R.id.patient_name_more);
        patientMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (patientNameList.size() > 0) {
                    DialogManager.showRadioDialog(currentActivity, null,
                            patientNameList.toArray(new CharSequence[patientNameList.size()]), patientNameIndex, null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            patientNameIndex = which;
                            patientIdTxt.setText(patientNameList.get(which));
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    private void checkInputState() {
        if (hospitalIdTxt.getText().toString().equals("")) {
            Common.cm.showAlertDlg(getString(R.string.input_err_title), getString(R.string.hospital_input_err_msg),null, null);
            return;
        }
        if (patientIdTxt.getText().toString().equals("")) {
            Common.cm.showAlertDlg(getString(R.string.input_err_title), getString(R.string.patient_id_input_err_msg),null, null);
            return;
        }
//        if (patientNameTxt.getText().toString().equals("")) {
//            Common.cm.showAlertDlg(getString(R.string.input_err_title), getString(R.string.patient_name_input_err_msg),null, null);
//            return;
//        }

        Intent intent = new Intent(this, ActivityPreview.class);
        LocalStorageManager localStorageManager = new LocalStorageManager();
        PatientImage data = new PatientImage();
        data.setUserId(localStorageManager.getUserLoginStatus());
        data.setHospitalId(hospitalIdTxt.getText().toString());
        data.setPatientId(patientIdTxt.getText().toString());
        data.setReceiptDate(todayCalendar.getTimeInMillis());
        intent.putExtra("data", data);
        startActivity(intent);
    }
}

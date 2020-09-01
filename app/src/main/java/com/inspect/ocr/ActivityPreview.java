package com.inspect.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inspect.ocr.common.Common;
import com.inspect.ocr.common.LocalStorageManager;
import com.inspect.ocr.models.PatientImage;
import com.inspect.ocr.rest.APIManager;
import com.inspect.ocr.utils.ImageUtils;
import com.inspect.ocr.view.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.inspect.ocr.common.Common.cm;
import static com.inspect.ocr.common.Common.currentActivity;

public class ActivityPreview extends AppCompatActivity {

    private static final int CAMERA_ACTIVITY_ID = 2002;
    private final static int CAMERA_PERMISSIONS_RESULT = 102;
    private Uri imageUri;
    private File targetImage = null;

    private RelativeLayout loadingLayout;
    private TouchImageView imageView;
    private PatientImage data;

    private ArrayList<String> hospitalNameList = new ArrayList<>();
    private ArrayList<String> patientNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        currentActivity = this;

        LocalStorageManager localStorageManager = new LocalStorageManager();

        if (localStorageManager.getHospitalName() != null) {
            hospitalNameList = cm.convertToArrayListFromString(localStorageManager.getHospitalName());
        }
        if (localStorageManager.getPatientName() != null) {
            patientNameList = cm.convertToArrayListFromString(localStorageManager.getPatientName());
        }

        ArrayList<String> permissions = new ArrayList();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ArrayList requirePermissions = cm.checkPermissions(permissions);
        if (!requirePermissions.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions((String[]) requirePermissions.toArray(new String[requirePermissions.size()]),
                        CAMERA_PERMISSIONS_RESULT);
            }
        }else {
            camera_call();
        }
        data = (PatientImage) getIntent().getSerializableExtra("data");

        TextView hospitalTxt = findViewById(R.id.hospital_value);
        hospitalTxt.setText(getResources().getString(R.string.hospital_title) + " : " + data.getHospitalId());

        TextView patientIdTxt = findViewById(R.id.patient_id_value);
        patientIdTxt.setText(getResources().getString(R.string.patient_id_title) + " : " + data.getPatientId());

        TextView receiptDateTxt = findViewById(R.id.receipt_date_value);
        receiptDateTxt.setText(getResources().getString(R.string.receipt_date_title) + " : " + cm.convertStringFromTime(data.getReceiptDate()));

        imageView = findViewById(R.id.image_view);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.rotate();
//                showDetailInfo();
            }
        });

        LinearLayout uploadBtn = findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadReceiptImageToServer();
            }
        });

        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.GONE);
    }

    private void camera_call()
    {
        ContentValues values = new ContentValues();
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent1, CAMERA_ACTIVITY_ID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(targetImage != null ){
            targetImage.delete();
            targetImage = null;
        }
        finish();
    }

    public Bitmap rotateImage( Bitmap bitmap ){
        ExifInterface exifInterface = null;
        String filePath = ImageUtils.getRealPathFromURI(imageUri.toString());
        try{
            exifInterface = new ExifInterface( filePath );
        }catch ( IOException e ){

        }
        int orientation = exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED );
        Matrix matrix = new Matrix();
        switch( orientation ){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
            default:
                break;
        }
        Bitmap rotateBitmap = Bitmap.createBitmap( bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true );
        return rotateBitmap;
    }
    private void uploadReceiptImageToServer() {
        if (targetImage == null) {
            Common.cm.showAlertDlg(getString(R.string.err_title), getString(R.string.img_err_msg),null, null);
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", data.getUserId());
        params.put("hospital_id", data.getHospitalId());
        params.put("patient_id", data.getPatientId());
        params.put("receipt_date", String.valueOf(data.getReceiptDate()));
        params.put("created_date", String.valueOf(Calendar.getInstance().getTimeInMillis()));

        loadingLayout.setVisibility(View.VISIBLE);
        APIManager apiManager = new APIManager( Global.UPLOAD_IMAGE_URL, params, "receipt_image", targetImage.getAbsolutePath(), null, new APIManager.AsyncEventListener() {
            @Override
            public void onServerResult(String result) {
                JSONObject object = null;
                loadingLayout.setVisibility(View.GONE);
                try {
                    object = new JSONObject(result);
                    String status = (String) object.getJSONObject("status").get("status_code");
                    String statusFlag = (String) object.getJSONObject("status").get("status_text");

                    if (status.equals("-1") && statusFlag.equals("1")) { //success
                        if (!hospitalNameList.equals(data.getHospitalId()))
                            hospitalNameList.add(data.getHospitalId());
                        if (!patientNameList.equals(data.getPatientId()))
                            patientNameList.add(data.getPatientId());
                        LocalStorageManager localStorageManager = new LocalStorageManager();
                        if (hospitalNameList.size() > 0)
                            localStorageManager.setHospitalName(cm.stringFromStringArray(hospitalNameList));
                        if (patientNameList.size() > 0)
                            localStorageManager.setPatientName(cm.stringFromStringArray(patientNameList));

                        imageView.setImageDrawable(currentActivity.getResources().getDrawable(R.drawable.image_placeholder));
                        imageUri = null;
                        if(targetImage.exists() ){
                            targetImage.delete();
                        }
                        targetImage = null;
                        Intent i=new Intent(currentActivity, MedicineBookActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else {
                        Common.cm.showAlertDlg(getString(R.string.err_title), getString(R.string.upload_err_msg),null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Common.cm.showAlertDlg(getString(R.string.err_title), getString(R.string.upload_err_msg),null, null);
                }
            }
        });
        apiManager.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_ACTIVITY_ID:
                if (resultCode == RESULT_OK) {
                    try
                    {
                        String filePath = ImageUtils.getRealPathFromURI(imageUri.toString());
                        final File dest = new File(filePath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                        bitmap = rotateImage( bitmap );
                        imageView.setImageBitmap(bitmap);
                        if (dest.exists()) {
                            targetImage = ImageUtils.saveBitmapToFile( dest, bitmap, 50 );
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }else{
                    onBackPressed();
                }
                break;
        }
    }

/*    public void getRotation(){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break; //Natural orientation
            case Surface.ROTATION_90: degrees = 90; break; //Landscape left
            case Surface.ROTATION_180: degrees = 180; break;//Upside down
            case Surface.ROTATION_270: degrees = 270; break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

//STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);
        mCamera.setParameters(params);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSIONS_RESULT:
                if (cm.hasPermission(Manifest.permission.CAMERA)) {
                    camera_call();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }
}

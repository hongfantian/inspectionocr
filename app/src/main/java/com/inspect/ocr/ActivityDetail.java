package com.inspect.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.inspect.ocr.models.PatientImage;
import com.inspect.ocr.view.TouchImageView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.inspect.ocr.common.Common.cm;
import static com.inspect.ocr.common.Common.currentActivity;

public class ActivityDetail extends AppCompatActivity {

    private TouchImageView imageView;
    private PatientImage data;
    private ProgressBar pb;
    private RelativeLayout detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        currentActivity = this;
        data = (PatientImage) getIntent().getSerializableExtra("data");

        TextView hospitalTxt = findViewById(R.id.hospital_value);
        hospitalTxt.setText(getResources().getString(R.string.hospital_title) + " : " + data.getHospitalId());

        TextView patientIdTxt = findViewById(R.id.patient_id_value);
        patientIdTxt.setText(getResources().getString(R.string.patient_id_title) + " : " + data.getPatientId());

        TextView receiptDateTxt = findViewById(R.id.receipt_date_value);
        receiptDateTxt.setText(getResources().getString(R.string.receipt_date_title) + " : " + cm.convertStringFromTime(data.getReceiptDate()));

        detailView = findViewById( R.id.detain_info );

        imageView = findViewById(R.id.image_view);

        RelativeLayout topLayout = findViewById( R.id.topLayout );
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDetailInfo();
            }
        });

        pb = findViewById(R.id.loading_spinner);
        pb.setVisibility( View.VISIBLE );
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();

        Picasso picasso = new Picasso.Builder( this )
                .downloader( new OkHttp3Downloader( okHttpClient ) )
                .build();

        File imgFile = new File(data.getImagePath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }else{
            picasso.get()
                    .load(data.getImagePath().replace("localhost", "192.168.1.111"))
                    .placeholder(R.drawable.image_placeholder)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //do smth when picture is loaded successfully
                            pb.setVisibility( View.GONE );
                            //holder.patientIdTxt.setText( "成功" );
                        }
                        @Override
                        public void onError(Exception ex) {
                            //do smth when there is picture loading error
                            //holder.loadingView.setVisibility(View.GONE);
                        }
                    });
        }

        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.rotate();
                showDetailInfo();
            }
        });
    }

    public void showDetailInfo(){
        if( detailView.getVisibility() == View.GONE ){
            detailView.setVisibility( View.VISIBLE );
        }else{
            detailView.setVisibility( View.GONE );
        }

    }
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }
}

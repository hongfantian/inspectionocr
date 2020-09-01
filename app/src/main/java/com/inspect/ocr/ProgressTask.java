package com.inspect.ocr;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import jp.dki.labtestvaluesgraph.Data;
import jp.dki.labtestvaluesgraph.JsonPojo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgressTask extends AsyncTask<Void,Void,Void> {
    private int progressStatus=0;
    private Handler handler = new Handler();
    private ProgressDialog pd = null;

    public ProgressTask( Context applicationContext ){
        pd = new ProgressDialog( applicationContext );
        pd.setProgress(1);
        pd.setCancelable( true );
    }
    // Initialize a new instance of progress dialog

    @Override
    protected void onPreExecute(){
/*        super.onPreExecute();
        pd.setIndeterminate(false);

        // Set progress style horizontal
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // Set the progress dialog background color
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));

        // Make the progress dialog cancellable
//        pd.setCancelable(true);
        // Set the maximum value of progress
//        pd.setMax(100);
        // Finally, show the progress dialog
        pd.show();*/
    }

    @Override
    protected Void doInBackground(Void...args){
        // Set the progress status zero on each button click
        progressStatus = 0;
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start(); // Start the operation
        return null;
    }

    protected void onPostExecute(){
        // do something after async task completed.
    }

    private JsonPojo[] getData() {
        String url = Global.RESULT_GET_ADDRESS;

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            if (response.body() != null) {
                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JsonPojo[] pojo = mapper.readValue(body, JsonPojo[].class);
                return pojo;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
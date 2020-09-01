package com.inspect.ocr;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressJob {
    private ProgressDialog pd = null;

    public ProgressJob( Context applicationContext ){
        pd = new ProgressDialog( applicationContext );
        pd.setProgress(1);
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable( true );
    }
    public void show(){
        pd.show();
    }
    public void hide(){
        pd.dismiss();
        pd.hide();
        pd = null;
    }
}

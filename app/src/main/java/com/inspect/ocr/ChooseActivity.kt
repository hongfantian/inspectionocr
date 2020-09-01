package com.inspect.ocr

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity



class ChooseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_choose )
    }
    fun clickMedicinePocket( v: View){
        Global.OPERATION = Global.MEDICINE_OPERATION
        val intent = Intent(this, MedicineBookActivity::class.java).apply {
            putExtra( "operation", Global.MEDICINE_OPERATION )
        }
        startActivity(intent)
    }
    fun clickInspect( v:View ){
        Global.OPERATION = Global.INSPECT_OPERATION
        val intent = Intent(this, OperationActivity::class.java).apply {
                        putExtra( "operation", Global.INSPECT_OPERATION )
        }
        startActivity(intent)
    }
}
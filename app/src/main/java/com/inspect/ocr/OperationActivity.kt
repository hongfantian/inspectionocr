package com.inspect.ocr

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class OperationActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation )
    }
    fun clickRead( v: View){
        if( Global.OPERATION == Global.INSPECT_OPERATION ){
            val intent = Intent(this, KindActivity::class.java).apply {
                //            putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }
    }
    fun clickShow( v:View ){
        if( Global.OPERATION == Global.INSPECT_OPERATION ){
            val intent = Intent(this, InspectResultFormatActivity::class.java).apply {
                //            putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }
    }
}
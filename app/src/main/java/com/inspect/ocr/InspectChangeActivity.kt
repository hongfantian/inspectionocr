package com.inspect.ocr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InspectChangeActivity : AppCompatActivity() {
    lateinit var eName:String
    lateinit var jName:String
    var value:Double = 0.0
    var isNumber:Int = 1
    lateinit var valueView:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_inspect )

        eName = this.intent.getStringExtra("eName" )
        jName = this.intent.getStringExtra("jName" )
        var isNumberStr = this.intent.getStringExtra("isNumber" ) as String
        try{
            isNumber = isNumberStr.toInt()
        }catch(e: NumberFormatException){
            isNumber = 0
        }

        var valueStr:String = this.intent.getStringExtra( "value" )
        if( isNumber == 1 ) {
            try {
                value = valueStr.toDouble()
            }catch( e: NumberFormatException ){
                value = 0.0
            }
        }

        var jNameView: TextView = findViewById(R.id.txt_inspect_j_name )
        //var eNameView: TextView = findViewById(R.id.txt_inspect_e_name )
        valueView =  findViewById(R.id.txt_inspect_value )


        jNameView.setText( jName )
        //eNameView.setText( eName )
        valueView.setText( valueStr )
    }

    fun confirm( v: View ){
        if( sendInspectValueToResult() ){
            onBackPressed()
        }
    }
    fun back( v:View ){
        onBackPressed()
    }
    private fun sendInspectValueToResult():Boolean{
        var valueStr:String = valueView.text.toString()
        var isValidate:Boolean = true
        try {
            if( isNumber == 1 ){
                value = valueStr.toDouble()
            }
        }catch( e: NumberFormatException ){
            val toast = Toast.makeText( applicationContext, "検査値は数字である必要があります。", Toast.LENGTH_SHORT )
            toast.show()
            isValidate = false
            return false
        }

        if( isValidate ){
            val intent = Intent().apply {
                putExtra( "eName", eName )
                putExtra( "value", valueStr )
            }
            setResult( RESULT_OK, intent )
            return true
        }
        return false
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
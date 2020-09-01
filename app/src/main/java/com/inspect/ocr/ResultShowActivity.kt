package com.inspect.ocr

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import com.inspect.ocr.com.inspect.ocr.datepicker.DatePickerFragment

class ResultShowActivity : AppCompatActivity(){
    companion object {
        const val INSPECT_CHANGE_REQUEST_CODE = 0
    }
    lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_show )
        tableLayout = findViewById(R.id.result_show )
        createTableColumn()
    }
    private fun createTableColumn(){
        for( i in 0 until InspectResult.result.size ){
            var row = getLayoutInflater().inflate(R.layout.table_row, null)
            var item:Item = InspectResult.getAt(i)
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            setCell(row as TableRow, item )
            tableLayout.addView( row )
        }
    }
    private fun setCell( row:TableRow, item:Item ){
        var jNameView:TextView = row.findViewById( R.id.txt_inspect_j_name )

        var inspectName:String = item.jName + " (" + item.eName + ")"
        jNameView.setText( inspectName )
        var eNameView:TextView = row.findViewById( R.id.txt_inspect_e_name )
        eNameView.setText( item.eName )

        var valueView:TextView = row.findViewById( R.id.txt_inspect_value )
            valueView.setText( item.inspectValue.toString() )

        var isNumberView:TextView = row.findViewById( R.id.txt_is_number )
        isNumberView.setText( "0" )
        if( item.isNumber )
            isNumberView.setText( "1" )

        var ck_confirm:CheckBox = row.findViewById( R.id.ck_confirm )
        ck_confirm.isChecked = false
        if( item.whoChanged == 1 ){
            ck_confirm.isChecked = true
        }
        ck_confirm.setOnClickListener{
            if( it is CheckBox ){
                val checked: Boolean = it.isChecked
                if( checked ){
                    item.whoChanged = 1
                }else{
                    item.whoChanged = 0
                }
            }
        }
    }
    fun changeInspectResult( v: View){
        var jNameView:TextView = v.findViewById<TextView>( R.id.txt_inspect_j_name)
        var jName:String = jNameView.text as String

        var eNameView:TextView = v.findViewById<TextView>( R.id.txt_inspect_e_name)
        var eName:String = eNameView.text as String

        var valueView:TextView = v.findViewById<TextView>( R.id.txt_inspect_value )
        var value:String = valueView.text as String

        var isNumberView:TextView = v.findViewById<TextView>( R.id.txt_is_number )
        var isNumber:String = isNumberView.text as String

        val intent = Intent(this, InspectChangeActivity::class.java).apply {
            putExtra( "jName", jName )
            putExtra( "eName", eName )
            putExtra( "value", value )
            putExtra( "isNumber", isNumber )
        }
        startActivityForResult(intent, INSPECT_CHANGE_REQUEST_CODE )
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, data: Intent? ) {
        var eName:String = ""
        var valueStr:String = ""
        if (requestCode == INSPECT_CHANGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK ) {
                eName = data!!.getStringExtra("eName" )
                valueStr = data!!.getStringExtra("value" )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

        setValueByInspectName( eName, valueStr, 1 )
    }
    private fun setValueByInspectName( _eName:String, _value:String, _whoChange:Int = 0 ){
        for( i in 0 until tableLayout.size ){
            var row = tableLayout.get(i)
            var eNameView:TextView = row.findViewById<TextView>( R.id.txt_inspect_e_name)
            var eName:String = eNameView.text as String
            var valueView:TextView = row.findViewById<TextView>( R.id.txt_inspect_value )
            var ckConfirm:CheckBox = row.findViewById<CheckBox>( R.id.ck_confirm )
            if( eName == _eName ){
                if( _whoChange == 1 ) {
                    ckConfirm.isChecked = true
                }else{
                    ckConfirm.isChecked = false
                }
                valueView.setText( _value )
                InspectResult.setInspectValue( eName, _value, _whoChange )
            }
        }
    }
    fun cancel( v:View ){
        onBackPressed()
    }
    fun submit( v:View ){
        val intent = Intent(this, DateChooseActivity::class.java).apply {
            //            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

}
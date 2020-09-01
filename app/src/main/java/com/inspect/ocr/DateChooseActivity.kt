package com.inspect.ocr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class DateChooseActivity: AppCompatActivity() {
    lateinit var tableLayout: TableLayout
    lateinit var progressBar: ProgressBar
    lateinit var hintTextView: TextView
    lateinit var datePicker: DatePicker
    lateinit var progressContainer: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_date )
        datePicker = findViewById( R.id.picker_date )
        progressBar = findViewById( R.id.progressBar )
        hintTextView = findViewById( R.id.hint_txt )
        progressContainer = findViewById( R.id.progress_container )

        progressContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        hintTextView.visibility = View.GONE

        datePicker.isEnabled = true
        if( !InspectResult.isChangedDate ){
            var dateFormat:List<String>? = InspectResult.getDateByFormat()
            if(  dateFormat != null && dateFormat.size == 3 ) {
                try {
                    var year:Int = dateFormat[0].toInt()
                    var month:Int = dateFormat[1].toInt()-1
                    var day:Int = dateFormat[2].toInt()
                    datePicker.updateDate( year, month, day )
                } catch (ignored: Exception) {
                    var error = ignored
                }
            }
            datePicker.isEnabled = false
        }
    }
    fun clickBack( v: View){
        onBackPressed()
    }
    fun clickSend( v:View ){
        var year:Int = datePicker.year
        var month:Int = datePicker.month
        var day:Int = datePicker.dayOfMonth
        InspectResult.inspectDate = Date( (year-1900), month, day ).toString()

        progressContainer.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE
/*        InspectResult.setConfident()*/
        Communication.sendPostRequest( this.applicationContext ){ Success ->
            progressContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            hintTextView.visibility = View.GONE
            if (Success) {
                Global.makeLargeTextToast( applicationContext,"検査結果の保管に成功しました。").show()
                val intent = Intent(this, ChooseActivity::class.java).apply {
                    //            putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
                this.finish()
            } else {
                Global.makeLargeTextToast( applicationContext,"エラーが発生しました。").show()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
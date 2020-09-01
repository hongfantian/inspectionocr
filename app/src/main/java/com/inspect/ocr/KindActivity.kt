package com.inspect.ocr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.inspect.ocr.Global.Companion.makeLargeTextToast
import com.inspect.ocr.data.ResultList

class KindActivity : AppCompatActivity(){
    lateinit var progressBar: ProgressBar
    lateinit var hintTextView: TextView
    lateinit var progressContainer: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kind )

        progressBar = findViewById( R.id.progressBar )
        hintTextView = findViewById( R.id.hint_txt )
        progressContainer = findViewById( R.id.progress_container )

        progressContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        hintTextView.visibility = View.GONE

    }
    fun clickManualInput( v: View){
        progressContainer.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE

        ResultList.resultList.clear()
        Communication.getResult( applicationContext, Profile.userId.toString() ){ Success ->
            progressContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            hintTextView.visibility = View.GONE
            if ( Success == 1 ) {
                val intent = Intent(this, InspectDateListActivity::class.java).apply {
                    //            putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
            } else {
                var msg:String = "エラーが発生しました。"
                Global.makeLargeTextToast( applicationContext,msg ).show()
            }
        }
    }

    fun clickScan( v:View ){
        val intent = Intent(this, OcrActivity::class.java).apply {
            //            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}
package com.inspect.ocr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.inspect.ocr.common.Common
import com.inspect.ocr.common.LocalStorageManager


class MainActivity : AppCompatActivity() {

    lateinit var progressContainer: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var hintTextView: TextView
    lateinit var retryBtn: Button

    var userName:String = ""
    var password:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Common.myApp = this

        setContentView(R.layout.activity_main )
        progressBar = findViewById( R.id.progressBar )
        hintTextView = findViewById( R.id.hint_txt )
        progressContainer = findViewById( R.id.progress_container )
        retryBtn = findViewById( R.id.retry_btn )
        retryBtn.visibility = View.GONE

//        val intent = Intent(this, OcrActivity::class.java).apply {
//            //            putExtra(EXTRA_MESSAGE, message)
//        }
//        startActivity(intent)
//        this.finish()

        configuration()
    }
    fun clickRetry( v:View ){
        progressContainer.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE
        configuration()
    }
    private fun configuration(){
        Communication.getInspect( applicationContext ){ Success ->
            progressContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            hintTextView.visibility = View.GONE

            if ( Success == 1 ) {
/*
                if( userName == "" || password == "" ){
                    val intent = Intent(this, LoginActivity::class.java).apply {
                        //            putExtra(EXTRA_MESSAGE, message)
                    }
                    startActivity(intent)
                }else{
                    val intent = Intent(this, ChooseActivity::class.java).apply {
                        //            putExtra(EXTRA_MESSAGE, message)
                    }
                    startActivity(intent)
                }
*/
                val intent = Intent(this, LoginActivity::class.java).apply {
                    //            putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
                this.finish()
            } else {
                retryBtn.visibility = View.VISIBLE
                var msg:String = "インターネットに接続されているかを確認し、アプリを再起動してください。"
                Global.makeLargeTextToast( applicationContext,msg ).show()
            }
        }
    }
}

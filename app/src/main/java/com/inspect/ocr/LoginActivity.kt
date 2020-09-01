package com.inspect.ocr

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.inspect.ocr.common.LocalStorageManager

class LoginActivity : AppCompatActivity() {

    lateinit var userNameView:EditText
    lateinit var passwordView:EditText
    lateinit var progressContainer: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var hintTextView: TextView

    var userName:String = ""
    var password:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login )
        userNameView = findViewById( R.id.edit_txt_id )
        passwordView = findViewById( R.id.edit_txt_password )

        progressContainer = findViewById( R.id.progress_container )
        progressBar = findViewById( R.id.progressBar )
        hintTextView = findViewById( R.id.hint_txt )

        progressContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        hintTextView.visibility = View.GONE

        var name:String? = LocalStorageManager.getUserLoginStatus()
        if( name != null ){
            userName = name
            userNameView.setText( userName )
        }

        var pass:String? = LocalStorageManager.getPassword()
        if( pass != null ){
            password = pass
            passwordView.setText( password )
        }


    }
    private fun savePreferences() {
        userName = userNameView.text.toString()
        password = passwordView.text.toString()

        LocalStorageManager.setPassword( password )
        LocalStorageManager.setUserLoginStatus( userName )
    }
    fun login( v: View ){
        userName = userNameView.text.toString()
        password = passwordView.text.toString()

        if( userName == "" || password == "" ){
            Global.makeLargeTextToast( applicationContext, "IDとパスワードを入力してください。" ).show()
            return
        }
        progressContainer.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE

        Communication.userRequest( applicationContext, userName, password ){ Success ->
            progressContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            hintTextView.visibility = View.GONE

            if ( Success == 1 ) {
                val intent = Intent(this, ChooseActivity::class.java).apply {
                    //            putExtra(EXTRA_MESSAGE, message)
                }
                savePreferences()
                startActivity(intent)
            } else {
                var msg:String = "エラーが発生しました。"
                if( Success == 2 ){
                    msg = "ユーザーIDとパスワードが間違っています。"
                }
                Global.makeLargeTextToast( applicationContext,msg ).show()
            }
        }
    }
    override fun onBackPressed() {
        val alt_bld = AlertDialog.Builder(this)
            .setTitle( "医療検査" )
            .setMessage( "アプリを終了しますか？" )
            .setPositiveButton(
                "はい",
                DialogInterface.OnClickListener { dialog, which ->
                    //Do Something Here
                    this.finish()
                })
            .setNegativeButton(
                "いいえ",
                DialogInterface.OnClickListener { dialog, which ->
                    //Do Something Here
                })
        val dialog: AlertDialog = alt_bld.create()
//        dialog.window!!.setBackgroundDrawableResource( R.color.green );
        dialog.show()
        dialog.getButton( AlertDialog.BUTTON_NEGATIVE).setTextColor( Color.BLACK )
        dialog.getButton( AlertDialog.BUTTON_POSITIVE).setTextColor( Color.BLACK )
    }
}
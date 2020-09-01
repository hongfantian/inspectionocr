package com.inspect.ocr

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.inspect.ocr.common.LocalStorageManager
import com.inspect.ocr.data.ResultList

class InspectDateListActivity : AppCompatActivity() {
    lateinit private var list:  ArrayList<java.util.HashMap<String, String>>
    val FIRST_COLUMN = "First"
    lateinit var progressContainer: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var hintTextView: TextView
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_list )

        progressContainer = findViewById( R.id.progress_container )
        progressBar = findViewById( R.id.progressBar )
        hintTextView = findViewById( R.id.hint_txt )

        progressContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        hintTextView.visibility = View.GONE

        listView = findViewById( R.id.listView1 )
        populateList();
        val adapter = DateListViewAdapter( applicationContext, list )
        listView.setAdapter( adapter )

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {
                // value of item that is clicked
                val item = listView.getItemAtPosition(position) as HashMap<String, String>
                val inspectDate:String = item["First"] as String
                InspectResult.set( ResultList.getAt(position) )
                InspectResult.inspectDate = inspectDate
                InspectResult.isChangedDate = false
                val intent = Intent( applicationContext, ResultShowActivity::class.java).apply {
                }
                startActivity(intent)
            }
        }

        listView.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {

            override fun onItemLongClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long):Boolean {
                // value of item that is clicked
                val item = listView.getItemAtPosition(position) as HashMap<String, String>
                val inspectDate:String = item["First"] as String
                var userId:Int = Profile.userId
                if( userId <= 0 ){
                    return true
                }
                isDeleteInspect( inspectDate )
                return true
            }
        }
    }

    private fun delete( inspectDate:String ){
        progressContainer.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE

        Communication.removeResultByDate( applicationContext, inspectDate ){ Success ->
            progressContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            hintTextView.visibility = View.GONE

            if ( Success == 1 ) {
                ResultList.removeItemByDate( inspectDate )
                populateList();

                val adapter = DateListViewAdapter( applicationContext, list )
                listView.setAdapter( adapter )
                adapter.notifyDataSetChanged()
            } else {
                var msg:String = "エラーが発生しました。"
                Global.makeLargeTextToast( applicationContext,msg ).show()
            }
        }
    }
    private fun isDeleteInspect( date:String ){
        val alt_bld = AlertDialog.Builder(this)
            .setTitle( R.string.app_name )
            .setMessage( date + "の検査項目を削除しますか？" )
            .setPositiveButton(
                "はい",
                DialogInterface.OnClickListener { dialog, which ->
                    //Do Something Here
                    delete( date )
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
    private fun populateList(){
        list = ArrayList<HashMap<String, String>>()
        for( i in 0 until ResultList.resultList.size ){
            val hashmap = HashMap<String, String>()
            var result = ResultList.getAt(i)
            hashmap[FIRST_COLUMN] = Global.removeWhiteSpace(result.inspectDate)
            list.add(hashmap)
        }
    }

    fun clickInspectRegister( v:View ){
        InspectResult.initialize()
        val intent = Intent(this, ResultShowActivity::class.java).apply {
        }
        startActivity(intent)
    }
}
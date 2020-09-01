package com.inspect.ocr

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.inspect.ocr.data.Result
import com.inspect.ocr.data.ResultList

class TestListPageActivity : AppCompatActivity(){

    lateinit private var list:  ArrayList<java.util.HashMap<String, String>>
    val FIRST_COLUMN = "First"
    val SECOND_COLUMN = "Second"
    val THIRD_COLUMN = "Third"
    val FOURTH_COLUMN = "Fourth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.one_inspect )
        var listView: ListView = findViewById( R.id.listView1 )

        populateList( 0 );
        val adapter = ListViewAdapter( applicationContext, list )
        listView.setAdapter( adapter )
    }
    private fun populateList( pageNo:Int ) { // TODO Auto-generated method stub
        list = ArrayList<HashMap<String, String>>()
        var idx:Int = pageNo * Global.VIEWCOUNT
        if( idx > 0 ){
            idx = -1
        }
        var first_result: Result? = null
        if( ResultList.resultList.size > idx ) {
            first_result = ResultList.resultList.get(idx)
        }
        var second_result: Result? = null
        if( ResultList.resultList.size > (idx +1) ) {
            second_result = ResultList.resultList.get(idx + 1)
        }
        var third_result: Result? = null
        if( ResultList.resultList.size > (idx +2) ){
            third_result = ResultList.resultList.get(idx+2)
        }
        if( first_result != null ){
            for( i in 0 until first_result.count() ){
                val hashmap = HashMap<String, String>()
                var item:Item? = first_result.getAt(i)
                if( item != null ){
                    hashmap[FIRST_COLUMN] = item.jName
/*
                    hashmap[SECOND_COLUMN] = item.inspectValue
                    if( second_result != null ) {
                        hashmap[THIRD_COLUMN] = second_result.getAt(i)!!.inspectValue
                    }
                    if( third_result != null ) {
                        hashmap[FOURTH_COLUMN] = third_result.getAt(i)!!.inspectValue
                    }
*/
                }
                list.add(hashmap)
            }
        }
    }
}
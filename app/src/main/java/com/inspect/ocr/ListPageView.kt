package com.inspect.ocr

import android.content.Context
import android.graphics.Color
import com.inspect.ocr.data.Result
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.inspect.ocr.data.ResultList


class ListPageView : LinearLayout {

    lateinit private var list:  ArrayList<java.util.HashMap<String, String>>
    lateinit private var header:View
    val FIRST_COLUMN = "First"
    val SECOND_COLUMN = "Second"
    val THIRD_COLUMN = "Third"
    val FOURTH_COLUMN = "Fourth"
    lateinit var listView: ListView
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int,
        pageNo: Int
    ) : super(context, attrs, defStyle) {
        initView(pageNo)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        pageNo: Int
    ) : super(context, attrs) {
        initView(pageNo)
    }

    constructor(context: Context?, pageNo: Int) : super(context) {
        initView(pageNo)
    }

    private fun initView(pageNo: Int) {
        val view =
            inflate(context, R.layout.one_inspect, null )
        listView = view.findViewById( R.id.listView1 )

//        View header = getLayoutInflater().inflate(R.layout.inspect_list_row_header, null, false) ;
        header = inflate( context, R.layout.inspect_list_row_header, null )
        listView.addHeaderView( header )

        populateList( pageNo )
        val adapter = ListViewAdapter( context, list )
        listView.setAdapter( adapter )
        addView(view)
    }
/*    fun getScrollPos():Int{
        var curTop:Int = listView.firstVisiblePosition
        return curTop
    }*/
    private fun populateList( pageNo:Int ) { // TODO Auto-generated method stub
        list = ArrayList<HashMap<String, String>>()
        var idx:Int = pageNo * Global.VIEWCOUNT
        var first_result:Result? = null

        var headerInNameTV: TextView = header.findViewById( R.id.inspect_name_txt )
        var headerInValueTV1:TextView = header.findViewById( R.id.inspect_value_txt_1 )
        var headerInValueTV2:TextView = header.findViewById( R.id.inspect_value_txt_2 )
        var headerInValueTV3:TextView = header.findViewById( R.id.inspect_value_txt_3 )

        if( ResultList.resultList.size > idx ) {
            first_result = ResultList.resultList.get(idx)
        }
        var second_result:Result? = null
        if( ResultList.resultList.size > (idx +1) ) {
            second_result = ResultList.resultList.get(idx + 1)
        }
        var third_result:Result? = null
        if( ResultList.resultList.size > (idx +2) ){
            third_result = ResultList.resultList.get(idx+2)
        }
        if( first_result != null ){
            if( first_result.count() > 0 ){
                headerInValueTV1.setText( first_result.inspectDate )
            }
            if( second_result != null && second_result.count() > 0 ){
                headerInValueTV2.setText( second_result.inspectDate )
            }
            if( third_result != null && third_result.count() > 0 ){
                headerInValueTV3.setText( third_result.inspectDate )
            }

            for( i in 0 until first_result.count() ){
                val hashmap = HashMap<String, String>()
                var item:Item? = first_result.getAt(i)
                if( item != null ){
                    hashmap[FIRST_COLUMN] = Global.removeWhiteSpace(item.jName)
                    hashmap[SECOND_COLUMN] = item.inspectValue
                    if( second_result != null ) {
                        hashmap[THIRD_COLUMN] = second_result.getAt(i)!!.inspectValue
                    }
                    if( third_result != null ) {
                        hashmap[FOURTH_COLUMN] = third_result.getAt(i)!!.inspectValue
                    }
                }
                list.add(hashmap)
            }
        }
    }
}

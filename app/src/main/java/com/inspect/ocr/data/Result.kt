package com.inspect.ocr.data

import com.inspect.ocr.Item
import org.json.JSONObject

class Result {
    var inspectDate:String = ""
    var inspectResult:ArrayList<Item> = ArrayList<Item>()
    fun insert( element: JSONObject ){
        var inspectIdStr:String = element["id"] as String
        var inspectId:Int = -1
        try {
            inspectId = inspectIdStr.toInt()
        }catch(e: NumberFormatException){

        }
        var item:Item = Item( element )
        inspectResult.add( item )
    }
    fun count():Int{
        return inspectResult.size
    }
    fun getAt( i:Int ):Item?{
        if( inspectResult.size <= i ){
            return null
        }
        return inspectResult.get(i)
    }
}
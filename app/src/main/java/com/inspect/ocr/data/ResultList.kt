package com.inspect.ocr.data

import org.json.JSONArray
import org.json.JSONObject

class ResultList {
    companion object {
        var resultList:ArrayList<Result> = ArrayList<Result>()
        fun parseResult( retList: JSONArray){
            for( i in 0 until retList.length() ){
                var jsonResult:JSONObject = retList.getJSONObject( i )
                var pos:Int = posInspectDate( jsonResult["inspectDate"] )
                var result:Result? = null
                if( pos == -1 ){
                    result = Result()
                    result.inspectDate = jsonResult["inspectDate"] as String
                    resultList.add( result )
                    pos = resultList.size-1
                }
                result = resultList.get(pos)
                result.insert( jsonResult )
            }
        }
        private fun posInspectDate( inspectDate:Any ):Int{
            var inspectDateStr:String = inspectDate.toString()
            var isExist:Boolean = false
            for( i in 0 until resultList.size ){
                var result:Result = resultList.get(i)
                if( inspectDateStr == result.inspectDate ){
                    return i
                }
            }
            return -1
        }
        public fun removeItemByDate( inspectDate:String ){
            var removeResult:Result? = null
            for( i in 0 until resultList.size ){
                var result:Result = resultList.get(i)
                if( result.inspectDate == inspectDate ){
                    removeResult = result
                    break;
                }
            }
            if( removeResult != null ){
                resultList.remove( removeResult )
            }
        }
        public fun getAt( i:Int ):Result{
            return resultList.get(i)
        }
        fun getCount():Int{
            return resultList.size
        }
    }
}
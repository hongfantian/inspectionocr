package com.inspect.ocr

import org.json.JSONObject

class Item {
    var id:Int = -1
    var jName:String = ""
    var eName:String = ""
    var point:Double = 0.0
    var inspectValue:String = ""
    var minValue:Double = 0.0
    var maxValue:Double = 0.0
    var unit:String = ""
    var manMinValue:String = "0.0"
    var manMaxValue:String = "0.0"
    var womanMinValue:String = "0.0"
    var womanMaxValue:String = "0.0"
    var isNumber:Boolean = true
    var conference:Boolean = false // set if ocr value is 100% confident.
    var whoChanged:Int = 0 // 0:ocr, 1: サーバー, 2:ユーザー

    constructor( _id:Int
                 ,_jName:String = ""
                ,_eName:String = ""
                ,_inspectValue:String = "0.0"
                ,_manMinValue:String = "0.0"
                ,_manMaxValue:String = "0.0"
                ,_womanMinValue:String = "0.0"
                ,_womanMaxValue:String = "0.0"
                ,_unit:String = ""
                ,_isNumber:String = "1" ){
        id = _id
        jName = Global.removeWhiteSpace(_jName )
        eName = Global.removeWhiteSpace(_eName)
        inspectValue = _inspectValue
        manMinValue = _manMinValue
        manMaxValue = _manMaxValue
        womanMinValue = _womanMinValue
        womanMaxValue = _womanMaxValue
        unit = _unit
        if( _isNumber == "1" ){
            isNumber = true
        }
        setMinValue()
        setMaxValue()
    }
    constructor( element:JSONObject ){
        var inspectIdStr:String = element["id"] as String
        var inspectId:Int = -1
        try {
            inspectId = inspectIdStr.toInt()
            id = inspectId

            if( element["inspectName"] != null ){
                jName = element["inspectName"] as String
            }
            if( element["inspectAliasName"] != null ){
                eName = element["inspectAliasName"] as String
            }
            if( element["inspectValue"] != null ){
                inspectValue = element["inspectValue"] as String
            }
            if( element["point"] != null ){
                point = element["point"].toString().toDouble()
            }
            if( element["manMinValue"] != null ){
                manMinValue = element["manMinValue"] as String
            }
            if( element["manMaxValue"] != null ){
                manMaxValue = element["manMaxValue"] as String
            }
            if( element["womanMinValue"] != null ){
                womanMinValue = element["womanMinValue"] as String
            }
            if( element["womanMaxValue"] != null ){
                womanMaxValue = element["womanMaxValue"] as String
            }
            if( element["unit"] != null ){
                unit = element["unit"] as String
            }
            if( element.has( "whoChanged" ) && element["whoChanged"] != null ){
                var _whoChanged:String = element["whoChanged"] as String
                if( _whoChanged == "1" ){
                    whoChanged = 1
                }
            }

            if( element["is_number"] != null ){
                var _isNumber:String = element["is_number"] as String
                isNumber = false
                if( _isNumber == "1" ){
                    isNumber = true
                }
            }
        }catch(e: NumberFormatException){
            var ex = e
        }



    }
    constructor( _id:Int, _jName: String ,_inspectValue: String ){
        id = _id
        jName = Global.removeWhiteSpace(_jName )
        inspectValue = _inspectValue
    }

    private fun setMinValue(){
        if( isNumber ){
            minValue = womanMinValue.toDouble()
            if( manMinValue < womanMinValue ){
                minValue = manMinValue.toDouble()
            }
        }
    }
    private fun setMaxValue(){
        if( isNumber ) {
            maxValue = manMaxValue.toDouble()
            if( manMaxValue > womanMaxValue ){
                maxValue = womanMaxValue.toDouble()
            }
       }
    }
}
package com.inspect.ocr

import android.graphics.Rect

class Cell {
    var name:String = ""
    var rect:Rect = Rect()
    var isConfident:Boolean = false
    var lines:Int = 1
    constructor( text:String ){
        name = text
    }
    constructor( text:String, _rect:Rect ){
        name = text
        rect = _rect
    }
    constructor( text:String, _lines:Int ){
        name = text
        lines = _lines
    }
}
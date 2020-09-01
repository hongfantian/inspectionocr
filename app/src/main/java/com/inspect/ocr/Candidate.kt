package com.inspect.ocr

class Candidate {
    lateinit var cell:Cell
    var columnPos:Int = 0
    var rowPos:Int = 0 // row position in ocr paper
    var point:Int = 0
    var sRowPos:Int = 0 // row position in real paper
    var sColumnPos:Int = 0 // column position in real paper
    constructor( _cell:Cell, _columnPos:Int, _rowPos:Int ){
        cell = _cell
        columnPos = _columnPos
        rowPos = _rowPos
    }
    constructor( _cell:Cell, _columnPos:Int, _rowPos:Int, _sColumnPos:Int, _sRowPos:Int ){
        cell = _cell
        columnPos = _columnPos
        rowPos = _rowPos
        sColumnPos = _sColumnPos
        sRowPos = _sRowPos
    }
}
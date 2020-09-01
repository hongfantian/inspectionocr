package com.inspect.ocr

import android.graphics.Rect
import com.google.firebase.ml.vision.text.FirebaseVisionText
import org.json.JSONArray
import com.google.gson.Gson;

class InspectOcrPaper {
    constructor(){
    }
    companion object {
        var paper: ArrayList<InspectColumn> = ArrayList<InspectColumn>()

        var twoLinesOCR:Boolean = false
        public var SIMILARINTERVAL:Int = 20
        fun makePaper( element:Cell ){

        }
        fun initializePaper(){
            twoLinesOCR = false
            paper.clear()
        }
        fun addCellToColumn( element:FirebaseVisionText.Element ){
            if( !twoLinesOCR ){
                twoLinesOCR = hasTwoLines( element.text )
            }

            var pos:Int = getCellColumnPos( element )

            if( pos == paper.size ){
                createCellToColumn( element )
            }else{
                //find column position and insert cell into that column.
                insertCellToColumnAt( element, pos )
            }
        }

        fun reArrangePaper(){
            for( i in 0 until paper.size ){
                var inspectColumn:InspectColumn = paper.get(i)
                var rect = inspectColumn.findMyColumnPos()
                var removeCells:ArrayList<Cell> = ArrayList<Cell>()
                for( j in 0 until inspectColumn.count() ){
                    var cell:Cell = inspectColumn.getCellAt(j)

                    var offset = cell.rect.top - rect.top
                    var height = rect.bottom - rect.top

                    if( !InspectAnalyser.isSameColumnRange( cell.rect, rect ) /*|| offset >= height*/ ){
                        var newCell:Cell = Cell( cell.name, cell.rect )
                        if( (i+1) >= paper.size ){
                            createCellToColumn( newCell )
                        }else{
                            insertCellToColumnAt( newCell, (i+1) )
                        }
                        removeCells.add( cell )
                    }
                }
                for( k in 0 until removeCells.size ){
                    inspectColumn.remove( removeCells.get(k) )
                }
            }
        }
        //determine in which column that element has to be inserted.
        private fun getCellColumnPos( element:FirebaseVisionText.Element ):Int{
            if( paper.size == 0 ){
                return 0
            }
            var ocrRect:Rect = Rect( element.boundingBox!!.left, element.boundingBox!!.top, element.boundingBox!!.right, element.boundingBox!!.bottom )
            for( i in 0 until paper.size ){
                var inspectColumn:InspectColumn = paper.get(i)
                var rect = inspectColumn.findMyColumnPos()

/*
                var offset = Math.abs( element.boundingBox!!.top - rect.top )
                var offset2 = Math.abs( element.boundingBox!!.top - rect.bottom )
                var height = rect.bottom - rect.top
                //if offset is smaller than cell height, two elements has to be in the same column.
                if( offset2 > height/2 &&  offset < height ){
                    return i
                }
*/
                var offset = element.boundingBox!!.top - rect.top
                var height = rect.bottom - rect.top

                if( InspectAnalyser.isSameColumnRange( ocrRect, rect ) && offset <= height ){
                    return i
                }
            }
            return paper.size
        }

        private fun createCellToColumn( element:FirebaseVisionText.Element ){
            var eRect = element.boundingBox
            var cell:Cell = Cell( element.text, Rect( eRect) )
            createCellToColumn( cell )
        }
        private fun createCellToColumn( cell:Cell ){
            var ocrInspectColumn:InspectColumn = InspectColumn()
            ocrInspectColumn.setCell( cell )
            paper.add( getAddColumnPos( cell ), ocrInspectColumn )
        }
        //determine position in which new column that new element has to be added.
        //it's for sorting columns according to coordinate. from Y axis min to max
        private fun getAddColumnPos( element:FirebaseVisionText.Element ):Int{
            return getAddColumnPos( Cell( element.text, Rect( element.boundingBox) ) )
        }
        private fun getAddColumnPos( cell:Cell ):Int{
            var pos = paper.size
            for( i in 0 until paper.size ){
                var inspectColumn:InspectColumn = paper.get(i)
                var rect:Rect = inspectColumn.findMyColumnPos()
                if( rect.top > cell.rect.top ){
                    return i
                }
            }
            return pos
        }
        private fun insertCellToColumnAt( element:FirebaseVisionText.Element, pos:Int ){
            var inspectColumn:InspectColumn = paper.get(pos)
            var eRect = element.boundingBox
            var cell:Cell = Cell( element.text, Rect(eRect) )

            //insert cell to row according to specific column.
            insertCellToColumnAt( cell, pos )
        }
        private fun insertCellToColumnAt( cell:Cell, pos:Int ){
            var inspectColumn:InspectColumn = paper.get(pos)
            inspectColumn.insertCell( cell )
        }
        fun getColumn( idx:Int ):InspectColumn{
            return paper.get(idx)
        }
        fun getPaperValue():ArrayList<InspectColumn> = paper

        fun getPaperToJson():String{
            val gson = Gson()
            var s:String = gson.toJson( paper )
            return s
        }

        public fun hasTwoLines( categoryName:String ):Boolean{
            if( categoryName.contains( "M:" ) || categoryName.contains( "F:") ){
                return true
            }
            return false
        }
        //determine one category has two lines .
    }
}
package com.inspect.ocr

import android.graphics.Rect

class InspectColumn {
    var column:ArrayList<Cell> = ArrayList<Cell>()
    constructor( column:ArrayList<Cell> ){
    }
    constructor(){

    }
    fun setCell( cell:Cell ){
        column.add(cell)
    }
    fun getInspectColumn():ArrayList<Cell>{
        return column
    }
    fun remove( cell:Cell ){
        column.remove( cell )
    }
    fun count():Int{
        return column.size
    }

    // find position in which row it has to be inserted.
    private fun findRowAt( newcell:Cell ):Int{
        for( i in 0 until column.size ){
            var cell = column.get(i)
            if( cell.rect.left > newcell.rect.left ){
                return i
            }
        }
        return 0
    }

    //find position of my column.
    fun findMyColumnPos():Rect{
        var rect: Rect = Rect();
        if( column.size > 0 ){
            var pos:Int = findMinTopCell()
            /*var pos:Int = findMaxTopCell()*/
            /*var pos:Int = findMaxBottomCell()*/
            rect = column.get(pos).rect
        }
        return rect
    }

    //find base point of this column.
    private fun findMinTopCell():Int{
        var pos = 0
        var cell:Cell? = null
        if( column.size == 0 )
            return 0
        for( i in 0 until column.size ){
            var c:Cell = column.get(i)
            if( cell == null ){
                cell = column.get(i)
                continue
            }
            if( cell.rect.top >= c.rect.top ){
                cell = c
                pos = i
            }
        }
        return pos
    }
    //find base point of this column.
    private fun findMaxTopCell():Int{
        var pos = 0
        var cell:Cell? = null
        if( column.size == 0 )
            return 0
        for( i in 0 until column.size ){
            var c:Cell = column.get(i)
            if( cell == null ){
                cell = column.get(i)
                continue
            }
            if( cell.rect.top < c.rect.top ){
                cell = c
                pos = i
            }
        }
        return pos
    }
    //find base point of this column.
    private fun findMaxBottomCell():Int{
        var pos = 0
        var cell:Cell? = null
        if( column.size == 0 )
            return 0
        for( i in 0 until column.size ){
            var c:Cell = column.get(i)
            if( cell == null ){
                cell = column.get(i)
                continue
            }
            if( cell.rect.bottom < c.rect.bottom ){
                cell = c
                pos = i
            }
        }
        return pos
    }
    // insert new cell according to row.
    // determine row in it.
    fun insertCell( newCell:Cell ){
        var pos = findRowAt( newCell )
        var isSimilar:Boolean = false
/*        if( pos < column.size ){
            isSimilar = isTwoElementSimilar( column.get(pos), newCell )
            if( !isSimilar ){
                if( pos > 0 ){
                    pos = pos - 1
                    if( pos >= 0 && pos < column.size ){
                        isSimilar = isTwoElementSimilar( column.get(pos), newCell )
                    }
                }
            }
            if( isSimilar ){
                mergeCell( column.get(pos), newCell )
            }
        }*/
/*        if( !isSimilar ){
            column.add( pos, newCell )
        }*/
        column.add( pos, newCell )
    }
    private fun mergeCell( old:Cell, new:Cell ){

        //extend region of old cell because it has been merged.
        if( old.rect.left > new.rect.left ){
            old.rect.left = new.rect.left
            old.name = new.name + old.name
        }else{
            old.name = old.name + new.name
        }
        if( old.rect.right < new.rect.right ){
            old.rect.right = new.rect.right
        }
        if( old.rect.top > new.rect.top ){
            old.rect.top = new.rect.top
        }
        if( old.rect.bottom < new.rect.bottom ){
            old.rect.bottom = new.rect.bottom
        }
    }
    private fun isTwoElementSimilar( old:Cell, new:Cell ):Boolean{
        var isSimilar:Boolean = false
        var offsetTop:Int = Math.abs( old.rect.top - new.rect.top )
        var offsetBottom:Int = Math.abs( old.rect.bottom - new.rect.bottom )
        var offsetX_1:Int = Math.abs( old.rect.left - new.rect.right )
        var offsetX_2:Int = Math.abs( old.rect.right - new.rect.left )
        var offsetX:Int = if( offsetX_1 > offsetX_2 ) offsetX_2 else offsetX_1
        var height:Int = old.rect.bottom - old.rect.top
        val oldTxt: Double = try { old.name.toDouble() } catch (e: NumberFormatException) { 0.0 }
        var newTxt:Double = try { new.name.toDouble() } catch (e: NumberFormatException) { 0.0 }
        if( offsetTop < InspectOcrPaper.SIMILARINTERVAL
            && offsetX < InspectOcrPaper.SIMILARINTERVAL
            && oldTxt > 0.0 && newTxt > 0.0 ){
            isSimilar = true
        }
        return isSimilar
    }
    fun getCellAt( idx:Int ):Cell{
        return column.get(idx)
    }
    public fun hasTwoLines():Boolean{
        for( i in 0 until column.count() ){
            var categoryName:String = column.get(i).name
            var regX =  Global.TWO_LINES_REGX
            if( regX.containsMatchIn( categoryName ) ){
                return true
/*
                if( categoryName.contains( "M:" ) || categoryName.contains( "F:") ){
                return true
*/
            }
        }
        return false
    }

}
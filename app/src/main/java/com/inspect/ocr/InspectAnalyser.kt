package com.inspect.ocr

import android.graphics.Rect
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class InspectAnalyser() {
    companion object{

        public fun getCellHeight( result:FirebaseVisionText ){
            for (block in result.textBlocks) {
                for (line in block.lines) {
                    for (element in line.elements) {
                        val elementText = element.text
                        val elementConfidence = element.confidence
                        val elementLanguages =
                            element.recognizedLanguages
                        val elementCornerPoints =
                            element.cornerPoints
                        val elementFrame = element.boundingBox
                    }
                }
            }
        }
        public fun adjustColumn( element:FirebaseVisionText.Element ){
            var paper = InspectOcrPaper.getPaperValue()
            InspectOcrPaper.addCellToColumn( element )
        }
        fun processInspectResult(){
            InspectOcrPaper.reArrangePaper()
            CandidateList.findMatchingCandidate()
            CandidateList.setCandidateRows()
            CandidateList.setBarrierCandidateRows()
            CandidateList.setCompensationCandidate()

            for( i in 0 until CandidateList.candidateList.size ){
                var candidate:Candidate = CandidateList.candidateList.get(i)
                findInspectResultByName( candidate )
            }
        }

        //it will be put value into the category.
        private fun findInspectResultByName( candidate:Candidate ){
            var columnPos:Int = candidate.columnPos
            var rowPos:Int = candidate.rowPos
            var startRowPos:Int = rowPos + 1
            var isTwoLinesOCR = InspectOcrPaper.getColumn(columnPos).hasTwoLines()

            if( isTwoLinesOCR /*&& !hasInspectValueInColumn(columnPos)*/ ){
                var fCellList:ArrayList<Cell>? = scanColumnInspect( candidate, columnPos, 0 )
                columnPos += 1
                startRowPos = 0
                var sCellList:ArrayList<Cell>? = scanColumnInspect( candidate, columnPos, 0 )
                if( fCellList != null && sCellList != null ){
                    //var bCellList:ArrayList<Cell> = chooseBetterCellByXAxis( candidate, fCellList, sCellList )
                    merge( fCellList, sCellList )
                    setInspectValue( candidate, fCellList )
                }else if( fCellList == null && sCellList != null ){
                    setInspectValue( candidate, sCellList )
                }else if( sCellList == null && fCellList != null ){
                    setInspectValue( candidate, fCellList )
                }else{
                    setInspectValue( candidate, null )
                }
            }else{
                var valueCellList:ArrayList<Cell>? = scanColumnInspect( candidate, columnPos, 0 )
                if( valueCellList != null ){
                    setInspectValue( candidate, valueCellList )
                }else{
                    setInspectValue( candidate, null )
                }
            }
        }

        private fun merge( fCellList:ArrayList<Cell>, sCellList:ArrayList<Cell> ){
            for( i in 0 until fCellList.size ){
                var fCell:Cell = fCellList.get(i)
                for( j in 0 until sCellList.size ){
                    var sCell:Cell = sCellList.get(j)
                    if( InspectAnalyser.isSameColumnRange( fCell.rect, sCell.rect ) ){
                        fCellList.addAll( sCellList )
                        return
                    }
                }
            }
        }
        private fun chooseBetterCellByXAxis( candidate:Candidate, fCellList:ArrayList<Cell>, sCellList:ArrayList<Cell> ):ArrayList<Cell>{

            var regX =  Global.TWO_LINES_REGX
            var fCell:Cell = fCellList.get(0)
            var sCell:Cell = sCellList.get(0)
            if( regX.containsMatchIn( fCell.name ) ){
                return sCellList
            }
/*            if( fCell.name.toUpperCase().contains("M:") || fCell.name.toUpperCase().contains("S:") ){
                return sCell
            }*/

            var fOffset:Int = Math.abs( fCell.rect.top - candidate.cell.rect.top )
            var sOffset:Int = Math.abs( sCell.rect.top - candidate.cell.rect.top )
            if( sOffset >= fOffset ){
                return fCellList
            }
            return sCellList
        }
        private fun scanColumnInspect( candidate: Candidate, columnPos: Int, startRowPos:Int ):ArrayList<Cell>?{
            var inspectNameLeft:Int = candidate.cell.rect.left
            var inspectNameRight:Int = candidate.cell.rect.right
            var width:Int = inspectNameRight - inspectNameLeft
            var rightBarrier:Candidate? = CandidateList.getBarrierRightByCandidate( candidate ) // it's barrier of not value cell in the same column.
            var barrierRect:Rect? = null
            if( rightBarrier != null )
                barrierRect = rightBarrier.cell.rect

            if( columnPos >= InspectOcrPaper.paper.size ){
                print( "There is no value column for that inspect category." )
                setInspectValue( candidate, null )
                return null
            }
            var ocrInspectColumn:InspectColumn = InspectOcrPaper.paper.get(columnPos)
            if( startRowPos >= ocrInspectColumn.count() ){
                print( "There is no value row for that inspect category." )
                setInspectValue( candidate, null )
                return null
            }

            var vCellList:ArrayList<Cell>? = null
            var curOffset:Int = 1000000
            var searchLeft:Int = 0

            for( i in startRowPos until ocrInspectColumn.count() ){
                var inspectCell:Cell = ocrInspectColumn.getCellAt(i)
                if( searchLeft > 0 && inspectCell.rect.left >= searchLeft ) continue
                if( inspectCell.rect.left <= inspectNameLeft ) continue
                if( isSameInspectRange( candidate.cell.rect, inspectCell.rect ) ) continue
                var regX =  Global.NOT_VALUE_REGX
                if( regX.containsMatchIn( inspectCell.name ) ){
                    searchLeft = inspectCell.rect.left
                    continue
                }
                if( barrierRect != null ){
                    if( inspectCell.rect.left >= barrierRect.left ){
                        continue
                    }
                }

                var offset:Int = inspectCell.rect.left - inspectNameRight
                var txt:String = inspectCell.name
                var value:Int = 0
                var tValue:String = txt
                val regex = Global.REMOVE_NOT_VALUE_REGX
                val match = regex .find( tValue )
                val firstPart:String? = match?.range?.start?.let { tValue.substring(0, it)}
                if( firstPart != null ) {
                    tValue = firstPart
                }
                txt = txt.replace( "[^0-9]".toRegex(), "" )
                txt.trimStart( '0' )
                try {
                    value = txt.toInt()
                }catch(e: NumberFormatException){
                    value = 0
                }
                if(  offset > 0 && offset < curOffset && value > 0 /*&& isSameColumnRange( candidate.cell.rect, inspectCell.rect )*/ ){
                    if( vCellList == null ){
                        vCellList = ArrayList<Cell>()
                    }
                    vCellList.add( inspectCell )
                }
            }
            return vCellList
        }

        private fun setInspectValue( candidate:Candidate, valueList:ArrayList<Cell>? ){
            var inspectValue:String = ""
                if( valueList != null ){
                    //arrangeCellByXAxis( valueList )
                    valueList.sortBy { it.rect.left }
                    for( i in 0 until valueList.size ){
                        var tValue:String = valueList.get(i).name
                        val regex = Global.REMOVE_NOT_VALUE_REGX
                        val match = regex .find( tValue )
                        val firstPart:String? = match?.range?.start?.let { tValue.substring(0, it)}
                        if( firstPart != null ) {
                            tValue = firstPart
                        }
                        tValue = tValue.replace( ",".toRegex(), "." )
                        tValue = tValue.replace( "[^0-9.]".toRegex(), "" )
                        tValue.trimStart( '0' )
                        inspectValue += tValue
                    }
                }else{
                    inspectValue = "0.0"
                }

            val iValue: Double = try { inspectValue.toDouble() } catch (e: NumberFormatException) { 0.0 }
            InspectResult.setInspectValue( candidate.cell.name, iValue.toString() )
        }
/*        private fun arrangeCellByXAxis( valueList:ArrayList<Cell>){
            if( valueList.size == 0 )
                return
            var mCell:Cell = valueList.get(0)
            var pos:Int = 1
            for( i in pos until valueList.size ){
                var cell:Cell = valueList.get(i)
                if( mCell.rect.left > cell.rect.left ){
                    mCell = cell
                    valueList.
                    pos = i
                }
            }
        }*/
        // this function is only for category in two lines meaning first column has label( inspect category name ) and second column has value.
        private fun hasInspectValueInColumn( columnPos:Int ):Boolean{
            var inspectColumn:InspectColumn = InspectOcrPaper.getColumn( columnPos )
            for( i in 0 until inspectColumn.count() ){
                var cell:Cell = inspectColumn.getCellAt(i)
                if( cell.name.toUpperCase().contains("M:") ){
                    return false
                }
            }
            return true
        }

        //ort: ocr cell's rect to be compensated.
        //crt: candidate cell's rect.
        public fun isSameInspectRange( ort:Rect, crt:Rect ):Boolean{

            //ort is bigger than crt.
            if( ort.left >= crt.left && ort.left <= crt.right ){
                return true
            }
            if( ort.right >= crt.left && ort.right <= crt.right ) {
                return true
            }
            if( ort.left <= crt.left && ort.right >=crt.right ){
                return true
            }
            return false
        }
        public fun isSameColumnRange( ort:Rect, crt:Rect ):Boolean{
            //ort is bigger than crt.
            if( ort.top >= crt.top&& ort.top <= crt.bottom ){
                return true
            }
            if( ort.bottom >= crt.top && ort.bottom <= crt.bottom ) {
                return true
            }
            if( ort.top <= crt.top && ort.bottom >=crt.bottom ){
                return true
            }
            return false
        }
        public fun hasInspectLabelInColumn( columnPos:Int ):Boolean{
            var inspectColumn:InspectColumn = InspectOcrPaper.getColumn( columnPos )
            for( i in 0 until inspectColumn.count() ){
                var cell:Cell = inspectColumn.getCellAt(i)

                var regX =  Global.MAN_LINE_IN_TWO
                if( regX.containsMatchIn( cell.name ) ){
                    return true
                }
            }
            return false
        }
        public fun compareToItem( item:Item, _name:String ):Int{
            var point:Int = 0
            var name = Global.removeWhiteSpace( _name )
            if( item.eName.contains( name ) || name.contains( item.eName ) ){
                point += 1;
            }
            if( item.manMaxValue.toString().contains( name ) || name.contains( item.manMaxValue.toString() )  ){
                point += 1;
            }
            if( item.manMinValue.toString().contains( name ) || name.contains( item.manMinValue.toString() )  ){
                point += 1;
            }
            if( item.womanMaxValue.toString().contains( name ) || name.contains( item.womanMaxValue.toString() )  ){
                point += 1;
            }
            if( item.womanMinValue.toString().contains( name ) || name.contains( item.womanMinValue.toString() )  ){
                point += 1;
            }

            return point
        }
    }


}
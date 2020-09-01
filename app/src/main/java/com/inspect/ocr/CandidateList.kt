package com.inspect.ocr

import android.graphics.Rect

class CandidateList {
    companion object {
        var candidateList:ArrayList<Candidate> = ArrayList<Candidate>()
        var candidateRowList:ArrayList<Candidate> = ArrayList<Candidate>()
        var compendationList:ArrayList<Candidate> = ArrayList<Candidate>()
        var barrierRowList:ArrayList<Candidate> = ArrayList<Candidate>()

        public fun initialize(){
            candidateList.clear()
            candidateRowList.clear()
            compendationList.clear()
            barrierRowList.clear()
        }

        public fun add( candidate: Candidate ){
            candidateList.add( findMyPos(candidate.columnPos), candidate )
        }
        private fun findMyPos( columnPos:Int ):Int{
            for( i in 0 until candidateList.size ){
                var candidate:Candidate = candidateList.get(i)
                if( candidate.columnPos > columnPos ){
                    return i
                }
            }
            return candidateList.size
        }

        //if there is no candidate , it has to be notified.
        fun findMatchingCandidate(){
            val fixedPaper = InspectPaper.paper
            //scan column.
            for( i in 0 until InspectOcrPaper.paper.size ){
                var inspectColumn:InspectColumn = InspectOcrPaper.paper.get(i)
                //scan row.
                for( j in 0 until inspectColumn.count() ){
                    var ocrCell:Cell = inspectColumn.getCellAt(j)
                    var ocrName:String = ocrCell.name
                    if( isExistCandidate( Global.removeWhiteSpace(ocrName) ) )
                        continue
                    loop@ for( k in 0 until fixedPaper.size ){
                        var fixedColumn = fixedPaper.get(k)
                        for( l in 0 until fixedColumn.count() ){
                            //compare.
                            var cell = fixedColumn.getCellAt(l)
                            var inspectName:String = cell.name
                            //if ocr cell is the right one .
                            if( Global.removeWhiteSpace(ocrName) ==  Global.removeWhiteSpace(inspectName) ){
                                //ocrCell.isConfident = true
                                var nextNumberCell:Cell? = getNumberCellBehindMe( ocrCell, inspectColumn, inspectName )
                                if( nextNumberCell != null ){
                                    // if inspect english name and japanese name are the same, coordinate has to be moved.
                                    var width = ocrCell.rect.right-ocrCell.rect.left
                                    ocrCell.rect.left = nextNumberCell.rect.left + 5
                                    ocrCell.rect.right = nextNumberCell.rect.left + width
                                }
                                var candidate:Candidate = Candidate( ocrCell, i, j, k, l )
                                CandidateList.add( candidate )
                                break@loop
                            }
                        }
                    }
                }
            }
        }

        fun isExistCandidate( ocrName:String ):Boolean{
            for( i in 0 until candidateList.size ){
                var candidate:Candidate = candidateList.get(i)
                if( ocrName == candidate.cell.name ){
                    return true
                }
            }
            return false
        }

        fun setCompensationCandidate(){
            for( i in 0 until candidateRowList.size ){
                var candidate:Candidate = candidateRowList.get(i)
                //findCompensationCandidate( candidate )
                makeCompensation( candidate )
            }
            for( i in 0 until compendationList.size ){
                var compendation:Candidate = compendationList.get(i)
                var pos:Int = findPosCandidateByName(compendation.cell.name )

                if( pos == -1 ){
                    candidateList.add( compendationList.get(i) )
                }else{
                    //candidateList.get(pos).cell.rect = compendationList.get(i).cell.rect
                    candidateList.get(pos).cell.rect.left = compendationList.get(i).cell.rect.left
                    candidateList.get(pos).cell.rect.right = compendationList.get(i).cell.rect.right
                    //candidateList.get(pos).columnPos = compendationList.get(i).columnPos
                }
            }
        }
        private fun getNumberCellBehindMe( ocrCell:Cell, inspectColumn:InspectColumn, inspectName:String ):Cell?{
            var item:Item? = InspectResult.getItemByInspectName( ocrCell.name )
            var inspectPoint:Double = 0.0
            if( item != null ){
                inspectPoint = item.point.toDouble()
            }
            for( i in 0 until inspectColumn.count() ){
                var cell:Cell = inspectColumn.getCellAt(i)
                var pt:Double = 0.0
                try {
                    pt = cell.name.toDouble()
                }catch(e: NumberFormatException){
                    pt = 0.0
                }
                if( inspectPoint > 0 && inspectPoint == pt ){
                    return cell
                }
/*
                if( pt > 0.0 && cell.rect.left > ocrCell.rect.left ){
                    for( j in (i+1) until inspectColumn.count() ){
                        var tCell:Cell = inspectColumn.getCellAt(j)
                        if( Global.removeWhiteSpace(inspectName).contains( Global.removeWhiteSpace(tCell.name) ) ){
                            return cell
                        }
                    }
                }
*/
            }
            return null
        }

        private fun findCompensation( i:Int, candidate: Candidate, vscolumnPos:Int ):Boolean{
/*            if( isInCandidateListByColumnIndex(vscolumnPos) ){
                return false
            }*/
            var sRowPos:Int = candidate.sRowPos
            var sColumnPos:Int = candidate.sColumnPos
            var inspectColumn:InspectColumn = InspectOcrPaper.getColumn(i)
            var fCell:Cell = inspectColumn.getCellAt(0)

            if( inspectColumn.hasTwoLines() ){
                if( !InspectAnalyser.hasInspectLabelInColumn(i) ){
                    return false
                }
            }

            var flag:Boolean = false
            for( j in 0 until inspectColumn.count() ){
                var cell:Cell = inspectColumn.getCellAt(j)
                if( InspectAnalyser.isSameInspectRange( candidate.cell.rect, cell.rect) ){
                    flag = true
                    var inpectPaperColumn:InspectColumn = InspectPaper.getColumn(vscolumnPos)
                    if( sRowPos >= inpectPaperColumn.count() ){
                        continue
                    }
                    cell.name = InspectPaper.getColumn(vscolumnPos).getCellAt(sRowPos).name
                    cell.rect.left = candidate.cell.rect.left
                    cell.rect.right = candidate.cell.rect.right
                    var compensate:Candidate = Candidate( cell, i, j, vscolumnPos, candidate.sRowPos )
                    compendationList.add( compensate )
                    break
                }
            }
            if( !flag ){
                var inspectPaperColumn:InspectColumn = InspectPaper.getColumn(vscolumnPos)
                if( sRowPos >= inspectPaperColumn.count() ) return true
                var cell:Cell = InspectPaper.getColumn(vscolumnPos).getCellAt(sRowPos)

                cell.rect = Rect( candidate.cell.rect.left, fCell.rect.top, candidate.cell.rect.right, fCell.rect.bottom )
                var compensate:Candidate = Candidate( cell, i, candidate.rowPos, vscolumnPos, candidate.sRowPos )
                compendationList.add( compensate )
            }
            return true
        }
        private fun makeCompensation( candidate:Candidate ){
            var sRowPos:Int = candidate.sRowPos
            var sColumnPos:Int = candidate.sColumnPos
            var vscolumnPos:Int = sColumnPos-1
            for( i in (candidate.columnPos-1) downTo 0 ){
                if( vscolumnPos < 0 ){
                    break
                }
                if( findCompensation( i, candidate, vscolumnPos ) ){
                    vscolumnPos -=1;
                }
            }

            vscolumnPos = sColumnPos + 1
            for( i in (candidate.columnPos+1) until InspectOcrPaper.paper.size ){
                if( vscolumnPos >= InspectPaper.paper.size ){
                    break
                }
                if( findCompensation( i, candidate, vscolumnPos ) ){
                    vscolumnPos +=1;
                }
            }
        }
        private fun findCompensationCandidate( candidate:Candidate ){
            var columnPos:Int = candidate.columnPos
            var rowPos:Int = candidate.rowPos
            var sRowPos:Int = candidate.sRowPos
            var ocrColumnPos:Int = 0
            for( i in 0 until InspectOcrPaper.paper.size ){
                if( i == columnPos ){
                    ocrColumnPos ++;
                    continue
                } // if cell is just itself, don't need to make compensation cell.
                var inspectColumn:InspectColumn = InspectOcrPaper.getColumn(i)

                //register virtual cell because it has to be existed.
                //find label in row to compensate by candidate
//                if( InspectAnalyser.hasInspectLabelInColumn(i ) ){
                    for( j in 0 until inspectColumn.count() ){
                        var cell:Cell = inspectColumn.getCellAt(j)
                        if( cell.isConfident ){
                            ocrColumnPos ++;
                            continue
                        }
                        cell.name = InspectPaper.getColumn( ocrColumnPos ).getCellAt(sRowPos).name
                        if( InspectAnalyser.isSameInspectRange( candidate.cell.rect, cell.rect) ){
                            var compensate:Candidate = Candidate( cell, i, j, ocrColumnPos, candidate.sRowPos )
                            compendationList.add( compensate )
                            ocrColumnPos ++;
                        }
                    }
//                }
            }
        }

        //if OCR has multiple rows for inspect category , this function just adjusts 1 inspect category for the same row
        // so when scanning the same row in different column, that row will be resulted in.
        fun setCandidateRows(){
            if( CandidateList.candidateList.size == 0 )
                return
            for( i in 0 until CandidateList.candidateList.size ){
                var candidate:Candidate = CandidateList.candidateList.get(i)
                var sRowPos:Int = candidate.sRowPos
                var isExist:Boolean = false
                for( j in 0 until CandidateList.candidateRowList.size ){
                    var crowCandidate:Candidate = CandidateList.candidateRowList.get(j)
                    if( crowCandidate.sRowPos == sRowPos ){
                        // if there is a candidate for that row but that row's left position is not suitable , that candidate has to be changed.
                        if( crowCandidate.cell.rect.left < candidate.cell.rect.left ) {
                            crowCandidate.cell.rect.left = candidate.cell.rect.left
                            crowCandidate.cell.rect.right = candidate.cell.rect.right
                        }
                        isExist = true
                        break
                    }
                }
                if( !isExist ){// if no candidate for that row, it has to be added.
                    if( CandidateList.candidateRowList.size <= sRowPos ) {
                        CandidateList.candidateRowList.add(candidate)
                    }
                }
            }
        }

/*        fun setCandidateRows(){
            if( CandidateList.candidateList.size == 0 )
                return

            for( i in 0 until CandidateList.candidateList.size ){
                var candidate:Candidate = CandidateList.candidateList.get(i)
                var sRowPos:Int = candidate.sRowPos
                var isExist:Boolean = false
                for( j in 0 until CandidateList.candidateRowList.size ){
                    var crowCandidate:Candidate = CandidateList.candidateRowList.get(j)
                    if( crowCandidate.sRowPos == sRowPos ){
                        isExist = true
                        break
                    }
                }
                if( !isExist ){
                    CandidateList.candidateRowList.add( candidate )
                }
            }
        }*/

        fun setBarrierCandidateRows(){
            for( i in 0 until candidateRowList.size ){
                var rect:Rect = candidateRowList.get(i).cell.rect
                for( j in 0 until InspectOcrPaper.paper.size ){
                    var ocrInspectColumn:InspectColumn = InspectOcrPaper.getColumn(j)
                    for( k in 0 until ocrInspectColumn.count() ) {
                        var inspectCell: Cell = ocrInspectColumn.getCellAt(k)
                        var regX = Global.NOT_VALUE_REGX
                        if (regX.containsMatchIn(inspectCell.name)) {
                            var rect2:Rect = inspectCell.rect
                            if( rect.left < rect2.left && !InspectAnalyser.isSameInspectRange( rect, rect2 ) ){
                                var candidate:Candidate = Candidate( inspectCell, j, i )
                                setForMostBarrierInRow( i, candidateRowList.get(i), inspectCell )
                            }
                        }
                    }
                }
            }
        }
        private fun setForMostBarrierInRow( index:Int, candidate:Candidate, cell:Cell ){
            if( index >= barrierRowList.size  ){
                barrierRowList.add( Candidate( cell, 0, candidate.sRowPos ) )
            }else{
                var barrier:Candidate = barrierRowList.get(index)
                if( barrier.cell.rect.left > cell.rect.left ){
                    barrierRowList.set( index, Candidate( cell, 0, 0,0, candidate.sRowPos ) )
                }
            }
        }

        fun getBarrierRightByCandidate( candidate:Candidate ):Candidate?{
            for( i in 0 until barrierRowList.size ){
                var barrier:Candidate = barrierRowList.get(i)
                if( candidate.sRowPos == barrier.sRowPos ){
                    return barrier
                }
            }
            return null
        }

        // this function is only used if one category is divided into two lines.
        fun findBestCandidate():Candidate?{
            if( CandidateList.candidateList.size == 0 ) return null;
            var point:Int = 0
            var bestCandidate:Candidate = CandidateList.candidateList.get(0)
            var matchingCount:Int = 0
            if( !InspectOcrPaper.twoLinesOCR ) return bestCandidate

            for( i in 0 until CandidateList.candidateList.size ){
                var candidate:Candidate = CandidateList.candidateList.get(i)
                var item:Item? = InspectResult.getItemByInspectName( candidate.cell.name )
                if( item != null ){
                    var columnPos = candidate.columnPos
                    var inspectColumn:InspectColumn = InspectOcrPaper.getColumn(columnPos)
                    for( j in 0 until inspectColumn.count() ){
                        var cell:Cell = inspectColumn.getCellAt(j)
                        var text:String = cell.name
                        candidate.point +=InspectAnalyser.compareToItem( item, text )
                    }
                }
                if( candidate.point > point ){
                    bestCandidate = candidate
                }
            }
            return bestCandidate
        }
        private fun findPosCandidateByName( name:String ):Int{
            for( i in 0 until candidateList.size ){
                if( name == candidateList.get(i).cell.name )
                    return i
            }
            return -1
        }
        private fun isInCandidateListByColumnIndex( sColumnPos:Int ):Boolean{
            for( i in 0 until candidateList.size ){
                if( sColumnPos == candidateList.get(i).sColumnPos )
                    return true
            }
            return false
        }
        fun isExistCell( name:String ):Boolean{
            for( i in 0 until candidateList.size ){
                var inspectName:String = candidateList.get(i).cell.name
                if( Global.removeWhiteSpace(inspectName) == name ){
                    return true
                }
            }
            return false
        }

        fun findScanByColumn( candidate:Candidate ){
            var columnPos:Int = candidate.columnPos
            var rowPos:Int = candidate.rowPos
       }
        public fun addCandidateRow( candidate:Candidate ){
            if( candidateRowList.size == 0 ){
                candidateRowList.add( candidate )
            }else{
                var pos:Int = findRowPos( candidate )
                if( pos >= 0 ){
                    candidateRowList.add( pos, candidate )
                }
            }
        }

        //
        private fun findRowPos( candidate: Candidate ):Int{
            var pos:Int = -1
            var inspectName:String = candidate.cell.name
            for( j in 0 until InspectPaper.paper.size )
            {
                var column:InspectColumn = InspectPaper.paper.get(j)
                for( k in 0 until column.count() )
                {
                    var tInspectName:String = column.getCellAt(k).name
                    if( Global.removeWhiteSpace(tInspectName).contains( tInspectName ) ){
                        pos = k
                        break
                    }
                }
            }
            return pos
        }

    }
}
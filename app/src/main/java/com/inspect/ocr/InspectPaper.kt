package com.inspect.ocr

class InspectPaper {
    constructor(){
    }
    companion object{
        public val paper:ArrayList<InspectColumn> = ArrayList<InspectColumn>()
        private fun addColumn( txtList:ArrayList<String>){
            var inspectColumn:InspectColumn = InspectColumn()
            for( i in 0 until txtList.size ){
                var cell:Cell = Cell( txtList.get(i))
                inspectColumn.column.add(cell)
            }
            paper.add( paper.size, inspectColumn )
        }
        fun initialize(){
            paper.clear()
            addColumn( arrayListOf( "TP", "WBC", "RPR" ) )
            addColumn( arrayListOf( "A/G", "RBC", "TPHA" ) )
            addColumn( arrayListOf( "ALB", "Hb", "HBs" ) )
            addColumn( arrayListOf( "T-Bil", "Ht", "HCV" ) )
            addColumn( arrayListOf( "D-Bil", "MCV", "判定" ) )
            addColumn( arrayListOf( "I-Bil", "MCH", "カットオフ値" ) )
            addColumn( arrayListOf( "TTT", "MCHC" ) )
            addColumn( arrayListOf( "ZTT", "血小板" ) )
            addColumn( arrayListOf( "GOT", "網状" ) )
            addColumn( arrayListOf( "GPT", "Neutro" ) )
            addColumn( arrayListOf( "ALP", "St" ) )
            addColumn( arrayListOf( "LDH", "Seg" ) )
            addColumn( arrayListOf( "γ-GTP", "Baso" ) )
            addColumn( arrayListOf( "CHE", "Eosino" ) )
            addColumn( arrayListOf( "LAP", "Lympho" ) )
            addColumn( arrayListOf( "CPK", "Mono" ) )
            addColumn( arrayListOf( "AMY" ) )
            addColumn( arrayListOf( "TCH" ) )
            addColumn( arrayListOf( "HDL" ) )
            addColumn( arrayListOf( "LDL", "大小" ) )
            addColumn( arrayListOf( "T-G", "多染" ) )
            addColumn( arrayListOf( "UA", "低色素" ) )
            addColumn( arrayListOf( "BUN" ) )
            addColumn( arrayListOf( "CRE", "蛋白" ) )
            addColumn( arrayListOf( "Na", "糖" ) )
            addColumn( arrayListOf( "Cl", "ウロビリノーゲン" ) )
            addColumn( arrayListOf( "K", "ビリルビン" ) )
            addColumn( arrayListOf( "Ca", "比重" ) )
            addColumn( arrayListOf( "lP", "PH" ) )
            addColumn( arrayListOf( "Fe", "ケトン体" ) )
            addColumn( arrayListOf( "TIBC", "潜血" ) )
            addColumn( arrayListOf( "UIBC", "赤血球" ) )
            addColumn( arrayListOf( "血糖", "白血球" ) )
            addColumn( arrayListOf( "A1c", "扁平上皮" ) )
            addColumn( arrayListOf( "CRP" ) )
            addColumn( arrayListOf( "RF" ) )
            addColumn( arrayListOf( "ASO" ) )
        }
        fun getColumn( idx:Int ):InspectColumn{
            return paper.get(idx)
        }
        fun getCellByName( name:String ):Cell?{
            for( i in 0 until paper.size ){
                for( j in 0 until paper.get(i).count() ){
                    var cell:Cell = paper.get(i).getCellAt(j)
                    if( name == cell.name )
                        return cell
                }
            }
            return null
        }


    }
}
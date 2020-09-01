package com.inspect.ocr
import com.google.gson.Gson;
import kotlin.collections.ArrayList
import com.inspect.ocr.data.Result
import org.json.JSONArray
import org.json.JSONObject
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.util.*

class InspectResult {
    companion object {
        var userId:Int = 0
        var inspectDate:String = ""
        var isChangedDate:Boolean = true
        var resultConfig:ArrayList<Item> = ArrayList<Item>()
        var result:ArrayList<Item> = ArrayList<Item>()
        private fun addColumn( item:Item ){
            result.add(item)
        }
        public fun getDateByFormat():List<String>?{
            var ret:List<String>? = null
            if( inspectDate != "" ){
                ret =  inspectDate.split( "-" )
            }
            return ret
        }
        fun set( _result:Result ){
            inspectDate = _result.inspectDate
            result = _result.inspectResult
        }
        fun setResultConfig( retList: JSONArray ){
            resultConfig.clear()
            for( i in 0 until retList.length() ) {
                var jsonResult: JSONObject = retList.getJSONObject(i)
                insert( jsonResult )
            }
        }
        private fun insert( element: JSONObject ){
            var item:Item = Item( element )
            resultConfig.add( item )
        }

        fun initialize(){
            inspectDate = ""
            isChangedDate = true
            result.clear()
            if( resultConfig.size > 0 ){
                for( i in 0 until  resultConfig.size ){
                    addColumn( resultConfig.get(i) )
                }
            }
/*            addColumn( Item(1, "総蛋白","TP","0.0","6.7", "8.3", "6.7", "8.3", "g/dl" ) )
            addColumn( Item(2, "Ａ／Ｇ比","A/G","0.0","1.3", "2.0", "1.3", "2.0", "g/dl" ) )
            addColumn( Item(3, "アルブミン","ALB","0.0",3.8, 5.3, 3.8, 5.3, "g/dl" ) )
            addColumn( Item(4, "総ビリルビン","T-Bil","0.0",0.20, 1.10, 0.20, 1.10, "g/dl" ) )
            addColumn( Item(5, "直接ビリルビン","D-Bil","0.0",0.00, 0.40, 0.00, 0.40, "g/dl" ) )
            addColumn( Item(6, "間接ビリルビン","I-Bil","0.0",0.20, 0.70, 0.20, 0.70, "g/dl" ) )
            addColumn( Item(7, "ＴＴＴ","TTT","0.0",0.0, 5.0, 0.0, 5.0, "g/dl" ) )
            addColumn( Item(8, "ＺＴＴ","ZTT","0.0",2.0, 12.0, 2.0, 12.0, "g/dl" ) )


            addColumn( Item(9,"GOT/AST","GOT","0.0",10.0,40.0, 10.0, 40.0, "IU/ℓ" ) )
            addColumn( Item(10, "GPT/ALT","GPT","0.0",5.0,45.0, 5.0, 45.0, "IU/ℓ" ) )
            addColumn( Item(11,"ＡＬＰ","ALP","0.0",104.0,338.0, 104.0, 338.0, "IU/ℓ" ) )
            addColumn( Item(12,"ＬＤＨ","LDH","0.0",120.0,240.0, 120.0, 240.0, "IU/ℓ" ) )
            addColumn( Item(13,"γ－ＧＴＰ","γ-GTP","0.0",0.0,70.0, 0.0, 35.0, "IU/ℓ" ) )
            addColumn( Item(14,"コリンエステラーゼ","CHE","0.0",234.0,494.0, 200.0, 452.0, "IU/ℓ" ) )
            addColumn( Item(15,"ＬＡＰ","LAP","0.0",30.0,70.0, 30.0, 70.0, "IU/ℓ" ) )
            addColumn( Item(16,"ＣＰＫ","CPK","0.0",60.0,230.0, 50.0, 190.0, "IU/ℓ" ) )
            addColumn( Item(17,"アミラーゼ","AMY","0.0",37.0,125.0, 37.0, 125.0, "IU/ℓ" ) )
            addColumn( Item(18,"総コレステロール","TCH","0.0",120.0,219.0, 120.0, 219.0, "mg/dl" ) )
            addColumn( Item(19,"ＨＤＬコレステロール","HDL","0.0",40.0,86.0, 40.0, 96.0, "mg/dl" ) )
            addColumn( Item(20,"ＬＤＬコレステロール","LDL","0.0",70.0,139.0, 70.0, 139.0, "mg/dl" ) )
            addColumn( Item(21,"中性脂肪","T-G","0.0",35.0,149.0, 35.0, 149.0, "mg/dl" ) )
            addColumn( Item(22,"尿酸","UA","0.0",3.4,7.0, 2.4, 7.0, "mg/dl" ) )
            addColumn( Item(23,"尿素窒素","BUN","0.0",8.0,23.0, 8.0, 23.0, "mg/dl" ) )
            addColumn( Item(24,"クレアチニン","CRE","0.0",0.61,1.08, 0.45, 0.82, "mg/dl" ) )
            addColumn( Item(25,"Ｎａ","Na","0.0",134.0,147.0, 134.0, 147.0, "mEq/ℓ" ) )
            addColumn( Item(26,"Ｃｌ","Cl","0.0",98.0,108.0, 98.0, 108.0, "mEq/ℓ" ) )
            addColumn( Item(27,"Ｋ","K","0.0",3.4,5.0, 3.4, 5.0, "mEq/ℓ" ) )
            addColumn( Item(28,"Ｃａ","Ca","0.0",8.4,10.4, 8.4, 10.4, "mg/dl" ) )
            addColumn( Item(29,"ｌＰ","lP","0.0",2.5,4.5, 4.5, 6.5, "mg/dl" ) )
            addColumn( Item(30,"Ｆｅ","Fe","0.0",54.0,200.0, 48.0, 154.0, "μg/dl" ) )
            addColumn( Item(31,"TIBC-比色","TIBC","0.0",253.0,365.0, 246.0, 410.0, "μg/dl" ) )
            addColumn( Item(32,"UIBC-比色","UIBC","0.0",104.0,259.0, 108.0, 325.0, "μg/dl" ) )
            addColumn( Item(33,"血糖","血糖","0.0",70.0,109.0, 70.0, 109.0, "mg/dl" ) )
            addColumn( Item(34,"HbA１c","A1c","0.0",4.3,5.8, 4.3, 5.8, "％" ) )
            addColumn( Item(35,"CRP 定性","CRP","-",0.0,0.0, 0.0, 0.0, "mg/dl", false ) )
            addColumn( Item(36,"CRP 定量","CRP","0.0",0.0,0.2, 0.0, 0.2, "mg/dl" ) )
            addColumn( Item(37,"RF  定性","RF","-",0.0,0.0, 0.0, 0.0, "IU/ℓ", false ) )
            addColumn( Item(38,"RF定量","RF","0.0",0.0,20.0, 0.0, 20.0, "IU/ℓ" ) )
            addColumn( Item(39,"ＡＳＯ","ASO","0.0",0.0,210.0, 0.0, 210.0, "IU/ℓ" ) )


            addColumn( Item(40,"白血球数","WBC","0.0",39.0, 98.0, 35.0, 91.0, "10²/μℓ" ) )
            addColumn( Item(41,"赤血球数","RBC","0.0",427.0, 570.0, 376.0, 500.0, "万/μℓ" ) )
            addColumn( Item(42,"血色素量","Hb","0.0",13.5, 17.6, 11.3, 15.2, "g/dl" ) )
            addColumn( Item(43,"ヘマトクリット","Ht","0.0",39.8, 51.8, 33.4, 44.9, "％" ) )
            addColumn( Item(44,"MCV","MCV","0.0",82.7,101.6, 79.0, 100.0, "fl" ) )
            addColumn( Item(45,"MCH","MCH","0.0",28.0,34.6, 26.3, 34.3, "pg" ) )
            addColumn( Item(46,"MCHC","MCHC","0.0",31.6,36.6, 30.7, 36.6, "%" ) )
            addColumn( Item(47,"血小板数","血小板","0.0",12.0,35.0, 12.0, 35.0, "万/μℓ" ) )

            addColumn( Item(48,"網赤血球数","網状","0.0",4.0,19.0, 4.0, 19.0, "‰" ) )
            addColumn( Item(49,"Neutro","N","0.0",42.0,73.0, 42.0, 73.0, "%" ) )
            addColumn( Item(50,"Stab","St","0.0",0.0,6.0, 0.0, 6.0, "%" ) )
            addColumn( Item(51,"Seg","Seg","0.0",36.0,73.0, 36.0, 73.0, "%" ) )
            addColumn( Item(52,"Baso","B","0.0",0.0,2.0, 0.0, 2.0, "%" ) )
            addColumn( Item(53,"Eosino","E","0.0",0.0,6.0, 0.0, 6.0, "%" ) )
            addColumn( Item(54,"Lympho","L","0.0",18.0,59.0, 18.0, 59.0, "%" ) )
            addColumn( Item(55,"Mono","Mno","0.0",0.0,8.0, 0.0, 8.0, "%" ) )
            addColumn( Item(56,"大小・奇形不同","大小","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(57,"多染性","多染","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(58,"低色・EBL素性","低色素","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(59,"蛋白　定性","蛋白","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(60,"蛋白　定量","蛋白","0.0",0.0,10.0, 0.0, 10.0, "" ) )
            addColumn( Item(61,"糖　　定性","糖","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(62,"糖　　定量","糖","0.0",0.0,20.0, 0.0, 20.0, "" ) )
            addColumn( Item(63,"ウロビリノーゲン","ウロビリノーゲン","0.0",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(64,"ビリルビン","ビリルビン","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(65,"比重","比重","0.0",1.002,1.030, 1.002, 1.030, "" ) )
            addColumn( Item(66,"反応(pH)","PH","0.0",5.0,8.0, 5.0, 8.0, "" ) )
            addColumn( Item(67,"ケトン体","ケトン体","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(68,"潜血反応","潜血","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(69,"赤血球","赤血球","0.0",0.0,0.0, 0.0, 0.0, "" ) )
            addColumn( Item(70,"白血球","白血球","0.0",0.0,0.0, 0.0, 0.0, "" ) )
            addColumn( Item(71,"扁平上皮","扁平上皮","0.0",0.0,0.0, 0.0, 0.0, "" ) )

            addColumn( Item(72,"RPRカード法（定性）","RPR","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(73,"TPHA（定性）","TPHA","-",0.0,0.0, 0.0, 0.0, "", false ) )
            addColumn( Item(74,"HBｓ抗原（定性）","HBｓ","0.0",0.0,0.0, 0.0, 0.0, "" ) )
            addColumn( Item(75,"HCV第3世代","HCV","0.0",0.0,0.0, 0.0, 0.0, "" ) )*/
        }

        public fun getAt( i:Int ):Item{
            return result.get(i)
        }
        public fun getItemByInspectName( _eName:String ):Item?{
            for( i in 0 until result.size ){
                var item:Item = result.get(i)
                if( item.eName == _eName ){
                    return item
                }
            }
            return null
        }

        public fun setConfident( ){
            for( i in 0 until result.size ){
                var item:Item = result.get(i)
                if( item.isNumber ){
                    if( item.inspectValue != "0.0" ){
                        item.whoChanged = 1 // this value is correct, no ocr
                    }
                }
            }
        }
        fun setInspectValue( eName:String, value:String, whoChanged:Int = 0 ){
            for( i in 0 until result.size ){
                var item:Item = result.get(i)
                if( Global.removeWhiteSpace(eName) == Global.removeWhiteSpace(item.eName) ){
                    //if patient already changed , then ocr analysis is not needed.
                    if( whoChanged != 1 && item.whoChanged == 1 ){
                        break
                    }
                    if( item.isNumber ){
                        var valueNum:Double = value.toDouble()
                        try {
                            while (valueNum > (item.womanMaxValue.toDouble())) {
                                // if value is too large, that is wrong value, so it has to be divided to get correct value.
                                valueNum = valueNum / 10
                            }
                            var fmt: DecimalFormat = DecimalFormat("0.##");


                            item.inspectValue = fmt.format(valueNum);
                        }catch ( e:NumberFormatException ){
                        }
                    }else{
                        var tempVal:String = value
                        if( whoChanged == 0 && value == "0.0" ){//if value is not double format, it means not number format inspect value.
                            tempVal = item.inspectValue
                        }
                        item.inspectValue = tempVal
                    }
                    item.whoChanged = whoChanged
                    break
                }
            }
        }
        fun resultByJSON():String{
            val gson = Gson()

            var s:String = gson.toJson( result )
            return s
        }
    }
}
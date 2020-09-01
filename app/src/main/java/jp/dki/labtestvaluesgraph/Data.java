package jp.dki.labtestvaluesgraph;

import com.inspect.ocr.Inspect;
import com.inspect.ocr.InspectResult;
import com.inspect.ocr.Item;

import java.util.ArrayList;
import java.util.List;

public class Data {

/*    public static final Data[] items = {
            new Data(1, "総蛋白", 11, "TP", "6.7", "8.3", "6.7", "8.3", "g/dl"),
            new Data(2, "Ａ／Ｇ比", 0, "A/G", "1.3", "2.0", "1.3", "2.0", null),
            new Data(3,"アルブミン",11,"ALB","3.8","5.3","3.8","5.3","g/dl"),
            new Data(4,"総蛋白",11,"TP","6.7","8.3","6.7","8.3","g/dl"),
            new Data(5,"Ａ／Ｇ比",0,"A/G","1.3","2.0","1.3","2.0",null),
            new Data(6,"アルブミン",11,"ALB","3.8","5.3","3.8","5.3","g/dl"),
            new Data(7,"総ビリルビン",11,"T-Bil","0.20","1.10","0.20","1.10","mg/dl"),
            new Data(8,"直接ビリルビン",11,"D-Bil","0.00","0.40","0.00","0.40","mg/dl"),
            new Data(9,"間接ビリルビン",0,"I-Bil","0.20","0.70","0.20","0.70","mg/dl"),
            new Data(10,"TTT",11,"TTT","0.0","5.0","0.0","5.0","KU"),
            new Data(11,"ＺＴＴ",11,"ＺＴＴ","2.0","12.0","2.0","12.0","KU"),
            new Data(12,"GOT/AST",17,"GOT","10","40","10","40","IU/ℓ"),
            new Data(13,"GPT/ALT",17,"GPT","5","45","5","45","IU/ℓ"),
            new Data(14,"ＡＬＰ",11,"ＡＬＰ","104","338","104","338","IU/ℓ"),
            new Data(15,"ＬＤＨ",11,"ＬＤＨ","120","240","120","240","IU/ℓl"),
            new Data(16,"γ－ＧＴＰ",11,"γ-GTP","0","70","0","35","IU/ℓ"),
            new Data(17,"コリンエステラーゼ",11,"CHEl","234","494","200","452","IU/ℓ"),
            new Data(18,"ＬＡＰl",11,"LAPl","30","70","30","70","IU/ℓ"),
            new Data(19,"ＣＰＫl",11,"ＣＰＫl","60","230","50","190","IU/ℓ"),
            new Data(20,"アミラーゼl",11,"AMYl","37","125","37","125","IU/ℓ"),
            new Data(21,"総コレステロール",17,"TCHl","120","219","120","219","mg/dll"),
            new Data(22,"ＨＤＬコレステロールl",17,"HDLl","40","86","40","96","mg/dll"),
            new Data(23,"ＬＤＬコレステロールl",18,"LDLl","70","139","70","139","mg/dll"),
            new Data(24,"中性脂肪l",11,"T-Gl","35","149","35","149","mg/dll"),
            new Data(25,"尿酸l",11,"UAl","3.4","7.0","2.4","7.0","mg/dll"),
            new Data(26,"尿素窒素l",11,"BUNl","8.0","23.0","8.0","23.0","mg/dll"),
            new Data(27,"クレアチニンl",11,"CREl","0.61","1.08","0.45","0.82","mg/dll"),
            new Data(28,"Ｎａl",11,"Ｎａl","134","147","134","147","mEq/ℓl"),
            new Data(29,"Ｃｌl",11,"Ｃｌl","98","108","98","108","mEq/ℓl"),
            new Data(30,"Ｋl",11,"Ｋl","3.4","5.0","3.4","5.0","mEq/ℓl"),
            new Data(31,"Ｃａl",11,"Ｃａl","8.4","10.4","8.4","10.4","mg/dll"),
            new Data(32,"ｌＰl",17,"ｌＰl","2.5","4.5","2.5","4.5","mg/dll"),
            new Data(33,"Ｆｅl",11,"Ｆｅl","54","200","48","154","μg/dll"),
            new Data(34,"TIBC-比色l",11,"TIBC","253","365","246","410","μg/dll"),
            new Data(35,"UIBC-比色l",11,"UIBCl","104","259","108","325","μg/dll"),
            new Data(36,"血糖l",11,"血糖l","70","109","70","109","mg/dll"),
            new Data(37,"HbA１cl",50,"A1cl","4.3","5.8","4.3","5.8","％l"),
            new Data(38,"CRP　定性",16,"CRPl","-","-","-","-", null),
            new Data(39,"CRP　定性",16,"CRPl","0","0.2","0","0.2","mg/dll"),
            new Data(40,"RF 定性",30,"RF","-","-","-","-",null),
            new Data(41,"(RAT)定量",30,"RF","0","20","0","20","IU/ℓl"),
            new Data(42,"ＡＳＯl",15,"ＡＳＯl","0","210","0","210","IU/ℓ"),
            new Data(43,"白血球数l",21,"WBCl","39","98","35","91","10²/μℓl"),
            new Data(44,"赤血球数l",21,"RBCl","427","510","376","500","万/μℓl"),
            new Data(45,"血色素量l",21,"Hb","13.5","17.6","11.3","15.2","g/dll"),
            new Data(46,"ヘマトクリットl",21,"Htl","39.8","51.8","33.4","44.9","％l"),
            new Data(47,"MCVl",21,"MCVl","82.7","101.6","79.0","100.0","fll"),
            new Data(48,"MCHl",21,"MCHl","28.0","34.6","26.3","34.3","pgl"),
            new Data(49,"MCHC",21,null,"31.6","36.6","30.7","36.6","%l"),
            new Data(50,"血小板数l",21,"血小板l","12","35","12","35","万/μℓl"),
            new Data(51,"網赤血球数l",12,"網状l","4","19","4","19","‰l"),
            new Data(52,"Neutrol",18,"N","42","73","42","73","%l"),
            new Data(53,"Stabl",18,"Stl","0","6","0","6","%l"),
            new Data(54,"Segl",18,"Segl","36","73","36","73","%l"),
            new Data(55,"Basol",18,"B","0","2","0","2","%l"),
            new Data(56,"Eosinol",18,"El","0","6","0","6","%l"),
            new Data(57,"Lymphol",18,"Ll","18","59","18","59","%l"),
            new Data(58,"Monol",18,"Mnol","0","8","0","8","%l"),
            new Data(59,"大小・奇形不同",18,"大小l","-","-","-","-",null),
            new Data(60,"多染性l",18,"多染l","-","-","-","-",null),
            new Data(61,"低色・EBL素性",18,"低色素l","-","-","-","-",null),
            new Data(62,"蛋白　定性",7,"蛋白","-","-","-","-",null),
            new Data(63,"蛋白　定量",7,"蛋白","0","10","0","10","mg/dl"),
            new Data(64,"糖　　定性",9,"糖l","-","-","-","-",null),
            new Data(65,"糖　　定量",9,"糖l","0","20","0","20","mg/dl"),
            new Data(66,"ウロビリノーゲンl",0,"ウロビリ\nノーゲン","-","-","-","-",null),
            new Data(67,"ビリルビンl",0,"ビリルビンl","-","-","-","-",null),
            new Data(68,"比重l",0,"比重l","1.002","1.030","1.002","1.030",null),
            new Data(69,"反応(pH)",0,"PHl","5.0","8.0","5.0","8.0",null),
            new Data(70,"ケトン体l",0,"ケトン体l","-","-","-","-",null),
            new Data(71,"潜血反応l",0,"潜血l","-","-","-","-",null),
            new Data(72,"赤血球l",0,"赤血球l",null,null,null,null,null),
            new Data(73,"白血球l",0,"白血球l",null,null,null,null,null),
            new Data(74,"扁平上皮l",0,"扁平上皮l",null,null,null,null,null),
            new Data(75,"RPRカード法（定性）l",15,null,"-","-","-","-",null),
            new Data(76,"TPHA（定性）l",32,"TPHA（定性）l","-","-","-","-",null),
            new Data(77,"HBｓ抗原（定性）l",29,"HBｓ抗原（定性）l",null,null,null,null,null),
            new Data(78,"HCV第3世代l",120,"HCV第3世代l",null,null,null,null,null)
    };*/
    public static Data[] items = null;
    public static String[] itemNames;
    public static void setItems(){
        int size = InspectResult.Companion.getResultConfig().size();
        if( size <= 0 ) return;

        items = new Data[size];

        for( int i = 0; i < InspectResult.Companion.getResultConfig().size(); i ++ ){
            Item item = InspectResult.Companion.getResultConfig().get(i);
            int id = (int)item.getId();
            String inspectName = item.getJName();
            int point = (int) item.getPoint();
            String inspectAliasName = item.getEName();
            String manMinValue = item.getManMinValue();
            String manMaxValue = item.getManMaxValue();
            String womanMinValue = item.getWomanMinValue();
            String womanMaxValue = item.getWomanMaxValue();
            String unit = item.getUnit();

            items[i] = new Data( id, inspectName, point, inspectAliasName, manMinValue, manMaxValue, womanMinValue, womanMaxValue, unit );
        }

        List<String> names = new ArrayList<>();
        for (Data data: items) {
            names.add(data.inspectName);
        }
        itemNames = names.toArray(new String[0]);
    }


    public static Integer getId(String inspectName) {
        for (Data data: items) {
            if (data.inspectName.equals(inspectName)) {
                return data.id;
            }
        }
        return null;
    }

    public static String getUnit(String inspectName) {
        for (Data data: items) {
            if (data.inspectName.equals(inspectName)) {
                return data.unit;
            }
        }
        return null;
    }

    public static Float getMinValue(String inspectName) {
        try {
            for (Data data : items) {
                if (data.inspectName.equals(inspectName)) {
                    Float manMin = Float.valueOf(data.manMinValue);
                    Float womanMin = Float.valueOf(data.womanMinValue);
                    if (manMin > womanMin) {
                        return womanMin;
                    } else {
                        return manMin;
                    }
                }
            }
        } catch (Exception e) {}
        return null;
    }

    public static Float getMaxValue(String inspectName) {
        try {
            for (Data data : items) {
                if (data.inspectName.equals(inspectName)) {
                    Float manMin = Float.valueOf(data.manMaxValue);
                    Float womanMin = Float.valueOf(data.womanMaxValue);
                    if (manMin > womanMin) {
                        return manMin;
                    } else {
                        return womanMin;
                    }
                }
            }
        } catch (Exception e) {}
        return null;
    }

    public final Integer id;
    public final String inspectName;
    public final Integer point;
    public final String inspectAliasName;
    public final String manMinValue;
    public final String manMaxValue;
    public final String womanMinValue;
    public final String womanMaxValue;
    public final String unit;

    public Data(Integer id, String inspectName, Integer point, String inspectAliasName,
                String manMinValue, String manMaxValue, String womanMinValue, String womanMaxValue,
                String unit) {
        this.id = id;
        this.inspectName = inspectName;
        this.point = point;
        this.inspectAliasName = inspectAliasName;
        this.manMinValue = manMinValue;
        this.manMaxValue = manMaxValue;
        this.womanMinValue = womanMinValue;
        this.womanMaxValue = womanMaxValue;
        this.unit = unit;
    }

}

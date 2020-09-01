package com.inspect.ocr

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast


class Global {
    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val USERID = "userid"

        val TWO_LINES_REGX  = """^M:|^F:|^m:|^f:|^M[0-9:~\-]+|^F[0-9:~\-]+|[A-Za-z0-9]?:[0-9.]?""".toRegex()
        val MAN_LINE_IN_TWO = """^M:|^m:|^[Mm]+[0-9:~\-]+""".toRegex()
        val NOT_VALUE_REGX  =  """^M:|^F:|^m:|^F:|[A-Za-z0-9]?[0-9]+[.]?[~\-][0-9]+|^~[A-Za-z0-9]?""".toRegex()
        val REMOVE_NOT_VALUE_REGX      = """F.+|M.+|f.+|m.+""".toRegex()
        private const val BASE_URL = "http://192.168.1.105/";
        //private const val BASE_URL = "http://healthmemo.xsrv.jp/";
        //private const val BASE_URL = "https://wksite.p104.net/";
        const val RESULT_SAVE_ADDRESS = BASE_URL + "index.php/result/save"
        const val LOGIN_ADDRESS = BASE_URL + "index.php/user/login"
        const val RESULT_GET_ADDRESS = BASE_URL + "index.php/result/get"
        const val RESULT_REMOVE_ADDRESS = BASE_URL + "index.php/result/del"
        const val INSPECT_GET_ADDRESS = BASE_URL + "index.php/result/getInspect"

        private const val SERVER_URL = BASE_URL +"receipt/api/"
        const val API_KEY = "8d29430ba38327d91303a935520e7b95"

        // Path of API
        public const val UPLOAD_IMAGE_URL = SERVER_URL + "rest/file_uploader_receipt_image.php"
        public const val RECEIPT_IMAGES_URL = SERVER_URL + "rest/receipt_images.php"
        public const val DELETE_IMAGE_URL =  SERVER_URL + "rest/file_delete_receipt_image.php"

        var RESIZE_WIDTH = 1125
        var RESIZE_HEIGHT = 1500

        const val MEDICINE_OPERATION:Int = 1
        const val INSPECT_OPERATION:Int = 2
        var OPERATION:Int = 0

        const val VIEWCOUNT:Int = 3

        var PAGEVIER_FIRST_LOAD:Int = 0

        public fun isNumber(){

        }
        public fun removeWhiteSpace( str:String ):String{
            return str.replace("\\s".toRegex(), "")
        }
        fun makeLargeTextToast(applicationContext: Context, text: CharSequence): Toast {
            return Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).also {
                val toastLayout = it.view as LinearLayout
                val toastTV = toastLayout.getChildAt(0) as TextView
                toastTV.textSize = 18f
            }
        }
    }
}
package com.inspect.ocr

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.ObjectMapper
import com.inspect.ocr.data.ResultList
import jp.dki.labtestvaluesgraph.JsonPojo
import org.json.JSONArray
import org.json.JSONObject


class Communication {
    companion object{
        fun sendPostRequest(context: Context, success: (Boolean) -> Unit) {
            val request = object :
                StringRequest(
                    Method.POST
                    ,Global.RESULT_SAVE_ADDRESS
                    ,Response.Listener { response ->
                        println("Server response receiver: $response")
                        if( response == "success" ){
                            success(true)
                        }else{
                            success( false )
                        }
                     }
                    ,Response.ErrorListener { error ->
                        Log.d("ERROR", "Server response failure: $error")
                        success(false)
                    }){
                override fun getParams(): Map<String, String>? {

                    val resultStr: String = InspectResult.resultByJSON()
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["user_id"] = Profile.userId.toString()
                    params["inspect_date"] = InspectResult.inspectDate
                    params["result"] = resultStr
                    return params
                }
            }
            Volley.newRequestQueue(context).add(request)
        }
        fun userRequest( context: Context, userId:String, password:String, success: (Int) -> Unit ){
            val request = object :
                StringRequest(
                    Method.POST
                    ,Global.LOGIN_ADDRESS
                    ,Response.Listener { response ->
                        println("Server response receiver: $response")
                        var ret = response.toString()
                        try {
                            val obj = JSONObject( ret )
                            var userIdStr:String = obj["userId"] as String
                            if( obj["status"] == 1 ){
                                Profile.userId = userIdStr.toInt()
                                success(1)
                            }else{
                                success(2)
                            }
                        } catch ( t: Throwable ){
                            success(2)
                        }
                    }
                    ,Response.ErrorListener { error ->
                        Log.d("ERROR", "Server response failure: $error")
                        success(3)
                    }){
                override fun getParams(): Map<String, String>? {

                    val resultStr: String = InspectResult.resultByJSON()
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["user_id"] = userId
                    params["password"] = password
                    return params
                }
            }
            Volley.newRequestQueue(context).add(request)
        }
        fun getInspect( context: Context, success: (Int) -> Unit ){
            val request = object :
                StringRequest(
                    Method.POST
                    ,Global.INSPECT_GET_ADDRESS
                    ,Response.Listener { response ->
                        var ret = response.toString()
                        try {
                            val retList = JSONArray( ret ) as JSONArray
                            InspectResult.setResultConfig( retList )
                            success(1)
                        } catch ( t: Throwable ){
                            success(2)
                        }
                    }
                    ,Response.ErrorListener { error ->
                        Log.d("ERROR", "Server response failure: $error")
                        success(3)
                    }){
            }

            val socketTimeout = 6000 //30 seconds - change to what you want
            val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            request.setShouldCache( true )
            request.retryPolicy = policy
            Volley.newRequestQueue(context).add(request)
        }
        fun removeResultByDate( context: Context, inspectDate:String, success: (Int) -> Unit ){
            val request = object :
                StringRequest(
                    Method.POST
                    ,Global.RESULT_REMOVE_ADDRESS
                    ,Response.Listener { response ->
                        var ret = response.toString()
                        try {
                            val obj = JSONObject( ret )
                            if( obj["status"] == 1 ){
                                success(1)
                            }else{
                                success(2)
                            }
                        } catch ( t: Throwable ){
                            success(2)
                        }
                    }
                    ,Response.ErrorListener { error ->
                        Log.d("ERROR", "Server response failure: $error")
                        success(3)
                    }){
                override fun getParams(): Map<String, String>? {

                    val resultStr: String = InspectResult.resultByJSON()
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["user_id"] = Profile.userId.toString()
                    params["inspect_date"] = inspectDate
                    return params
                }
            }

            val socketTimeout = 60000 //30 seconds - change to what you want

            val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            request.retryPolicy = policy
            Volley.newRequestQueue(context).add(request)
        }
        fun getResult(  context: Context, userId:String, success: (Int) -> Unit ){
            val request = object :
                StringRequest(
                    Method.POST
                    ,Global.RESULT_GET_ADDRESS
                    ,Response.Listener { response ->
                        var ret = response.toString()
                        try {
                            val retList = JSONArray( ret ) as JSONArray
                            ResultList.parseResult( retList )
                            success(1)
                        } catch ( t: Throwable ){
                            success(2)
                        }
                    }
                    ,Response.ErrorListener { error ->
                        Log.d("ERROR", "Server response failure: $error")
                        success(3)
                    }){
                override fun getParams(): Map<String, String>? {

                    val resultStr: String = InspectResult.resultByJSON()
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["userId"] = userId
                    return params
                }
            }

            val socketTimeout = 60000 //30 seconds - change to what you want

            val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            request.retryPolicy = policy
            Volley.newRequestQueue(context).add(request)
        }

        fun getGraphData(  context: Context, userId:String, inspectId:String, success: (Int) -> Unit ){
            val request = object :
                StringRequest(
                    Method.POST
                    ,Global.RESULT_GET_ADDRESS
                    ,Response.Listener { response ->
                        var ret = response.toString()
                        try {
                            if( ret != null ){
                                val mapper = ObjectMapper()
                                var pojo: Array<JsonPojo>? =
                                    mapper.readValue(
                                        ret, Array<JsonPojo>::class.java
                                    )
                                success(1)
                            }
                        } catch ( t: Throwable ){
                            success(2)
                        }
                    }
                    ,Response.ErrorListener { error ->
                        Log.d("ERROR", "Server response failure: $error")
                        success(3)
                    }){
                override fun getParams(): Map<String, String>? {

                    val resultStr: String = InspectResult.resultByJSON()
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["userId"] = userId
                    return params
                }
            }

            val socketTimeout = 60000 //30 seconds - change to what you want

            val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            request.retryPolicy = policy
            Volley.newRequestQueue(context).add(request)
        }
    }
}
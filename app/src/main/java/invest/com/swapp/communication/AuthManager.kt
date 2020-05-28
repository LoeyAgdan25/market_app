package invest.com.swapp.communication

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.lang.Exception

class AuthManager(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext,workerParameters) {

    private lateinit var client: OkHttpClient

    override suspend fun doWork(): Result = coroutineScope {

        val requestType = inputData.getString("requestType")
        val userName = inputData.getString("username")
        val password = inputData.getString("password")
        var responseBody = ""

        var postData = JSONObject()
        var postRegister = JSONObject()

            try {
                postData.put("email",userName)
                postData.put("password",password)
            }catch (e:Exception){
                e.printStackTrace()
            }


            try {
                postRegister.put("username",userName)
                postRegister.put("email",userName)
                postRegister.put("password",password)
            }catch (e:Exception){
                e.printStackTrace()
            }
        //TODO:- validate sql injection
        client = OkHttpClient()
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(),postData.toString())
        Log.d("_Request","{'email':'$userName','password':'$password'}")
        var path4 = "login.php"
                if(requestType.equals("register")){
                        path4 = "register.php"
                    }

        val urlRequest = Uri.Builder().scheme(AuthConstant.URL_SCHEME)
                .authority(AuthConstant.URL_AUTHORITY)
                .appendPath(AuthConstant.URL_PATH_1)
                .appendPath(AuthConstant.URL_PATH_2)
                .appendPath(AuthConstant.URL_PATH_3)
                .appendPath(path4)
                .build().toString()

        val loginRequest = Request.Builder().url(urlRequest).post(requestBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()
        var output: Data = Data.EMPTY
        return@coroutineScope try {

                var response = client.newCall(loginRequest).execute()

                responseBody = if(response.isSuccessful){
                    response.body!!.string()
                }else{
                    "empty"
                }

            var j = JSONObject(responseBody)
            //output = workDataOf("RESPONSE" to " SUCCESS -> ${j.getString("result")}")
            //${j.getString("jwt")}
            output = workDataOf("RESPONSE" to " SUCCESS -> ${j.getString("message")} ")
            Log.d("_RESPONSE",responseBody)
            Log.d("_URL", urlRequest.toString())

            if(response.code == 200){

                /*{
                    "message": "Successful login.",
                    "jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOm51bGwsImF1ZCI6IlBvc3RtYW5SdW50aW1lXC83LjI0LjEiLCJpYXQiOjE1ODkzODc1NjEsIm5iZiI6MTU4OTM4NzU3MSwiZXhwIjoxNTg5MzkzNTYxLCJkYXRhIjp7ImlkIjoiMjAiLCJ1aWQiOiI1ZWIyYzZlMWU5MDMwIiwiZmlyc3RuYW1lIjpudWxsLCJsYXN0bmFtZSI6bnVsbCwidXNlcm5hbWUiOiJsb2V5X2FnZGFuIiwiZW1haWwiOiJsb2V5YWdkYW4yNUBnbWFpbC5jb20ifX0.f_0oeukPcOUvewfvDsfzcBUCHnjAviFMd646UII_M7w",
                    "email": "loeyagdan25@gmail.com",
                    "expireAt": 1589393561
                }*/
                //save to database
                output = workDataOf("RESPONSE" to "${j.getString("message")}")

            }

            if(response.code == 401){
                output = workDataOf("RESPONSE" to " LOGIN FAILED -> ")
            }

            Result.success(output)

        }catch(e:Exception){
            e.printStackTrace()
            Result.failure(workDataOf("exception" to e.localizedMessage,"response" to responseBody))

        }
    }
}

class AuthConstant{
    companion object {
        val URL_SCHEME = "http"
        val URL_AUTHORITY = "www.codezzila.com"
        val URL_PATH_1 = "projects"
        val URL_PATH_2 = "vest"
        val URL_PATH_3 = "request"
        val URL_PATH_4 = "login.php"
    }
}


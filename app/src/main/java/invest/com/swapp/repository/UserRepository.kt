package invest.com.swapp.repository

import android.app.Application
import android.content.Context
import android.util.Log
import invest.com.swapp.api.ApiInterface
import invest.com.swapp.api.RetrofitInstance
import invest.com.swapp.api.SignInBody
import invest.com.swapp.api.UserBody
import kotlinx.coroutines.*
import org.json.JSONObject

class UserRepository(app: Application){

    val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
    val app = app.getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE)

    suspend fun login(user: SignInBody):String{
        var resBody = ""
        val value = GlobalScope.async {
            var resp = retIn.signin(user).execute()
            if(resp.isSuccessful){
                resBody = resp.body()!!.string()

                Log.d("_login", resBody)

                var loginResponse = JSONObject(resBody)
                    val jwt = loginResponse.getString("jwt")
                    val email = loginResponse.getString("email")
                    val expireAt = loginResponse.getInt("expireAt")

                    Log.d("_login", "$jwt $email $expireAt")
                    with (app.edit()) {
                        putString("jwt",jwt)
                        putString("email",email)
                        putInt("expireAt",expireAt)
                        commit()
                    }

                resBody = loginResponse.getString("message")
            }
        }
        value.await()
        return resBody
    }

    suspend fun signup(user: UserBody):String{
        var result = ""
        //val process = GlobalScope.async {
            var resp = retIn.register(user).execute()
            if(resp.isSuccessful){
                result = resp.body()!!.string()
            }
        //}
        //process.wait()

        Log.d("_signupResult", result)
        return result
    }

    fun isCredentialValid():Boolean{
        val jwt = app.getString("jwt","")
        Log.d("__jwt", jwt)
        if(jwt.isNullOrEmpty()){
            return false
        }
        return true
    }

    fun logout(){
        with(app.edit()){
            clear()
            commit()
        }

        //todo:clear database...
    }

}
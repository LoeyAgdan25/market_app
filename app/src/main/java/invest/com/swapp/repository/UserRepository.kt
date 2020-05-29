package invest.com.swapp.repository

import invest.com.swapp.api.ApiInterface
import invest.com.swapp.api.RetrofitInstance
import invest.com.swapp.api.SignInBody
import invest.com.swapp.api.UserBody
import kotlinx.coroutines.*
import okhttp3.internal.wait

class UserRepository{
    val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
    suspend fun login(user: SignInBody):String{
        var resBody = ""
        val value = GlobalScope.async {
            var resp = retIn.signin(user).execute()
            if(resp.isSuccessful){
                resBody = resp.body()!!.string()
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
        return result
    }
}
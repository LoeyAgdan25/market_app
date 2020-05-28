package invest.com.swapp.api

import invest.com.swapp.model.HistoryData
import invest.com.swapp.model.StocksResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ApiInterface {

    @Headers("Content-Type:application/json")
    @POST("auth/login.php")
    fun signin(@Body info: SignInBody): retrofit2.Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("auth/register.php")
    fun register(@Body info: UserBody):retrofit2.Call<ResponseBody>

    //todo:- convert to response body object, do an experiment later
    @Headers("Content-Type:application/json","Cache-Control:no-cache")
    @GET("data/getstocks.php")
    fun getAllStocks():retrofit2.Call<ResponseBody>

    @Headers("Content-Type:application/json","Cache-Control:no-cache")
    @GET("data/gethistory.php")
    fun getHistoryData(@Query("sid") sid: Int?, @Query("cid") cid: Int?):retrofit2.Call<ResponseBody>
}

    data class UserBody(val username:String,
                    val email: String,
                    val password: String)

    data class SignInBody(val email: String, val password: String)


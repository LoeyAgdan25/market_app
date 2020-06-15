package invest.com.swapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import invest.com.swapp.api.SignInBody
import invest.com.swapp.api.UserBody
import invest.com.swapp.repository.UserRepository

class AuthViewModel(application: Application): AndroidViewModel(application){

    var response: MutableLiveData<String> = MutableLiveData()
    val userRepository = UserRepository(application)


    suspend fun login(username:String, password:String){
        var result = userRepository.login(SignInBody(username,password))
        if(result != null){
            response.postValue(result)
        }else{
            response.postValue(result)
        }
    }

    suspend fun isSessionValid(): Boolean{
       return userRepository.isCredentialValid()
    }

    suspend fun register(username:String, email: String, password: String){
        var result = userRepository.signup(UserBody(username,email,password))
        if(result != null){
            response.postValue(result)
        }else{
            response.postValue(result)
        }
    }

    fun logout(){
        userRepository.logout()
    }

}
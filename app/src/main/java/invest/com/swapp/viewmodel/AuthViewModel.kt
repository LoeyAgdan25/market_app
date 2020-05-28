package invest.com.swapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import invest.com.swapp.api.SignInBody
import invest.com.swapp.api.UserBody
import invest.com.swapp.repository.UserRepository

class AuthViewModel:ViewModel(){

    var response: MutableLiveData<String> = MutableLiveData()

    suspend fun login(username:String, password:String){
        var result = UserRepository().login(SignInBody(username,password))
        if(result != null){
            response.postValue(result)
        }else{
            response.postValue(result)
        }
    }

    suspend fun register(username:String, email: String, password: String){
        var result = UserRepository().signup(UserBody(username,email,password))
        if(result != null){
            response.postValue(result)
        }else{
            response.postValue(result)
        }
    }

}
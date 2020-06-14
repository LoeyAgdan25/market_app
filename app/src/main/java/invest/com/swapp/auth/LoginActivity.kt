package invest.com.swapp.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.*
import invest.com.swapp.*
import invest.com.swapp.R
import invest.com.swapp.helper.ConnectivityManager
import invest.com.swapp.security.SSharedPreferenceManager
import invest.com.swapp.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import org.json.JSONObject

class LoginActivity : AppCompatActivity(){

    private var connectivityManager: ConnectivityManager = ConnectivityManager()
    private lateinit var authViewModel:AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        App.context = applicationContext

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        authViewModel.response.observe(this@LoginActivity, Observer {



            if(it.contains("Successful")){
                var intent = Intent(this,MasterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }else{
                toast("Authentication error, please check your credential")
                txt_username.text!!.clear()
                password.text!!.clear()
                field_layout_container.visibility = View.VISIBLE
                linear_progress_circular.visibility = View.INVISIBLE
            }
        })

        email_sign_in_button.setOnClickListener {

            if(connectivityManager.isConnectingToInternet(this)) {
                    if (txt_username.text!!.isEmpty()) {
                        txt_username_layout.error = "Email is required"
                        }

                    if (password.text!!.isEmpty()){
                        password_layout.error = "Password is required"
                        }

                    if(password.text!!.length < 8 || password.text!!.contains(" ")){
                        password_layout.error = "A valid password is required"
                    }

                    if(txt_username.text!!.isNotEmpty() && password.text!!.isNotEmpty() && password.text!!.length >= 8 && !password.text!!.contains(" ")){

                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(it.windowToken, 0)
                        field_layout_container.visibility = View.INVISIBLE
                        linear_progress_circular.visibility = View.VISIBLE
                        doLogin()
                    }
            }else{
                toast("Please check internet connectivity")
            }
        }

        txt_username.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {    txt_username_layout.error = null      }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        password.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {password_layout.error = null}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        btn_open_privacy.setOnClickListener { doOpenPrivacyPolicy() }
        email_sign_up_button.setOnClickListener { attemptSignup()}
        forgot_password_in_button.setOnClickListener { attemptForgotPassword() }
    }

    private fun doLogin(){
        GlobalScope.launch { authViewModel.login(txt_username.text.toString(), password.text.toString())}
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //do nothing
    }

    override fun onResume() {
        super.onResume()
        if(authViewModel.userRepository.isCredentialValid()){
            var i = Intent(this, MasterActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
    }

    private fun attemptSignup(){
        val intent = Intent(baseContext, SignupActivity::class.java)
        startActivity(intent)
    }

    fun attemptForgotPassword(){}

    fun doOpenPrivacyPolicy(){
        val url = "http://3.17.23.239/swapp-privacy-policy/"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

}

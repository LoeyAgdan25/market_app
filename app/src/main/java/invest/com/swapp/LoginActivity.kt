package invest.com.swapp


import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.regions.Regions
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity(){

    private var cUsername:String = ""
    private var cPassword:String = ""
    private var userPool: CognitoUserPool? = null
    private var forgotPasswordContinuation: ForgotPasswordContinuation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        userPool = CognitoUserPool(baseContext,
                UtilityHelper.CognitoUserPool.USERPOOL_ID,
                UtilityHelper.CognitoUserPool.CLIENT_ID,
                UtilityHelper.CognitoUserPool.CLIENT_SECRET,
                Regions.US_EAST_2)

        email_sign_in_button.setOnClickListener { attemptLogin() }
        email_sign_up_button.setOnClickListener { attemptSignup()}
        forgot_password_in_button.setOnClickListener { attemptForgotPassword() }
        this.supportActionBar!!.hide()

        AppHelper.init(baseContext)
        findCurrent()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            1 -> // Register user
                if(resultCode == Activity.RESULT_OK){
                    var name = data!!.getStringExtra("name")
                    if(!name.isEmpty()){
                        txt_username.setText(name)
                        password.setText("")
                        password.requestFocus()
                    }

                    var pwd = data.getStringExtra("password")
                    if(!pwd.isEmpty()){
                        password.setText(pwd)
                    }

                    if(!name.isEmpty() && !pwd.isEmpty()){
                        cUsername = name
                        cPassword = pwd
                        AppHelper.userPool!!.getUser(cUsername).getSessionInBackground(authHandler)
                    }
                }
            2 -> // Confirm register user
                if(resultCode == Activity.RESULT_OK){

                }
            3 -> //Forgot Password
                if(resultCode == Activity.RESULT_OK){
                    var newPass = data!!.getStringExtra("newPass")
                    var code = data!!.getStringExtra("code")
                    if(newPass != null && code != null){
                        if(!newPass.isEmpty() && !code.isEmpty()){
                            forgotPasswordContinuation!!.setPassword(newPass)
                            forgotPasswordContinuation!!.setVerificationCode(code)
                            forgotPasswordContinuation!!.continueTask()
                        }
                    }
                }
            4 -> //Main
                if(resultCode == Activity.RESULT_OK){
                    if(txt_username == null){

                    }

                    txt_username.setText("")
                    txt_username.requestFocus()
                    password.setText("")
                    password.requestFocus()
                }
            else -> print("not")
        }

    }

    private fun attemptLogin() {
        cUsername = txt_username.text.toString()
//        if (cUsername.isEmpty()) { return }

        cPassword = password.text.toString()
//        if (cPassword.isEmpty()){ return }

        Log.d("signin","trigger")

        userPool!!.getUser(cUsername).getSessionInBackground(authHandler)
    }

    var authHandler = object: AuthenticationHandler{
        override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
            AppHelper.currSession = userSession
            AppHelper.newDevice = newDevice

            //dismiss dialog

            val intent = Intent(baseContext, MasterActivity::class.java)
            startActivityForResult(intent, 4)

            Log.d("signin","success signing in")
        }

        override fun onFailure(exception: Exception?) {
            Log.d("signin","error signing in")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation?) {

        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
            getUserAuthentication(authenticationContinuation!!,cUsername)
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {

        }
    }

    var forgotPwdHandler = object: ForgotPasswordHandler{
        override fun onSuccess() {
            Log.d("_forgot_password","success");
        }

        override fun onFailure(exception: Exception?) {
            Log.d("_forgot_password","exception failed ${exception}"  )
        }

        override fun getResetCode(continuation: ForgotPasswordContinuation?) {
            getForgotPasswordCode(continuation!!)
        }

    }

    fun getUserAuthentication(authenticationContinuation: AuthenticationContinuation,username:String){
        Log.d("signin","Get authentication...")
        var authDetails = AuthenticationDetails(username,cPassword,null)
        authenticationContinuation.setAuthenticationDetails(authDetails)
        authenticationContinuation.continueTask()
    }

    private fun attemptSignup(){
        val intent = Intent(baseContext, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun findCurrent(){
        var user = AppHelper.userPool!!.currentUser
        if (user.userId == null) return
            cUsername = user.userId
        if(cUsername != null){
            AppHelper.user = cUsername
            txt_username.setText(user.userId)
            user.getSessionInBackground(authHandler)
        }
    }

    private fun getForgotPasswordCode(forgotPasswordContinuation: ForgotPasswordContinuation){
        this.forgotPasswordContinuation = forgotPasswordContinuation
        var intent = Intent(baseContext, ForgotPasswordActivity::class.java)
        intent.putExtra("destination",forgotPasswordContinuation.parameters.destination)
        intent.putExtra("deliveryMed",forgotPasswordContinuation.parameters.deliveryMedium)
        startActivityForResult(intent,3)

    }

    private fun attemptForgotPassword(){
        cUsername = txt_username.text.toString()
        if(cUsername.isEmpty()){
            return
        }

        AppHelper.userPool!!.getUser(cUsername).forgotPasswordInBackground(forgotPwdHandler) //add handler
    }

}

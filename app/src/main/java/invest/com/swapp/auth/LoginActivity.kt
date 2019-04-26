package invest.com.swapp.auth


import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.regions.Regions
import invest.com.swapp.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.contentView
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast
import java.lang.Exception

//TODO:- Fix indeterminate progress to dismiss...

class LoginActivity : AppCompatActivity(){

    private var cUsername:String = ""
    private var cPassword:String = ""
    private var userPool: CognitoUserPool? = null
    private var forgotPasswordContinuation: ForgotPasswordContinuation? = null
    private var connectivityManager: ConnectivityManager = ConnectivityManager()
    var indeterminateP: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userPool = CognitoUserPool(baseContext,
                UtilityHelper.CognitoUserPool.USERPOOL_ID,
                UtilityHelper.CognitoUserPool.CLIENT_ID,
                UtilityHelper.CognitoUserPool.CLIENT_SECRET,
                Regions.US_EAST_2)

        email_sign_in_button.setOnClickListener {
            if(connectivityManager.isConnectingToInternet(this)) {
                indeterminateP = indeterminateProgressDialog("Please wait... ", "Signing in")
                attemptLogin()
            }else{
                alert{
                    title("No Internet")
                    message("Please check internet connection")
                    positiveButton("Ok"){
                        //do nothing
                    }
                }.show()
            }
        }

        btn_open_privacy.setOnClickListener { doOpenPrivacyPolicy() }
        email_sign_up_button.setOnClickListener { attemptSignup()}
        forgot_password_in_button.setOnClickListener { attemptForgotPassword() }
        this.supportActionBar!!.hide()

        AppHelper.init(baseContext)
        findCurrent()

    }


    fun doOpenPrivacyPolicy(){
        val url = "http://3.17.23.239/swapp-privacy-policy/"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
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
        indeterminateP!!.show()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow( contentView!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


        cUsername = txt_username.text.toString()
        if (cUsername.isEmpty()) {
            alert("Email required","").show()
            indeterminateP!!.dismiss()
            return }

        cPassword = password.text.toString()
        if (cPassword.isEmpty()){
            alert("Password required",""){
                positiveButton("Ok",{
//                    toast("do this!")
                })
            }.show()
            indeterminateP!!.dismiss()
            return }

        userPool!!.getUser(cUsername).getSessionInBackground(authHandler)


    }

    var authHandler = object: AuthenticationHandler{
        override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
            AppHelper.currSession = userSession
            AppHelper.newDevice = newDevice

            indeterminateP!!.dismiss()
            val intent = Intent(baseContext, MasterActivity::class.java)
            startActivityForResult(intent, 4)
            finish()
        }

        override fun onFailure(exception: Exception?) {

            if(!password.text.toString().isEmpty()){
                var error = ""
                if("${exception}".contains("Unable to execute HTTP", ignoreCase = true)){
                    error = "Your internet might be slow at this time."
                }else{
                    error = "There is an error occured."
                }
                indeterminateP!!.dismiss()
                alert{
                    title("Error")
                    message("${error}")
                    positiveButton("Ok",{})
                }.show()
            }

            //
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
            alert{
                message("Email is required")
                positiveButton("Ok"){

                }
            }.show()
            return
        }else {
            alert{
                message("Verification code will be sent on your email")
                title("Forgot Password")
                positiveButton("Continue"){
                    AppHelper.userPool!!.getUser(cUsername).forgotPasswordInBackground(forgotPwdHandler) //add handler
                }
                negativeButton("Cancel"){
                    //do nothing
                }
            }.show()
        }
    }

}

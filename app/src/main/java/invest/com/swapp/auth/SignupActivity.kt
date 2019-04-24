package invest.com.swapp.auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.content_signup.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import java.lang.Exception
import android.content.Intent
import android.provider.ContactsContract
import invest.com.swapp.AppHelper
import invest.com.swapp.MasterActivity
import invest.com.swapp.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager
import org.jetbrains.anko.contentView
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private val PASSWORD_POLICY = """Password should be minimum 8 characters long,
            |at least one number""".trimMargin()

    var indeterminateP:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)
        this.supportActionBar!!.title = "Sign Up"
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        AppHelper.init(baseContext)
        btn_signup.setOnClickListener{
            if(validateEmail(txt_email.text.toString()) && validatePassword(txt_password_2.text.toString())){
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow( contentView!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                indeterminateP = indeterminateProgressDialog("Please wait... ", "Sign up")
                doSignUpTapped()
            }
        }
    }



    private fun doSignUpTapped(){
        var cognitoUserAttr = CognitoUserAttributes()
        cognitoUserAttr.addAttribute("email",txt_email.text.toString())
        cognitoUserAttr.addAttribute("profile","FREE")
        //this.userPool!!.signUpInBackground(txt_email.text.toString(),txt_password_1.text.toString(),cognitoUserAttr,null,handler)

        AppHelper.userPool!!.signUpInBackground(txt_email.text.toString(),txt_password_2.text.toString(),cognitoUserAttr,null,handler)
        indeterminateP!!.show()
    }

    /**
     *  Credential validation
     *
     * */

    private fun validateEmail(email: String): Boolean {
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()){
            return true
        }
            alert("Invalid Email Address",""){
                positiveButton("Ok",{})
            }.show()
            return false
    }

    private fun validatePassword(password: String, updateUI: Boolean = true): Boolean{

        val str = password
        var valid = true

        if (str.length < 8) {
            valid = false
        }

        var exp = ".*[0-9].*"
        var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
        var matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        if (updateUI) {
            val error: String? = if (valid) null else PASSWORD_POLICY
            if(error != null){
                alert(error!!,""){
                    positiveButton("Ok",{})
                }.show()
            }
        }

        return valid
    }

    /**
     *  AWS Handler...
     *
     * */

    val handler = object: SignUpHandler{
        override fun onSuccess(user: CognitoUser?, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails?) {
            indeterminateP!!.dismiss()
            val intent = Intent(baseContext, MasterActivity::class.java)
            intent.putExtra("email", txt_email.text.toString())
            startActivity(intent)

        }

        override fun onFailure(exception: Exception?) {
            indeterminateP!!.dismiss()
            var error = ""
            if(exception.toString().contains("UsernameExist", ignoreCase = true)){
                error = "User already exist"
            }

            alert("${error}","Error"){
                positiveButton("Ok",{

                })
            }.show()
        }
    }

}



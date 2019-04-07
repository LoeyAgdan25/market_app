package invest.com.swapp.auth

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
import invest.com.swapp.AppHelper
import invest.com.swapp.R
import org.jetbrains.anko.toast

class SignupActivity : AppCompatActivity() {

    var userPool:CognitoUserPool? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)
        this.supportActionBar!!.title = "Sign Up"
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        AppHelper.init(baseContext)
        btn_signup.setOnClickListener{ doSignUpTapped() }



    }



    private fun doSignUpTapped(){
        var cognitoUserAttr = CognitoUserAttributes()
        cognitoUserAttr.addAttribute("email",txt_email.text.toString())
        cognitoUserAttr.addAttribute("profile","FREE")
        //this.userPool!!.signUpInBackground(txt_email.text.toString(),txt_password_1.text.toString(),cognitoUserAttr,null,handler)

        AppHelper.userPool!!.signUpInBackground(txt_email.text.toString(),txt_password_1.text.toString(),cognitoUserAttr,null,handler)
    }



    /**
     *  AWS Handler...
     *
     * */

    val handler = object: SignUpHandler{
        override fun onSuccess(user: CognitoUser?, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails?) {
            //Log.d("_login","User successfully signed , signUpConfirmationState : " + signUpConfirmationState )
            toast("User signup waiting for confirmation...")

            val intent = Intent(baseContext, ConfirmationActivity::class.java)
            intent.putExtra("email", txt_email.text.toString())
            startActivity(intent)

        }

        override fun onFailure(exception: Exception?) {
            Log.d("_login","Error user signed in" + exception.toString())
            toast(exception.toString())
        }
    }

}



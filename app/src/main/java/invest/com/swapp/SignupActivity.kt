package invest.com.swapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.content_signup.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import java.lang.Exception
import AppController

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        btn_signup.setOnClickListener{ doSignUpTapped() }

    }

    private fun doSignUpTapped(){

        Log.d("Signup","doing signup...")

        val app:AppController = AppController()


        var userPoolCognito = app.getUserPool()

        

        var cognitoUserAttr = CognitoUserAttributes()
        cognitoUserAttr.addAttribute("email",txt_email.text.toString())
        cognitoUserAttr.addAttribute("profile","FREE")
        userPoolCognito?.signUpInBackground(txt_email.text.toString(),txt_password_1.text.toString(),cognitoUserAttr,null,handler )

    }



    /**
     *  AWS Handler...
     *
     * */

    val handler = object: SignUpHandler{
        override fun onSuccess(user: CognitoUser?, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails?) {
            Log.d("_login","User successfully signed , signUpConfirmationState : " + signUpConfirmationState )
            //TODO:- Open confirmation code...


        }

        override fun onFailure(exception: Exception?) {
            Log.d("_login","Error user signed in" + exception.toString())
        }
    }

}



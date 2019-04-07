package invest.com.swapp.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.regions.Regions
import invest.com.swapp.R

import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.content_confirmation.*
import org.jetbrains.anko.toast
import java.lang.Exception

class ConfirmationActivity : AppCompatActivity() {

    var userPool: CognitoUserPool? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        setSupportActionBar(toolbar)


        userPool = CognitoUserPool(baseContext,
                UtilityHelper.CognitoUserPool.USERPOOL_ID,
                UtilityHelper.CognitoUserPool.CLIENT_ID,
                UtilityHelper.CognitoUserPool.CLIENT_SECRET,
                Regions.US_EAST_2)


        txt_email_confirm.setText(intent.getStringExtra("email").toString())

        btn_confirm.setOnClickListener { confirmCodeTqpped()  }


    }

    private fun confirmCodeTqpped(){

        userPool!!.getUser(txt_email_confirm.text.toString()).confirmSignUpInBackground(txt_code_confirm.text.toString(),true,handler)

    }

    val handler = object:GenericHandler{
        override fun onSuccess() {
            Log.d("_login","confirmation success" )
            toast("User successfully confirm")
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
        }


        override fun onFailure(exception: Exception?) { //To change body of created functions use File | Settings | File Templates.
            Log.d("_login","exception" + exception!!.message  )
            toast(exception!!.message.toString())
        }
    }



}

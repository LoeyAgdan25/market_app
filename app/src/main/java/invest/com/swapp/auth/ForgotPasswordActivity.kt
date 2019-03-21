package invest.com.swapp.auth

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import invest.com.swapp.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        this.supportActionBar!!.show()
        this.supportActionBar!!.title = "Forgot Password"
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var extras = intent.extras
        if(extras != null){
            var dest = extras.getString("destination")
            var delMed = extras.getString("deliveryMed")

            Log.d("_forgot_password","Destination: ${dest} ${delMed}")
        }


        btn_send_forgot_password.setOnClickListener { doForgotPasswordTapped() }
    }

    private fun doForgotPasswordTapped(){
        getCode()
    }

    private fun getCode(){
        var newPassword = txt_forget_password_email.text.toString()
        if(newPassword == null || newPassword.isEmpty()){
            return
        }

        var verCode = txt_confirmation_code.text.toString()
        if(verCode == null || verCode.isEmpty()){
            return
        }

        exit(newPassword,verCode)

    }

    private fun exit(newPass:String, code:String){
        var intent = Intent()

        var n = newPass
        var c = code

        if(n == null || c == null){
            n = ""
            c = ""
        }

        intent.putExtra("newPass",n)
        intent.putExtra("code",c)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}

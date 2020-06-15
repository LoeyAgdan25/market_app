package invest.com.swapp.auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import invest.com.swapp.MasterActivity
import invest.com.swapp.R
import invest.com.swapp.communication.AuthManager
import invest.com.swapp.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.content_signup.*
import kotlinx.android.synthetic.main.content_signup.field_layout_container
import kotlinx.android.synthetic.main.content_signup.linear_progress_circular
import kotlinx.android.synthetic.main.content_signup.password_layout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert
import org.jetbrains.anko.contentView
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {

    private val PASSWORD_POLICY = """Password should be minimum 8 characters long,
            |at least one number""".trimMargin()

    lateinit var authViewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)
        this.supportActionBar!!.title = "Sign Up"
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = resources.getColor(R.color.colorAccent)

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        authViewModel.response.observe(this, Observer {
            if(it.contains("success")){
                toast("Congratulations your account is created.")
                var intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else{
                toast("Oops. There something wrong. Please try again later.")
            }
        })

        btn_signup.setOnClickListener{
            var validEmail = validateEmail(txt_email.text.toString())
            var validPassword = validatePassword(txt_password_2.text.toString())

            if(!validEmail){
                if(txt_email.text!!.isEmpty()){
                    txt_email_layout.error = "this field cannot be empty"
                }else{
                    txt_email_layout.error = "Invalid email"
                }
            }

            if(!validPassword){
                password_layout.error = "Should be 8 character long"
            }

            if(!comparePassword()){
                password_layout_verify.error = "Password mismatched"
            }

            if(validEmail && validPassword && comparePassword()){

                 val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                 imm.hideSoftInputFromWindow( contentView!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                 linear_progress_circular.visibility = View.VISIBLE
                 field_layout_container.visibility = View.GONE
                 doSignUpTapped()
            }
        }

        txt_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {
                txt_email_layout.error = null}

            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {
                if (s.isNotEmpty()){}
            }
        })

        txt_password_2.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password_layout.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        txt_password_2_verify.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password_layout_verify.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }



    private fun doSignUpTapped(){
       GlobalScope.launch {
            authViewModel.register(txt_email.text.toString(), txt_email.text.toString(), txt_password_2.text.toString())
       }
    }

    /**
     *  Credential validation
     *
     * */

    private fun validateEmail(email: String): Boolean {
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()){
            return true
        }
            return false
    }

    private fun comparePassword(): Boolean{
        if(txt_password_2_verify.text.toString() == txt_password_2.text.toString()){
            return true
        }

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
            if(error != null){}
        }

        if(password.contains(" ")){
            return false
        }

        return valid
    }


}



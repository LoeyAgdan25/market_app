package invest.com.swapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import invest.com.swapp.R

import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.content_confirmation.*
import org.jetbrains.anko.toast
import java.lang.Exception

class ConfirmationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        setSupportActionBar(toolbar)


        txt_email_confirm.setText(intent.getStringExtra("email").toString())

        btn_confirm.setOnClickListener { confirmCodeTqpped()  }


    }

    private fun confirmCodeTqpped(){


    }
}

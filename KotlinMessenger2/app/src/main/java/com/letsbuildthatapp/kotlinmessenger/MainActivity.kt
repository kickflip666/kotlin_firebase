package com.letsbuildthatapp.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        register_button_register.setOnClickListener {


            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()
            val username = username_edittext_register.text.toString()

            Log.d("MainActivity", "Email is: " + email)
            Log.d("MainActivity", "Password: $password")


        }

        already_have_the_account_text_view.setOnClickListener {

            Log.d("MainActivity","Try to show login activity")
        }
    }
}

package com.letsbuildthatapp.kotlinmessenger.registration

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.letsbuildthatapp.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{

            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()



            Log.d("Login","Attempt with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            // .addOnCompleteListener()
            // .add


        }

        back_to_register_textview.setOnClickListener {
            finish()

        }

    }




}


package com.letsbuildthatapp.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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
            Log.d("MainActivity","Username: " + username)

            //Firebase authentication to create user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)

                .addOnCompleteListener{

                   if (!it.isSuccessful) return@addOnCompleteListener

                    //eles if successful

                    Log.d("Main","Successfully created user with uid: ${it.result?.user?.uid}")
                }

        }

        already_have_the_account_text_view.setOnClickListener {

            Log.d("MainActivity","Try to show login activity")
        }
    }
}

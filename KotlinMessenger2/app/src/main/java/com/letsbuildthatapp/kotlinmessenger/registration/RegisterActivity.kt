package com.letsbuildthatapp.kotlinmessenger.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.letsbuildthatapp.kotlinmessenger.messages.LatestMessagesActivity
import com.letsbuildthatapp.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        register_button_register.setOnClickListener {
        performRegister()


        }
        already_have_the_account_text_view.setOnClickListener {

            Log.d("RegisterActivity","Try to show login activity")
            //launch the login activity somehow
            val intent = Intent ( this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener{
            Log.d("RegisterActivity", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            startActivityForResult(intent, 0)


        }
    }

    var selectedPhotoUri: Uri? = null

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
                //proceed and check what selected image was...
                Log.d("RegisterActivity","Photo was selected")
                Log.d("RegisterActivity","Photo data: ${data.data}")

                selectedPhotoUri = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

                selectphoto_imageview_register.setImageBitmap(bitmap)
                selectphoto_button_register.alpha = 0f

               // val bitmapDrawable = BitmapDrawable(bitmap)
               // selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
            }
    }

    private fun performRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val username = username_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this,"Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is:$email")
        Log.d("RegisterActivity", "Password: $password")
        Log.d("RegisterActivity","Username: $username")

        //Firebase authentication to create user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                //else if successful
                Log.d("RegisterActivity","Successfully created user with uid: ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d ("RegisterActivity","Failed to create user: ${it.message}")
                Toast.makeText(this,"Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun uploadImageToFirebaseStorage(){
        Log.d("RegisterActivity","URL: ${selectedPhotoUri}")

        if (selectedPhotoUri == null) return
val filename = UUID.randomUUID().toString()
val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        Log.d("RegisterActivity","URL: ${filename}")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {

                Log.d("RegisterActivity","Successfully upload image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.d("RegisterActivity","File Location: $it")

                    saveUserToFireBaseDataBase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity","Failed: ${it.message}")



            }
    }
    private fun saveUserToFireBaseDataBase(profileImageURL: String){
       val uid = FirebaseAuth.getInstance().uid ?: ""
      val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            username_edittext_register.text.toString(),
            profileImageURL
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
                val intent = Intent (this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to write data: ${it.message}")


            }
    }
}

class User(val uid: String, val usename: String, val profileImageURL: String){
    constructor() : this("","","")
}